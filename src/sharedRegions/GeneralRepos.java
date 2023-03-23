package src.sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import genclass.*;
import src.entities.mStates;
import src.entities.oStates;
import src.entities.oThief;

public class GeneralRepos {
    private ReentrantLock lock;
    private Condition cond;
    private String path;

    public GeneralRepos(String logPath) {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.path = logPath;
    }

    public void printOThiefState(){
        lock.lock();
        //print thief state nao sei se tem a haver com a assault party ou nao
        lock.unlock();
        return;
    }

}
