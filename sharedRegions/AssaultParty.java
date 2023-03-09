package sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import entities.oThief;

public class AssaultParty {

    private int currentRoomID;
    private oThief[] thieves; //use the entirety of the thief object or just thief IDs?
                              //thief IDs seem more reasonable since we can reference
                              // the current thread and use it's ID to see it's position
                              //is this reasonable?
                              //each thief needs to have its position in the party:
                              //directly assigned or discovered during runtime?
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
        //figuring out movement logic and algorithm

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
        //blocks all thieves until whole party is ready to move out
        //changes direction variable to -1;
        //changes hasArrived to 0, since movement target has changed 

    }
}
