package src.sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import src.entities.mStates;
import src.entities.mThief;
import src.entities.oStates;
import src.entities.oThief;
import src.infrastructure.*;

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
     * Reference to the array containing all Assault Party shared regions
     */
    private AssaultParty[] aParties;

    /**
     * Reference to the general Repository shared region
     */
    private final GeneralRepos repos;

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
     * 
     */
    private int waitingThieves;

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
     * @throws MemException
     */

    public ControlCollectionSite(AssaultParty[] aParties, GeneralRepos repos, int roomNumber, int thiefMax)
            throws MemException {

        this.lock = new ReentrantLock();
        this.canvasCond = lock.newCondition();
        this.prepAssaultCond = lock.newCondition();
        this.readyCond = lock.newCondition();
        this.canvasRecvCond = lock.newCondition();
        this.signalCond = lock.newCondition();

        this.aParties = aParties;
        this.repos = repos;
        this.emptyRooms = new boolean[roomNumber];
        this.partyRunStatus = new boolean[aParties.length];

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
        this.waitingThieves = 0;
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
     * @throws InterruptedException
     */
    public boolean amINeeded() throws InterruptedException {
        lock.lock();
        availableThieves++;
        oThief curThread = (oThief) Thread.currentThread();
        if (!curThread.isFirstCycle()) {
            repos.setOrdinaryThiefState(curThread.getThiefID(), oStates.CONCENTRATION_SITE);
            repos.setOrdinaryThiefPartyState(curThread.getThiefID(), 'W');
            repos.removeThiefFromAssaultParty(curThread.getThiefID(), curThread.getCurAP());
            curThread.setFirstCycle(false);
        }
        readyCond.signal();
        prepAssaultCond.await();
        if (heistRun) {
            if (thiefSlots >= 0) {
                signalCond.signal();
                thiefSlots--;
                availableThieves--;
                waitingThieves++;
            }
            lock.unlock();
            return true;
        } else {
            prepAssaultCond.signal();
            lock.unlock();
            return false;
        }
    }

    /***
     * Sets up an Assault Party to be formed, assigning it a room and signaling the
     * Ordinary Thief threads
     * 
     * @return
     * @throws InterruptedException
     */
    public int prepareAssaultParty() throws InterruptedException {
        lock.lock();
        nextParty++;
        if (nextParty > 1) {
            nextParty = 0;
        }
        repos.setMasterThiefState(mStates.ASSEMBLING_A_GROUP);
        nextRoom = this.computeNextRoom();
        thiefSlots = 3;
        if (nextRoom == -1) {
            return -1;
        }
        repos.setAssaultPartyRoom(nextParty, nextRoom + 1);
        for (int i = 0; i < 3; i++) {
            prepAssaultCond.signal();
            if (thiefSlots >= 0) {
                signalCond.await();
            }
        }
        partyRunStatus[nextParty] = true;
        lock.unlock();
        return nextParty;
    }

    /***
     * Forces the Master Thief thread to sleep for a pre-determined amount of time
     * 
     * @throws InterruptedException
     */
    public void takeARest() throws InterruptedException {
        lock.lock();
        repos.setMasterThiefState(mStates.WAITING_FOR_GROUP_ARRIVAL);
        Thread.sleep(100);
        lock.unlock();
        return;
    }

    /***
     * Method for the retrieval of canvases from the Collection Site. Only one
     * canvas is processed in a method call
     * 
     * @throws InterruptedException
     */
    public void collectACanvas() throws InterruptedException {
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
                canvasRecvCond.await();
            }
        }
        repos.setMasterThiefState(mStates.DECIDING_WHAT_TO_DO);

        canvasCond.signalAll();
        lock.unlock();
    }

    /***
     * Method for the handing in of canvases
     * 
     * @throws MemException
     * @throws InterruptedException
     */
    public void handACanvas() throws MemException, InterruptedException {
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        queueSize++;

        while (!handIn) {
            canvasRecvCond.signal();
            canvasCond.await();
        }
        handIn = false;
        roomHandInQueue.write(curThread.getCurRoom());
        canvasHandInQueue.write(curThread.hasPainting());
        repos.setThiefCanvas(curThread.getCurAP(), curThread.getThiefID(), 0);
        canvasRecvCond.signal();
        // canvasCond.await();
        repos.removeThiefFromAssaultParty(curThread.getThiefID(), curThread.getCurAP());
        lock.unlock();
    }

    /***
     * Method for controlling the lifecycle of the Master Thief Thread
     * 
     * @return Integer value dependent on the current situation of the heist
     * @throws InterruptedException
     */
    public int appraiseSit() throws InterruptedException {
        lock.lock();

        int emptyCounterSit = 0;
        int returnValue;

        for (int i = 0; i < emptyRooms.length; i++) {
            if (emptyRooms[i]) {
                emptyCounterSit++;
            }
        }

        if (emptyCounterSit == emptyRooms.length) {
            if (queueSize != 0) {
                lock.unlock();
                return 1;
            }
            while (availableThieves < 6) {
                readyCond.await();
            }
            this.heistRun = false;

            lock.unlock();
            return 2;
        }

        if (availableThieves >= 3) {
            lock.unlock();
            return 0;
        }

        for (int i = 0; i < aParties.length; i++) {
            if (partyRunStatus[i]) {
                lock.unlock();
                return 1;
            }
        }
        if (availableThieves >= 3) {
            lock.unlock();
            return 0;
        }

        while (availableThieves < 3) {
            readyCond.await();
        }

        returnValue = 0;
        lock.unlock();
        return returnValue;
    }

    /***
     * Transitory method for initiating the simulation
     */
    public void startOperations() {
        lock.lock();
        System.out.println("startOperations");
        mThief curThread = (mThief) Thread.currentThread();
        curThread.setState(mStates.DECIDING_WHAT_TO_DO);
        repos.setMasterThiefState(mStates.DECIDING_WHAT_TO_DO);
        lock.unlock();
        return;
    }

    /***
     * Transitory method for closing off the simulation: signals all Ordinary Thief
     * threads upon exiting
     */
    public void sumUpResults() {
        lock.lock();
        System.out.println("sumResults");
        repos.setMasterThiefState(mStates.PRESENTING_THE_REPORT);
        repos.finalResult(this.totalPaintings);
        prepAssaultCond.signalAll();
        lock.unlock();
    }
}
