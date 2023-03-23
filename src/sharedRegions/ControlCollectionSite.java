package src.sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import src.entities.mStates;
import src.entities.mThief;
import src.entities.oThief;
import src.infrastructure.*;

public class ControlCollectionSite {

    private ReentrantLock lock;
    private Condition cond;
    private Condition canvasCond;
    private Condition prepAssaultCond;
    private Condition readyCond;
    private Condition canvasRecvCond;

    private AssaultParty[] aParties;
    private Museum museum;
    private GeneralRepos repos;

    private MemFIFO<oThief> waitingQueue;
    private boolean[] emptyRooms;

    private int totalPaintings;
    private int nextParty;
    private int nextRoom;
    private int lastRoom;
    private int availableThieves;
    private int waitingQueueSize;
    private boolean heistRun;


    public ControlCollectionSite(AssaultParty[] aParties, GeneralRepos repos, int roomNumber, int thiefMax)
            throws MemException {

        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.canvasCond = lock.newCondition();
        this.prepAssaultCond = lock.newCondition();
        this.readyCond = lock.newCondition();
        this.canvasRecvCond = lock.newCondition();

        this.aParties = aParties;
        this.repos = repos;
        this.emptyRooms = new boolean[roomNumber];
        this.totalPaintings = 0;
        this.availableThieves = 0;
        try {
            this.waitingQueue = new MemFIFO<oThief>(new oThief[thiefMax]);            
        } catch (MemException e) {
        }
        this.waitingQueueSize = 0;
        this.heistRun = true;
        this.nextParty = 0;
        this.lastRoom = -1;
    }

    public int getNextRoom() {
        for (int i = 0; i < emptyRooms.length; i++) {
            if (!emptyRooms[i] && (i != lastRoom)) {
                lastRoom = i;
                return i;
            }
        }
        return -1;
    }

    public boolean amINeeded() throws InterruptedException {
        lock.lock();
        availableThieves++;
        System.out.println("amINeeded");
        // log state concentration site
        readyCond.signal();
        prepAssaultCond.await();

        if (heistRun) {
            return true;
        } else {
            return false;
        }
    }

    public int prepareAssaultParty() throws InterruptedException {
        lock.lock();
        System.out.println("prepareAssaultParty");
        nextRoom = this.getNextRoom();
        if (nextRoom != -1) {
            return -1;
        }
        for (int i = 0; i < 3; i++) {
            prepAssaultCond.signal();
        }
        //log state
        lock.unlock();
        return nextParty;
    }

    public boolean getHeistStatus() {
        return heistRun;
    }

    public void takeARest() throws InterruptedException {
        lock.lock();
        System.out.println("takeARest");
        Thread.sleep(100);
        // log state
        lock.unlock();
        return;
    }

    public void collectACanvas() throws InterruptedException {
        lock.lock();
        System.out.println("collectACanvas");
        oThief lastThief;
        //outra opçao e fazer varias fifos para as varias informaçoes que tem que se armazenar (fifo para sala e fifo para bools)
        boolean collect = true;
        while (collect) {
            try {
                lastThief = waitingQueue.read();
                if (lastThief.hasPainting()) {
                    totalPaintings++;
                } else {
                    emptyRooms[lastThief.getCurRoom()] = true;
                }
                collect = false;
                waitingQueueSize--;
                break;
            } catch (MemException e) {
                canvasRecvCond.await();
                lock.lock();
            }
        }
        // log state
        canvasCond.signal();
        lock.unlock();
    }

    public void handACanvas() throws MemException, InterruptedException {
        lock.lock();
        System.out.println("handACanvas");
        oThief curThread = (oThief) Thread.currentThread();
        waitingQueue.write(curThread);
        waitingQueueSize++;
        canvasRecvCond.signal();
        canvasCond.await();
    }

    public int appraiseSit() throws InterruptedException {
        lock.lock();
        System.out.println("appraiseSit");
        // log state
        int emptyCounter = 0;
        int returnValue;

        for (int i = 0; i < emptyRooms.length; i++) {
            if (emptyRooms[i]) {
                emptyCounter++;
            }
        }

        if (emptyCounter == emptyRooms.length) {
            while (availableThieves < 6) {
                readyCond.await();
                lock.lock();
            }
            this.heistRun = false;
            lock.unlock();
            return 2;
        }

        if (waitingQueueSize > 0) {
            lock.unlock();
            return 1;
        }

        while (availableThieves < 3) {
            readyCond.await();
            lock.lock();
        }
        returnValue = 0;
        lock.unlock();
        return returnValue;
    }

    public void startOperations() {
        lock.lock();
        System.out.println("startOperations");
        mThief curThread = (mThief) Thread.currentThread();
        curThread.setState(mStates.DECIDING_WHAT_TO_DO);
        lock.unlock();
        return;
    }

    public void sumUpResults() {
        lock.lock();
        System.out.println("sumResults");
        // log state
        mThief curThread = (mThief) Thread.currentThread();
        // print heist results
        cond.signalAll();
        lock.unlock();
        // to copy and paste into other methods requiring this action

    }
}
