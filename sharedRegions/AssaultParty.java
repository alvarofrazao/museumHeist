package sharedRegions;

import java.util.concurrent.locks.ReentrantLock;

import entities.oThief;

public class AssaultParty {

    private int currentRoomID;
    private oThief[] thieves;
    private int currentThiefNum; // (?) tentative
    private int thiefMax;
    private ReentrantLock lock;
    private int direction;

    public AssaultParty(ReentrantLock lock, int partySize, int thiefMax){
        this.lock = lock;
        this.thieves = new oThief[partySize];
        this.currentThiefNum = 0;
        this.thiefMax = thiefMax;
    }

    public boolean isFull() {
        if (currentThiefNum >= thiefMax) {
            return true;
        } else
            return false;
    }

    public void sendAssaultParty() {

    }

    public boolean crawlIn() {
        int hasArrived = 0;
        if(hasArrived == thiefMax)
        {
            return false;
        }
        else return true;
    }

    public boolean crawlOut() {
        int hasArrived = 0;
        if(hasArrived == thiefMax)
        {
            return false;
        }
        else return true;
    }

    public void reverseDirection() {

    }
}
