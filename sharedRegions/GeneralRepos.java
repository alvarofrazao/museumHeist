package sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import entities.oThief;
import genclass.*;

public class GeneralRepos {
    private ReentrantLock lock;
    private Condition cond;
    // private String path;

    GeneralRepos() {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
    }

    public void printOThiefState(){
        lock.lock();
        oThief curThread = (oThief)Thread.currentThread();
        //print thief state nao sei se tem a haver com a assault party ou nao
        lock.unlock();
        return;
    }

}
