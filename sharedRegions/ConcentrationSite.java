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

    public void prepareExcursion(int partyNum) { //????
        aParties[partyNum].depart();
    }

    public boolean amINeeded() {
        int i = 0;
        lock.lock();
        oThief curThread = (oThief)Thread.currentThread();
        //provavelmente tem que mudar o estado da thread?
        boolean returnValue = true;
        for (AssaultParty x : aParties) {
            if (!x.isFull()) {
                x.addThief(curThread);
                curThread.setAssaultParty(i);
                availableThieves--;
                returnValue = false;
                break;
            }
            else{
                i++;
                returnValue = true;
            }

        }
        return returnValue;

    }

    public boolean wasILast(){
        if()
    }

    public void prepareAssaultParty() {
        mThief curThread = (mThief)Thread.currentThread();
    }

    public void sendAssaultParty() {
        mThief curThread = (mThief)Thread.currentThread();
    }
}
