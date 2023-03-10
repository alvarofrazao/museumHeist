package sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import entities.oThief;
import entities.oStates;

public class AssaultParty {

    private int currentRoomID;
    private Museum museum;
    private oThief[] thieves;
    private int currentThiefNum;
    private int thiefMax;
    private ReentrantLock lock;
    private GeneralRepos repos;
    private int hasArrived;
    private Condition cond;
    private int S;

    public AssaultParty(int partySize, int thiefMax, int S) {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.thieves = new oThief[partySize];
        this.currentThiefNum = 0;
        this.thiefMax = thiefMax;
        this.hasArrived = 0;
        this.S = S;
    }

    public boolean isFull() {
        if (currentThiefNum >= thiefMax) {
            return true;
        } else
            return false;
    }

    public int getRoomID()
    {
        return currentRoomID;
    }

    public boolean crawlIn() {
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        int curThiefPPos = 0;
        // to copy and paste into other methods requiring this action
        // figuring out movement logic and algorithm

        if (hasArrived == thiefMax) {
            for (int i = 0; i < currentThiefNum; i++) {
                thieves[i].setState(oStates.AT_A_ROOM);
            }
            lock.unlock();
            return false;
        } else {
            for(;curThiefPPos < currentThiefNum;curThiefPPos++){
                if(thieves[curThiefPPos].getThiefID() == curThread.getThiefID())
                {
                    curThiefPPos = curThread.getThiefID();
                    break;
                }
            }
            if (thieves[curThiefPPos].getCurrentPosition() > museum.getRoomDistance(currentRoomID)) {
                if (thieves[curThiefPPos].getCurrentPosition() < (thieves[curThiefPPos + 1].getCurrentPosition() + S)) {
                    curThread.decrementPosition();
                    lock.unlock();
                } else {
                    lock.unlock();
                }

            }
        }
        return true;
    }

    public boolean crawlOut() {
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        int curThiefPPos = 0;

        if (hasArrived == thiefMax) {
            for (int i = 0; i < currentThiefNum; i++) {
                thieves[i].setState(oStates.COLLECTION_SITE);
            }
            return false;
        } else {
            for(;curThiefPPos < currentThiefNum;curThiefPPos++){
                if(thieves[curThiefPPos].getThiefID() == curThread.getThiefID())
                {
                    curThiefPPos = curThread.getThiefID();
                    break;
                }
            }
            if (thieves[curThiefPPos].getCurrentPosition() > 0) {
                if (thieves[curThiefPPos].getCurrentPosition() < (thieves[curThiefPPos + 1].getCurrentPosition() + S)) {
                    curThread.decrementPosition();
                    lock.unlock();
                } else {
                    lock.unlock();
                }

            }
            return true;
        }
    }

    public void reverseDirection() {
        lock.lock();
        if (hasArrived < currentThiefNum) {
            return;
        } else {
            for (int i = 0; i < currentThiefNum; i++) {
                thieves[i].setState(oStates.CRAWLING_OUTWARDS);
            }
            return;
        }
    }
}
