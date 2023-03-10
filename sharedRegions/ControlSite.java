package sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ControlSite { // this shared region houses both what is dubbed the Control Site
                           // as well as the Collection Site

    private ReentrantLock lock;
    private Condition cond;
    //do these variables make sense? check later
    private AssaultParty[] aParties;
    private Museum museum;
    private GeneralRepos repos;

    public ControlSite(AssaultParty[] aParties, GeneralRepos repos) {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.aParties = aParties;
        this.repos = repos;
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
        // oThief curThread = (oThief)Thread.currentThread();
        // to copy and paste into other methods requiring this action
    }

    public void appraiseSit() {
        // mThief curThread = (mThief)Thread.currentThread();
        // to copy and paste into other methods requiring this action

    }

    public void startOperations() {
        // mThief curThread = (mThief)Thread.currentThread();
        // to copy and paste into other methods requiring this action

    }

    public void sumUpResults() {
        // mThief curThread = (mThief)Thread.currentThread();
        // to copy and paste into other methods requiring this action

    }

}
