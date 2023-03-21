package sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import entities.oThief;
import entities.mStates;
import entities.mThief;

import infrastructure.*;

public class ControlCollectionSite { // this shared region houses both what is dubbed the Control Site
                           // as well as the Collection Site

    private ReentrantLock lock;
    private Condition cond;
    private AssaultParty[] aParties;
    private Museum museum;
    private GeneralRepos repos;
    private boolean[] emptyRooms;
    private int totalPaintings;
    private int availableThieves;
    private MemFIFO waitingQueue;


    public ControlCollectionSite(AssaultParty[] aParties, GeneralRepos repos, int roomNumber,int thiefMax) throws MemException {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.aParties = aParties;
        this.repos = repos;
        this.emptyRooms = new boolean[roomNumber];
        this.totalPaintings = 0;
        this.availableThieves = thiefMax;
        this.waitingQueue = new MemFIFO(thiefMax);
    }

    public boolean checkEmptyRooms(){
        return true;
    }

    public void takeARest() {
        // mThief curThread = (mThief)Thread.currentThread();
        // to copy and paste into other methods requiring this action

    }

    public void collectACanvas() {
        // mThief curThread = (mThief)Thread.currentThread();
        // to copy and paste into other methods requiring this action

    }

    public void handACanvas() throws MemException, InterruptedException {
        lock.lock();
        oThief curThread = (oThief)Thread.currentThread();
        waitingQueue.write(curThread);
        cond.signal();
        cond.await();

        /*if(curThread.hasPainting()){
            totalPaintings++;
        }
        else{
            emptyRooms[aParties[curThread.getCurAP()].getRoomID()] = true;
        }
        cond.signalAll();
        lock.unlock();*/
    }

    public int appraiseSit() {
        lock.lock();
        mThief curThread = (mThief)Thread.currentThread();
        int emptyCounter = 0;
        int returnValue = 1;
        for(int i = 0; i < emptyRooms.length;i++){
            if(emptyRooms[i])
            {
                emptyCounter++;
            }
        }

        if(emptyCounter == emptyRooms.length)
        {
            returnValue = 2;
        }

        if(availableThieves >= 3){
            returnValue = 0;
        }

        lock.unlock();
        return returnValue;

    }

    public void startOperations() {
        lock.lock();
        mThief curThread = (mThief)Thread.currentThread();
        curThread.setState(mStates.DECIDING_WHAT_TO_DO);
        lock.unlock();
        return;
    }

    public void sumUpResults() {
        // mThief curThread = (mThief)Thread.currentThread();
        // to copy and paste into other methods requiring this action

    }

    public void sendAssaultParty() {
        lock.lock();
    }
}

