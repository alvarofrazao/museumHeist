package sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import entities.oThief;

public class ConcentrationSite {

    private ReentrantLock lock;
    private Condition cond;
    private AssaultParty[] aParties;
    private GeneralRepos repos;

    ConcentrationSite(AssaultParty[] aParties) {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.aParties = aParties;    }

    public int prepareExcursion() {
        int id;
        int i = 0;
        for (AssaultParty x : aParties) {
            if (!x.isFull()) {
                i++;
                break;
            }
        }
        id = i;
        return id;
    }

    public boolean amINeeded() {
        int i = 0;
        // oThief curThread = (oThief)Thread.currentThread();
        // to copy and paste into other methods requiring this action

        for (AssaultParty x : aParties) {
            if (!x.isFull()) {
                i++;
                break;
            }
        }
        if (i == aParties.length) {
            return false;
        } else
            return true;
    }

    public void prepareAssaultParty() {
        // mThief curThread = (mThief)Thread.currentThread();
        // to copy and paste into other methods requiring this action
    }

    public void sendAssaultParty() {
        // mThief curThread = (mThief)Thread.currentThread();
        // to copy and paste into other methods requiring this action
    }
}
