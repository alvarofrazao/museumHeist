package serverSide.objects;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import infrastructure.*;
import serverSide.entities.*;
import serverSide.main.ServerControlSite;
import interfaces.*;

public class ControlCollectionSite implements CCLInterface {

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
    private final GeneralReposInterface grStub;

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

    public ControlCollectionSite(GeneralReposInterface grStub, int roomNumber, int thiefMax) {

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

    public ReturnInt getNextRoom() {
        ReturnInt ret = new ReturnInt(nextRoom, 99);
        return ret;
    }

    public ReturnBoolean getHeistStatus() {
        ReturnBoolean ret = new ReturnBoolean(heistRun,false);
        return ret;
    }

    /***
     * Controls the lifecycle of the Ordinary Thief threads
     * 
     * @return true or false, depending on the status of the heist
     */
    public ReturnBoolean amINeeded(int thid, boolean fc, int ap) {
        try {
            lock.lock();
            boolean fc_local = fc;
            ReturnBoolean ret;
            availableThieves++;
            if (!fc) {
                try {
                    grStub.setOrdinaryThiefState(thid, oStates.CONCENTRATION_SITE);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                try {
                    grStub.setOrdinaryThiefPartyState(thid, 'W');
                } catch (Exception e) {
                    // TODO: handle exception
                }
                try {
                    grStub.removeThiefFromAssaultParty(thid, ap);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                fc_local = false;
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
                ret = new ReturnBoolean(true, fc_local);
                return ret;
            } else {
                prepAssaultCond.signal();
                ret = new ReturnBoolean(false, fc_local);
                return ret;
            }
        } finally {
            lock.unlock();
        }

    }

    /***
     * Sets up an Assault Party to be formed, assigning it a room and signaling the
     * Ordinary Thief threads
     * 
     * @return
     */
    public ReturnInt prepareAssaultParty() {
        try {
            lock.lock();
            ReturnInt ret;
            nextParty++;
            if (nextParty > 1) {
                nextParty = 0;
            }
            try {
                grStub.setMasterThiefState(mStates.ASSEMBLING_A_GROUP);
            } catch (Exception e) {
                // TODO: handle exception
            }

            nextRoom = this.computeNextRoom();
            thiefSlots = 3;
            if (nextRoom == -1) {
                ret = new ReturnInt(-1, 99);
                return ret;
            }
            try {
                grStub.setAssaultPartyRoom(nextParty, nextRoom + 1);
            } catch (Exception e) {
                // TODO: handle exception
            }
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
            ret = new ReturnInt(nextParty, 99);
            return ret;
        } finally {
            lock.unlock();
        }

    }

    /***
     * Forces the Master Thief thread to sleep for a pre-determined amount of time
     * 
     * @throws InterruptedException
     */
    public void takeARest() {
        try {
            lock.lock();
            try {
                grStub.setMasterThiefState(mStates.WAITING_FOR_GROUP_ARRIVAL);
            } catch (Exception e) {
                // TODO: handle exception
            }
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                // TODO: handle exception
            }
            return;
        } finally {
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
                        System.out.println("Master thread holding in COLCAN");
                        canvasRecvCond.await();
                    } catch (Exception f) {
                    }
                    handIn = true;
                }
            }
            try {
                grStub.setMasterThiefState(mStates.DECIDING_WHAT_TO_DO);
            } catch (Exception e) {
                // TODO: handle exception
            }
            System.out.println("Master thread leaving COLCAN");
        } finally {
            lock.unlock();
        }

    }

    /***
     * Method for the handing in of canvases
     * 
     */
    public void handACanvas(int thid, boolean canvas, int ap, int room) {
        try {
            lock.lock();
            queueSize++;

            while (!handIn) {
                System.out.println(
                        "Thread id: " + thid + " holding in HNDCAN");
                canvasRecvCond.signalAll();
                try {
                    canvasCond.await();
                } catch (Exception e) {
                }
            }
            handIn = false;

            try {
                roomHandInQueue.write(room);
            } catch (Exception e) {
            }
            try {
                canvasHandInQueue.write(canvas);
            } catch (Exception e) {
            }
            System.out.println("Thread id: " + thid + " from room: " + room + " handed " + canvas + " canvas");
            try {
                grStub.setThiefCanvas(ap, thid, 0);
            } catch (Exception e) {
                // TODO: handle exception
            }
            try {
                grStub.removeThiefFromAssaultParty(thid, ap);
            } catch (Exception e) {
                // TODO: handle exception
            }
            canvasRecvCond.signal();
            System.out.println("Thread id: " + thid + " leaving HNDCAN");
        } finally {
            lock.unlock();
        }
    }

    /***
     * Method for controlling the lifecycle of the Master Thief Thread
     * 
     * @return Integer value dependent on the current situation of the heist
     */
    public ReturnInt appraiseSit() {
        try {
            lock.lock();
            ReturnInt ret;
            int emptyCounterSit = 0;

            for (int i = 0; i < emptyRooms.length; i++) {
                if (emptyRooms[i]) {
                    emptyCounterSit++;
                }
            }

            System.out.println("EMTPY ROOMS: " + emptyCounterSit);
            System.out.println("QUEUE SIZE: " + queueSize);

            if (emptyCounterSit == emptyRooms.length) {
                if (queueSize != 0) {
                    ret = new ReturnInt(1, 99);
                    return ret;
                }
                System.out.println("Master thread waiting for ths to terminate");

                if (availableThieves < 6) {
                    ret = new ReturnInt(1, 99);
                    return ret;
                } else {
                    this.heistRun = false;
                    ret = new ReturnInt(2, 99);
                    return ret;
                }
            }

            if (availableThieves >= 3) {
                ret = new ReturnInt(0, 99);
                return ret;
            }

            for (int i = 0; i < 2; i++) {
                if (partyRunStatus[i]) {
                    ret = new ReturnInt(1, 99);
                    return ret;
                }
            }
            if (availableThieves >= 3) {
                ret = new ReturnInt(0, 99);
                return ret;
            }

            while (availableThieves < 3) {
                System.out.println("Master thread waiting for ths to continue");
                try {
                    readyCond.await();
                } catch (Exception e) {
                }
            }
            ret = new ReturnInt(0, 99);
            return ret;
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
            try {
                grStub.setMasterThiefState(mStates.DECIDING_WHAT_TO_DO);
            } catch (Exception e) {
                // TODO: handle exception
            }

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
            try {
                grStub.setMasterThiefState(mStates.PRESENTING_THE_REPORT);
                grStub.finalResult(this.totalPaintings);
                grStub.shutdown();
            } catch (Exception e) {
                // TODO: handle exception
            }
            prepAssaultCond.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        ServerControlSite.shutdown();
    }
}
