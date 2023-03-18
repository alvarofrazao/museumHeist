package sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import genclass.*;

public class GeneralRepos {
    private ReentrantLock lock;
    private Condition cond;
    // private String path;

    GeneralRepos() {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
    }
}
