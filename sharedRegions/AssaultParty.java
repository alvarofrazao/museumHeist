package sharedRegions;

import java.util.Arrays;
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

    public int getRoomID() {
        return currentRoomID;
    }

    public boolean crawlIn() {
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        int curThiefPartyPos = 0;

        if (hasArrived == thiefMax) {
            for (int i = 0; i < currentThiefNum; i++) {
                thieves[i].setState(oStates.AT_A_ROOM);
            }
            lock.unlock();
            return false;
        } else {
            int nextMove = 1;
            int nextPos;
            boolean canMove = true;
            for (; curThiefPartyPos < currentThiefNum; curThiefPartyPos++) {
                if (thieves[curThiefPartyPos].getThiefID() == curThread.getThiefID()) {
                    curThiefPartyPos = curThread.getThiefID();
                    break;
                }
            }
            for (; nextMove <= curThread.getMD(); nextMove++) {
                nextPos = curThread.getCurrentPosition() + nextMove;

                if (nextPos < museum.getRoomDistance(currentRoomID)) {
                    if (curThiefPartyPos != currentThiefNum) {
                        if (nextPos > (thieves[curThiefPartyPos + 1].getCurrentPosition() + S)) {
                            nextMove--;
                            break;
                        }
                    }

                    for (oThief thf : thieves) {
                        if (thf.getCurrentPosition() == nextPos) {
                            canMove = false;
                        }
                    }

                } else {
                    hasArrived++;
                    nextMove--;
                    break;
                }
            }
            if(!canMove)
            {
                curThread.moveIn(nextMove-1);
            }else{
                curThread.moveIn(nextMove);
            }        
        }
        lock.unlock();
        return true;
    }

    public boolean crawlOut() {
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        int curThiefPartyPos = 0;

        if (hasArrived == thiefMax) {
            for (int i = 0; i < currentThiefNum; i++) {
                thieves[i].setState(oStates.AT_A_ROOM);
            }
            lock.unlock();
            return false;
        } else {
            int nextMove = 1;
            int nextPos;
            boolean canMove = true;
            for (; curThiefPartyPos < currentThiefNum; curThiefPartyPos++) {
                if (thieves[curThiefPartyPos].getThiefID() == curThread.getThiefID()) {
                    curThiefPartyPos = curThread.getThiefID();
                    break;
                }
            }
            for (; nextMove <= curThread.getMD(); nextMove++) {
                nextPos = curThread.getCurrentPosition() + nextMove;

                if (nextPos > 0) {
                    if (curThiefPartyPos != currentThiefNum) {
                        if (nextPos > (thieves[curThiefPartyPos + 1].getCurrentPosition() + S)) {
                            nextMove--;
                            break;
                        }
                    }

                    for (oThief thf : thieves) {
                        if (thf.getCurrentPosition() == nextPos) {
                            canMove = false;
                        }
                    }
                } else {
                    hasArrived++;
                    nextMove--;
                    break;
                }

            }
            if(!canMove)
            {
                curThread.moveOut(nextMove+1);
            }else{
                curThread.moveOut(nextMove);
            }
        }
        lock.unlock();
        return true;
    }

    public void reverseDirection() {
        lock.lock();
        if (hasArrived < currentThiefNum) {
            lock.unlock();
            return;
        } else {
            for (int i = 0; i < currentThiefNum; i++) {
                thieves[i].setState(oStates.CRAWLING_OUTWARDS);
            }
            lock.unlock();
            return;
        }
    }

    public void sortByDistance() {
        // implement array sorting
    }
}
