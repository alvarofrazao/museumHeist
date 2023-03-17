package sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import entities.mThief;
import entities.oThief;

public class ConcentrationSite {

    private ReentrantLock lock;
    private Condition cond;
    private AssaultParty[] aParties;
    private GeneralRepos repos;
    private int availableThieves;
    private int totalThieves;


    ConcentrationSite(AssaultParty[] aParties, int totalNum) {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.aParties = aParties;
        this.totalThieves = totalNum;
        this.availableThieves = totalNum; 
    }

    public int getAvailableThieves(){
        return availableThieves;
    }

    public void amINeeded() throws InterruptedException {
        lock.lock();
        int i = 0;
        oThief curThread = (oThief)Thread.currentThread();
        cond.signalAll();
        for (AssaultParty x : aParties) {
            if (!x.isFull()) {
                x.addThief(curThread);
                curThread.setAssaultParty(i);
                availableThieves--;
                lock.unlock();
                x.prepareExcursion();
                
                break;
            }
            else{
                i++;
            }

        }
        //lock.unlock();
    }

    public boolean wasILast(){
        if(totalThieves == availableThieves)
        {
            return true;
        }
        else{
            
            return false;
        }
    }

    public void prepareAssaultParty() {
        mThief curThread = (mThief)Thread.currentThread();
    }

    public void sendAssaultParty() {
        mThief curThread = (mThief)Thread.currentThread();
    }
}
