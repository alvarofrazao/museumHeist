package serverSide.sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import serverSide.entities.*;
import infrastructure.*;
import serverSide.main.ServerCollectionSite;
import serverSide.stubs.GeneralReposStub;

public class ControlCollectionSite {

    /**
     * Main monitor object for the class
     */
    private ReentrantLock lock;

    /**
     * Condition variable used to control access through the handACanvas method by
     * the Ordinary Thief threads
     */
    private Condition canvasCond;

    /**
     * Condition variable used to block Ordinary Thief threads before they join a
     * party
     */
    private Condition prepAssaultCond;

    /**
     * Condition variable used to signal the Master Thief thread during its blocking
     * stage in the appraiseSit method
     */
    private Condition readyCond;

    /**
     * Condition variable used signal the Master Thief thread when an Ordinary Thief
     * thread has arrived to hand in something
     */
    private Condition canvasRecvCond;

    /**
     * Condition variable used to control how many thieves are signaled during the
     * ASSEMBLING_A_GROUP state of the Master Thief thread
     */
    private Condition signalCond;

    /**
     * Stub of the general repository
     */
    private final GeneralReposStub grStub;

    /*
     * Array storing the status of all the rooms in the museum
     */
    private boolean[] emptyRooms;

    /**
     * Array storing the run status of all parties
     */
    private boolean[] partyRunStatus;

    /**
     * FIFO used to store the canvas values of the thief threads
     */
    private MemFIFO<Boolean> canvasHandInQueue;

    /**
     * FIFO used to store the information regarding which rooms the canvas values
     * where computed in
     */
    private MemFIFO<Integer> roomHandInQueue;

    /**
     * Total number of paintings stolen
     */
    private int totalPaintings;

    /**
     * Index of the next party to be formed
     */
    private int nextParty;

    /**
     * Index of the next room in the array to be targeted
     */
    private int nextRoom;

    /**
     * Index of the room that was targeted by the last party that was sent
     */
    private int lastRoom;

    /**
     * Number of thieves currently able to form a party
     */
    private int availableThieves;

    /**
     * Number of slots left in the currently forming party
     */
    private int thiefSlots;

    /**
     * Number of elements in the canvas queues
     */
    private int queueSize;

    /**
     * Flag to determine whether or not the thief threads can write in to the FIFO
     * objects
     */
    private boolean handIn;

    /**
     * Current status of the simulation
     */
    private boolean heistRun;

    /***
     * ControlCollectionSite object instantiation
     * 
     * @param aParties   Reference to array containg all AssaultParty shared memory
     *                   regions
     * @param repos      Reference to GeneralRepository shared memory region
     * @param roomNumber Number of rooms in the museum
     * @param thiefMax   Maximum number of thieves in total
     */

    public ControlCollectionSite(GeneralReposStub grStub, int roomNumber, int thiefMax) {

        this.lock = new ReentrantLock();
        this.canvasCond = lock.newCondition();
        this.prepAssaultCond = lock.newCondition();
        this.readyCond = lock.newCondition();
        this.canvasRecvCond = lock.newCondition();
        this.signalCond = lock.newCondition();

        this.grStub = grStub;
        this.emptyRooms = new boolean[roomNumber];
        this.partyRunStatus = new boolean[2];

        try {
            this.canvasHandInQueue = new MemFIFO<Boolean>(new Boolean[thiefMax]);
            this.roomHandInQueue = new MemFIFO<Integer>(new Integer[thiefMax]);
        } catch (MemException e) {
        }

        this.heistRun = true;
        this.handIn = true;

        this.totalPaintings = 0;
        this.thiefSlots = 3;
        this.availableThieves = 0;
        this.nextParty = -1;
        this.lastRoom = -1;
        this.nextRoom = -1;
        this.queueSize = 0;
    }

    /***
     * Determines which room to next send a party to
     * 
     * @return Room array index
     */
    public int computeNextRoom() {
        try {
            lock.lock();
            for (int i = 0; i < emptyRooms.length; i++) {
                if (!emptyRooms[i]) {
                    if (i != lastRoom) {
                        lastRoom = i;
                        break;
                    }
                }
            }
            return lastRoom;
        } finally {
            lock.unlock();
        }
    }

    public void printRoomStatus() {
        for (boolean x : emptyRooms) {
            System.out.println("Room status " + x);
        }
    }

    public int getNextRoom() {
        return nextRoom;
    }

    public boolean getHeistStatus() {
        return heistRun;
    }

    /***
     * Controls the lifecycle of the Ordinary Thief threads
     * 
     * @return true or false, depending on the status of the heist
     */
    public boolean amINeeded() {
        try{
            lock.lock();
            availableThieves++;
            cclClientProxy curThread = (cclClientProxy) Thread.currentThread();
            if (!curThread.getFC()) {
    
                grStub.setOrdinaryThiefState(curThread.getThId(), oStates.CONCENTRATION_SITE);
                grStub.setOrdinaryThiefPartyState(curThread.getThId(), 'W');
                grStub.removeThiefFromAssaultParty(curThread.getThId(), curThread.getAP());
                curThread.setFC(false);
            }
            readyCond.signal();
            try {
                prepAssaultCond.await();
            } catch (InterruptedException e) {
            }
            if (heistRun) {
                if (thiefSlots >= 0) {
                    signalCond.signal();
                    thiefSlots--;
                    availableThieves--;
                }
                return true;
            } else {
                prepAssaultCond.signal();
                return false;
            }
        }finally{
            lock.unlock();
        }
        
    }

    /***
     * Sets up an Assault Party to be formed, assigning it a room and signaling the
     * Ordinary Thief threads
     * 
     * @return
     */
    public int prepareAssaultParty() {
        try{
            lock.lock();
            nextParty++;
            if (nextParty > 1) {
                nextParty = 0;
            }
            grStub.setMasterThiefState(((cclClientProxy) Thread.currentThread()).getThId(), mStates.ASSEMBLING_A_GROUP);
            nextRoom = this.computeNextRoom();
            thiefSlots = 3;
            if (nextRoom == -1) {
                return -1;
            }
            grStub.setAssaultPartyRoom(nextParty, nextRoom + 1);
            for (int i = 0; i < 3; i++) {
                prepAssaultCond.signal();
                if (thiefSlots >= 0) {
                    try {
                        signalCond.await();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
            partyRunStatus[nextParty] = true;
            return nextParty;
        }finally{
            lock.unlock();
        }
        
    }

    /***
     * Forces the Master Thief thread to sleep for a pre-determined amount of time
     * 
     * @throws InterruptedException
     */
    public void takeARest() {
        try{
            lock.lock();
            grStub.setMasterThiefState(((cclClientProxy) Thread.currentThread()).getThId(),
                    mStates.WAITING_FOR_GROUP_ARRIVAL);
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                // TODO: handle exception
            }
            return;
        }finally{
            lock.unlock();
        }
        
    }

    /***
     * Method for the retrieval of canvases from the Collection Site. Only one
     * canvas is processed in a method call
     * 
     */
    public void collectACanvas() {
        try {
            lock.lock();
            int roomRead;
            boolean canvasRead;
            boolean collect = true;
            while (collect) {
                try {
                    roomRead = roomHandInQueue.read();
                    canvasRead = canvasHandInQueue.read();
                    queueSize--;
                    if (canvasRead) {
                        totalPaintings++;

                    } else {
                        emptyRooms[roomRead] = true;
                    }
                    collect = false;
                    handIn = true;
                    break;
                } catch (MemException e) {
                    canvasCond.signalAll();
                    handIn = true;
                    try {
                        System.out.println("Thread id: " + ((cclClientProxy) Thread.currentThread()).getThId()
                                + " holding in COLCAN");
                        canvasRecvCond.await();
                    } catch (Exception f) {
                    }
                    handIn = true;
                }
            }
            grStub.setMasterThiefState(((cclClientProxy) Thread.currentThread()).getThId(),
                    mStates.DECIDING_WHAT_TO_DO);

            System.out.println("Thread id: " + ((cclClientProxy) Thread.currentThread()).getThId() + " leaving COLCAN");
        } finally {
            lock.unlock();
        }

    }

    /***
     * Method for the handing in of canvases
     * 
     */
    public void handACanvas() {
        try {
            lock.lock();
            cclClientProxy curThread = (cclClientProxy) Thread.currentThread();
            queueSize++;

            while (!handIn) {
                System.out.println(
                        "Thread id: " + ((cclClientProxy) Thread.currentThread()).getThId() + " holding in HNDCAN");
                canvasRecvCond.signalAll();
                try {
                    canvasCond.await();
                } catch (Exception e) {
                }
            }
            handIn = false;

            try {
                roomHandInQueue.write(curThread.getRoom());
            } catch (Exception e) {
            }
            try {
                canvasHandInQueue.write(curThread.getHC());
            } catch (Exception e) {
            }
            System.out.println("Thread id: " + ((cclClientProxy) Thread.currentThread()).getThId() + " from room: "
                    + curThread.getRoom() + " handed " + curThread.getHC() + " canvas");
            grStub.setThiefCanvas(curThread.getAP(), curThread.getThId(), 0);
            canvasRecvCond.signal();
            grStub.removeThiefFromAssaultParty(curThread.getThId(), curThread.getAP());
            System.out.println("Thread id: " + ((cclClientProxy) Thread.currentThread()).getThId() + " leaving HNDCAN");
        } finally {
            lock.unlock();
        }
    }

    /***
     * Method for controlling the lifecycle of the Master Thief Thread
     * 
     * @return Integer value dependent on the current situation of the heist
     */
    public int appraiseSit() {
        try {
            lock.lock();

            int emptyCounterSit = 0;
            int returnValue;

            for (int i = 0; i < emptyRooms.length; i++) {
                if (emptyRooms[i]) {
                    emptyCounterSit++;
                }
            }

            System.out.println("EMTPY ROOMS: " + emptyCounterSit);
            System.out.println("QUEUE SIZE: " + queueSize);

            if (emptyCounterSit == emptyRooms.length) {
                if (queueSize != 0) {
                    return 1;
                }
                System.out.println("Thread ID: " + ((cclClientProxy) Thread.currentThread()).getThId()
                        + " waiting for ths to terminate");
                /*
                 * while (availableThieves < 6) {
                 * try {
                 * readyCond.await();
                 * } catch (Exception e) {
                 * }
                 * }
                 */
                if (availableThieves < 6) {
                    return 1;
                } else {
                    this.heistRun = false;
                    return 2;
                }
            }

            if (availableThieves >= 3) {
                return 0;
            }

            for (int i = 0; i < 2; i++) {
                if (partyRunStatus[i]) {
                    return 1;
                }
            }
            if (availableThieves >= 3) {
                return 0;
            }

            while (availableThieves < 3) {
                System.out.println("Thread ID: " + ((cclClientProxy) Thread.currentThread()).getThId()
                        + " waiting for ths to continue");
                try {
                    readyCond.await();
                } catch (Exception e) {
                }
            }

            returnValue = 0;
            return returnValue;
        } finally {
            lock.unlock();
        }

    }

    /***
     * Transitory method for initiating the simulation
     */
    public void startOperations() {
        try {
            lock.lock();
            System.out.println("startOperations");
            cclClientProxy curThread = (cclClientProxy) Thread.currentThread();
            curThread.setThState(mStates.DECIDING_WHAT_TO_DO);
            grStub.setMasterThiefState(curThread.getThId(), mStates.DECIDING_WHAT_TO_DO);
            return;
        } finally {
            lock.unlock();
        }

    }

    /***
     * Transitory method for closing off the simulation: signals all Ordinary Thief
     * threads upon exiting
     */
    public void sumUpResults() {
        try {
            lock.lock();
            System.out.println("sumResults");
            grStub.setMasterThiefState(((cclClientProxy) Thread.currentThread()).getThId(),
                    mStates.PRESENTING_THE_REPORT);
            grStub.finalResult(this.totalPaintings);
            grStub.shutdown();
            prepAssaultCond.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        try {
            lock.lock();
            ServerCollectionSite.waitConnection = false;
        } finally {
            lock.unlock();
        }
    }
}
