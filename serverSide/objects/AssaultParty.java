package serverSide.objects;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import genclass.GenericIO;
import interfaces.*;
import serverSide.entities.*;
import serverSide.main.ServerAssaultParty;

public class AssaultParty implements APInterface {

    /**
     * Main monitor object
     */
    private ReentrantLock lock;

    /**
     * Condition variable used before the ingoing movement
     */
    private Condition cond;

    /**
     * 
     */
    private Condition setupCond;

    /**
     * Condition variable used before and during the outgoing momevement
     */
    private Condition reverseCond;

    /**
     * Condition variable used for the party formation phase
     */
    private Condition partyFullCond;

    /**
     * Stub of the general repository
     */
    private final GeneralReposInterface grStub;
    /**
     * Index of the room currently assigned to the party
     */
    private int currentRoomID;

    /**
     * Array storing the current distance of each thief to the Collection Site
     */
    private int[] thiefDist;

    /**
     * Number of thieves currently in the party.
     */
    private int currentThiefNum;

    /**
     * Number of thieves that have arrived at the movement target (either Museum
     * room or Collection Site)
     */
    private int hasArrived;

    /**
     * Maximum separation between two consecutive thieves
     */
    private int S;

    /**
     * Index of the party in the party array
     */
    private int id;

    /**
     * Party status flag. True means the thieves are currently between the
     * CRAWLING_INWARDS and COLLECTION_SITE states
     */
    private boolean isRunning;

    /**
     * Control variables for the in-going movement
     */
    private boolean moveRestrictIn[];

    /**
     * Control variables for the outgoing movement
     */
    private boolean moveRestrictOut[];

    /***
     * Instantiation of AssaultParty object
     * 
     * @param id        Party id in the AssaultParty array: either 0 or 1
     * @param partySize Number of thieves in a single party
     * @param S         Maximum distance between to consecutive thieves
     * @param museum    Reference to the Museum shared memory region
     * @param repos     Reference to the GeneralRepository shared memory region
     */
    public AssaultParty(int id, int partySize, int thiefMax, int S, GeneralReposInterface grStub) {
        this.id = id;
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.reverseCond = lock.newCondition();
        this.partyFullCond = lock.newCondition();
        this.setupCond = lock.newCondition();
        this.thiefDist = new int[partySize];
        this.moveRestrictIn = new boolean[partySize];
        this.moveRestrictOut = new boolean[partySize];
        this.grStub = grStub;
        this.currentThiefNum = 0;
        this.hasArrived = 0;
        this.S = S;
        this.isRunning = false;
    }

    public ReturnBoolean getStatus() {
        ReturnBoolean ret = new ReturnBoolean(isRunning, false);
        return ret;
    }

    public ReturnInt getRoomID() {
        ReturnInt ret = new ReturnInt(currentRoomID, 0);
        return ret;
    }

    /***
     * Setups up AssaultParty variables for party formation
     * 
     * @param roomID index of the room assigned to the party
     */
    public void setupParty(int roomID) {
        try {
            lock.lock();
            currentThiefNum = 0;
            hasArrived = 0;
            isRunning = false;
            currentRoomID = roomID;
            while (currentThiefNum < 3) {
                try {
                    setupCond.signalAll();
                    partyFullCond.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /***
     * Assigns thief to party, and blocks waiting for departure signal.
     * 
     * @return Room index that was assigned to the party
     */
    public ReturnInt addThief(int thid) {
        try {
            ReturnInt ret;
            lock.lock();
            partyFullCond.signalAll();
            try {
                setupCond.await();
            } catch (Exception e) {
            }
            int partyPos;

            if (currentThiefNum >= 3) {
                partyPos = 2;
            } else {
                partyPos = currentThiefNum;
            }
            try {
                grStub.addThiefToAssaultParty(thid, this.id, currentThiefNum);
            } catch (Exception e) {
                GenericIO
                        .writelnString("Thief " + thid + "remote exception on addThieftoAssaultParty" + e.getMessage());
                System.exit(1);
            }
            thiefDist[partyPos] = 0;
            currentThiefNum++;
            moveRestrictIn[partyPos] = true;
            moveRestrictOut[partyPos] = true;
            partyFullCond.signalAll();
            while (moveRestrictIn[partyPos]) {
                try {
                    cond.await();
                } catch (Exception e) {
                }
            }
            try {
                grStub.setOrdinaryThiefState(thid, oStates.CRAWLING_INWARDS);
            } catch (Exception e) {
                GenericIO.writelnString("Thief " + thid + "remote exception on setOrdinaryThiefState" + e.getMessage());
                System.exit(1);
            }
            ret = new ReturnInt(currentRoomID, partyPos);
            return ret;
        } finally {
            lock.unlock();
        }
    }

    /***
     * Method for the ingoing movement, each thief moves until it is S units away
     * from the one behind them, then stops and signals another thief to move,
     * repeating the process until all three arrive at the Museum Room
     * 
     */
    public void crawlIn(int distance, int thid, int ppos) {
        try {
            lock.lock();
            int move = 1;
            int curIdx = ppos;

            int behindDist;
            int nextPos = 0;
            int roomDist = distance;
            boolean canMove = true;

            for (; move <= roomDist; move++) {
                behindDist = -1;

                if (!canMove) {
                    move--;
                    thiefDist[curIdx] += move;
                    try {
                        grStub.setThiefPosition(this.id, thid, thiefDist[curIdx]);
                    } catch (Exception e) {
                        GenericIO.writelnString(
                                "Thief " + thid + "remote exception on setThiefPosition" + e.getMessage());
                        System.exit(1);
                    }
                    moveRestrictIn[curIdx] = true;
                    if ((curIdx + 1) >= 3) {
                        moveRestrictIn[0] = false;
                    } else {
                        moveRestrictIn[curIdx + 1] = false;
                    }
                    cond.signalAll();
                    while (moveRestrictIn[curIdx]) {
                        try {
                            cond.await();
                        } catch (Exception e) {
                        }
                    }
                    canMove = true;
                    curIdx = ppos;
                    behindDist = thiefDist[curIdx];
                    move = 1;
                }

                nextPos = move + thiefDist[curIdx];

                // Arrival detection
                if (nextPos >= roomDist) {
                    thiefDist[curIdx] = roomDist;
                    try {
                        grStub.setThiefPosition(this.id, thid, roomDist);
                    } catch (Exception e) {
                        GenericIO.writelnString(
                                "Thief " + thid + "remote exception on setThiefPosition" + e.getMessage());
                        System.exit(1);
                    }

                    try {
                        grStub.setOrdinaryThiefState(thid, oStates.AT_A_ROOM);
                    } catch (Exception e) {
                        GenericIO.writelnString(
                                "Thief " + thid + "remote exception on setOrdinaryThiefState" + e.getMessage());
                        System.exit(1);
                    }
                    hasArrived += 1;
                    moveRestrictIn[curIdx] = true;
                    if ((curIdx + 1) >= 3) {
                        moveRestrictIn[0] = false;
                    } else {
                        moveRestrictIn[curIdx + 1] = false;
                    }
                    cond.signalAll();
                    break;
                }

                // Computing the index of the thief that is behind the current Thread
                for (int i = 0; i < 3; i++) {
                    if (thiefDist[i] < thiefDist[curIdx]) {
                        if (thiefDist[i] > behindDist) {
                            behindDist = thiefDist[i];
                        }
                    }
                }

                // matching distance detection
                for (int i = 0; i < 3; i++) {
                    if (nextPos == thiefDist[i]) {
                        move++;
                        break;
                    }
                }

                // Movement iteration stopping condition
                if (nextPos > (behindDist + S)) {
                    canMove = false;
                }
            }

        } finally {
            lock.unlock();
        }
    }

    /***
     * Method for the outgoing movement, each thief moves until it is S units away
     * from the one behind them, then stops and signals another thief to move,
     * repeating the process until all three arrive at the Collection Site
     * 
     */

    public void crawlOut(int distance, int thid, int ppos) {
        try {
            lock.lock();
            int move = 1;
            int curIdx = ppos;
            int behindDist;
            int nextPos = 0;
            int roomDist = distance;
            boolean canMove = true;

            for (; move <= roomDist; move++) {
                behindDist = roomDist + 1;

                if (!canMove) {
                    move--;
                    thiefDist[curIdx] -= move;
                    try {
                        grStub.setThiefPosition(this.id, thid, thiefDist[curIdx]);
                    } catch (Exception e) {
                        GenericIO.writelnString(
                                "Thief " + thid + "remote exception on setThiefPosition" + e.getMessage());
                        System.exit(1);
                    }
                    moveRestrictOut[curIdx] = true;
                    if ((curIdx + 1) >= 3) {
                        moveRestrictOut[0] = false;
                    } else {
                        moveRestrictOut[curIdx + 1] = false;
                    }
                    reverseCond.signalAll();
                    while (moveRestrictOut[curIdx]) {
                        try {
                            reverseCond.await();
                        } catch (Exception e) {
                        }
                    }

                    canMove = true;
                    behindDist = roomDist + 1;
                    move = 1;
                }

                nextPos = thiefDist[curIdx] - move;

                // Arrival detection
                if (nextPos <= 0) {
                    thiefDist[curIdx] = 0;
                    try {
                        grStub.setThiefPosition(this.id, thid, 0);
                    } catch (Exception e) {
                        GenericIO.writelnString(
                                "Thief " + thid + "remote exception on setThiefPosition" + e.getMessage());
                        System.exit(1);
                    }
                    try {
                        grStub.setOrdinaryThiefState(thid, oStates.COLLECTION_SITE);
                    } catch (Exception e) {
                        GenericIO.writelnString(
                                "Thief " + thid + "remote exception on setOrdinaryThiefState" + e.getMessage());
                        System.exit(1);
                    }
                    hasArrived++;
                    if (hasArrived == 3) {
                        isRunning = false;
                    }
                    moveRestrictOut[curIdx] = true;
                    reverseCond.signalAll();

                    if ((curIdx + 1) >= 3) {
                        moveRestrictOut[0] = false;
                    } else {
                        moveRestrictOut[curIdx + 1] = false;
                    }
                    reverseCond.signalAll();

                    break;
                }

                // Computing the index of the thief that is behind the current Thread
                for (int i = 0; i < 3; i++) {
                    if (thiefDist[i] > thiefDist[curIdx]) {
                        if (thiefDist[i] < behindDist) {
                            behindDist = thiefDist[i];
                        }
                    }
                }

                // matching distance detection
                for (int i = 0; i < 3; i++) {
                    if (nextPos == thiefDist[i]) {
                        move++;
                        break;
                    }
                }

                // Movement iteration stopping condition
                if (nextPos < (behindDist - S)) {
                    canMove = false;
                }
            }

        } finally {
            lock.unlock();
        }
    }

    /***
     * Method for preparing and initiating the outgoing movement:
     * the last thread to call the method signals one other thread waiting on the
     * condition to initiate the movement
     * 
     */

    public void reverseDirection(int thid, int ppos) {
        try {
            lock.lock();
            hasArrived--;
            if (hasArrived <= 0) {
                reverseCond.signalAll();
                moveRestrictOut[0] = false;
            }

            cond.signalAll();

            while (moveRestrictOut[ppos]) {
                try {
                    reverseCond.await();
                } catch (Exception e) {
                }
            }
            try {
                grStub.setOrdinaryThiefState(thid, oStates.CRAWLING_OUTWARDS);
            } catch (Exception e) {
                GenericIO.writelnString(
                        "Thief " + thid + "remote exception on setOrdinaryThiefState" + e.getMessage());
                System.exit(1);
            }
            return;
        } finally {

            lock.unlock();
        }
    }

    /***
     * Method that signals the start of the party's ingoing movement: the Master
     * signals a single thread to start moving
     * 
     */

    public void signalDeparture() {
        try {
            lock.lock();
            isRunning = true;
            moveRestrictIn[0] = false;
            cond.signalAll();

        } finally {

            lock.unlock();
        }
    }

    public void shutdown() {
        ServerAssaultParty.shutdown();
    }
}
