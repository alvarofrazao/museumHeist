package sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import entities.oThief;

public class AssaultParty {

    private int currentRoomID;
    private oThief[] thieves;
    private int currentThiefNum; // (?) tentative
    private int thiefMax;
    private ReentrantLock lock;
    private int direction;
    private GeneralRepos repos;
    private boolean isMoving;  // (?) tentative
    private int hasArrived;  // (?) tentative
    private Condition cond;  // (?) tentative

    public AssaultParty(ReentrantLock lck, int partySize, int thiefMax) {
        this.lock = lck;
        this.cond = lck.newCondition(); // maybe correct????? is this a reference to
                                        // the lock argument's condition, or a new condition entirely
        this.thieves = new oThief[partySize];
        this.currentThiefNum = 0;
        this.thiefMax = thiefMax;
        this.isMoving = false;
        this.hasArrived = 0;
        this.direction = 1; // tentative movement increment approach
    }

    public boolean isFull() {
        if (currentThiefNum >= thiefMax) {
            return true;
        } else
            return false;
    }

    public void insertThief() {

    }

    public boolean crawlIn() {
        // oThief curThread = (oThief)Thread.currentThread();
        // to copy and paste into other methods requiring this action

        if (hasArrived == thiefMax) {
            return false;
        } else
            return true;
    }

    public boolean crawlOut() {
        // oThief curThread = (oThief)Thread.currentThread();
        // to copy and paste into other methods requiring this action

        int hasArrived = 0;
        if (hasArrived == thiefMax) {
            return false;
        } else
            return true;
    }

    public void reverseDirection() {

    }
}
