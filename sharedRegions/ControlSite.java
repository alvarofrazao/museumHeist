package sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import entities.oThief;
import entities.mStates;
import entities.mThief;

public class ControlSite { // this shared region houses both what is dubbed the Control Site
                           // as well as the Collection Site

    private ReentrantLock lock;
    private Condition cond;
    //do these variables make sense? check later
    private AssaultParty[] aParties;
    private Museum museum;
    private GeneralRepos repos;
    private boolean[] emptyRooms;
    private int totalPaintings;
    private int availableThieves;


    public ControlSite(AssaultParty[] aParties, GeneralRepos repos, int roomNumber,int thiefMax) {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.aParties = aParties;
        this.repos = repos;
        this.emptyRooms = new boolean[roomNumber];
        this.totalPaintings = 0;
        this.availableThieves = thiefMax;
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

    public void handACanvas() {
        lock.lock();
        oThief curThread = (oThief)Thread.currentThread();
        if(curThread.hasPainting()){
            totalPaintings++;
        }
        else{
            emptyRooms[aParties[curThread.getCurAP()].getRoomID()] = true;
        }
        lock.unlock();
        cond.signalAll();
    }

    public int appraiseSit() {
        lock.lock();
        // mThief curThread = (mThief)Thread.currentThread();
        // to copy and paste into other methods requiring this action

    }

    public void startOperations() {
        lock.lock();
        mThief curThread = (mThief)Thread.currentThread();
        curThread.setState(mStates.DECIDING_WHAT_TO_DO);
        return;
    }

    public void sumUpResults() {
        // mThief curThread = (mThief)Thread.currentThread();
        // to copy and paste into other methods requiring this action

    }

    public void prepareAssaultParty() throws InterruptedException {
        mThief curThread = (mThief)Thread.currentThread();
        cond.signalAll();
        cond.await();
    }

    public void sendAssaultParty() {
        
        lock.lock();
    }
}

}
