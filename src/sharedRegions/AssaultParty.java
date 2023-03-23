package src.sharedRegions;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import src.entities.oStates;
import src.entities.oThief;

public class AssaultParty {

    private ReentrantLock lock;
    private Condition cond;
    private Condition reverseCond;
    private int currentRoomID;
    private Museum museum;
    private oThief[] thieves;
    private int currentThiefNum;
    private int thiefMax;
    private GeneralRepos repos;
    private int hasArrived;
    private int S;
    private boolean isRunning;

    public AssaultParty(int id, int partySize, int thiefMax, int S) {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.reverseCond = lock.newCondition();
        this.thieves = new oThief[partySize];
        this.currentThiefNum = 0;
        this.thiefMax = thiefMax;
        this.hasArrived = 0;
        this.S = S;
        this.isRunning = false;
    }

    Comparator<oThief> crawlInComparator = new Comparator<oThief>() {
        @Override
        public int compare(oThief t1, oThief t2) {
            return Integer.compare(t2.getCurrentPosition(), t1.getCurrentPosition());
        }
    };

    Comparator<oThief> crawlOutComparator = new Comparator<oThief>() {
        @Override
        public int compare(oThief t1, oThief t2) {
            return Integer.compare(t1.getCurrentPosition(), t2.getCurrentPosition());
        }
    };

    public boolean isFull() {
        if (currentThiefNum >= thiefMax) {
            return true;
        } else
            return false;
    }

    public int getPartySize() {
        return thiefMax;
    }

    public boolean getStatus() {
        return isRunning;
    }

    public int getRoomID() {
        return currentRoomID;
    }

    public void setReady() {
        lock.lock();
        isRunning = true;
        cond.signal();
        lock.unlock();
        return;
    }

    public int addThief() throws InterruptedException {
        lock.lock();
        System.out.println("addthief");
        oThief curThread = (oThief) Thread.currentThread();
        thieves[currentThiefNum] = curThread;
        currentThiefNum++;
        cond.await();
        return currentRoomID;
    }

    public void crawlIn() throws InterruptedException {
        lock.lock();
        System.out.println("crawlIn");
        oThief curThread = (oThief) Thread.currentThread();
        //log state crawling inwards
        int move = 1;
        int partyPos = 0;
        int nextPos = 0;
        int roomDist = museum.getRoomDistance(curThread.getCurRoom());
        boolean canMove = true;

        for (; move <= roomDist; move++) {
            if(!canMove){
                curThread.setPos(move-1);
                cond.signal();
                cond.await();
                lock.lock();
                System.out.println("crawlIn");
                canMove = true;
                curThread = (oThief) Thread.currentThread();
                move = 0;
            }

            nextPos = move + curThread.getCurrentPosition();

            if (nextPos >= roomDist) {
                curThread.setPos(roomDist);
                //log state at a room
                cond.signal();
                lock.unlock();
                break;
            }

            Arrays.sort(thieves, crawlInComparator);

            for (; partyPos < currentThiefNum; partyPos++) {
                if (thieves[partyPos].getThiefID() == curThread.getThiefID()) {
                    break;
                }
            }
            if(partyPos != (thieves.length-1)){
                if(nextPos > (thieves[partyPos+1].getCurrentPosition()+S)){
                    canMove = false;
                }
            }
        }
        return;
    }

    public void crawlOut() throws InterruptedException{
        lock.lock();
        System.out.println("crawlOut");
        oThief curThread = (oThief) Thread.currentThread();
        //log state crawling outwards
        int move = 1;
        int partyPos = 0;
        int nextPos = 0;
        int roomDist = museum.getRoomDistance(curThread.getCurRoom());
        boolean canMove = true;

        for (; move <= roomDist; move++) {
            if(!canMove){
                curThread.moveOut(move-1);
                cond.signal();
                cond.await();
                lock.lock();
                System.out.println("crawlOut");
                canMove = true;
                curThread = (oThief) Thread.currentThread();
                move = 0;
            }

            nextPos =curThread.getCurrentPosition() - move;

            if (nextPos <= 0) {
                curThread.setPos(0);
                //log state at a room
                cond.signal();
                lock.unlock();
                break;
            }

            Arrays.sort(thieves, crawlOutComparator);

            for (; partyPos < currentThiefNum; partyPos++) {
                if (thieves[partyPos].getThiefID() == curThread.getThiefID()) {
                    break;
                }
            }
            if(partyPos != (thieves.length-1)){
                if(nextPos < (thieves[partyPos+1].getCurrentPosition()-S)){
                    canMove = false;
                }
            }
        }
        return;
    }

    public void reverseDirection() throws InterruptedException {
        lock.lock();
        System.out.println("revdir");
        hasArrived--;
        if (hasArrived >= 0) {
            reverseCond.await();
            return;
        } else {
            reverseCond.signal();
            reverseCond.await();
            return;
        }
    }

    public boolean wasILast() {
        if (currentThiefNum == thiefMax) {
            return true;
        } else {

            return false;
        }
    }

    public void setupParty(int roomID) {
        lock.lock();
        currentThiefNum = 0;
        isRunning = false;
        currentRoomID = roomID;
        lock.unlock();
    }

    public void signalDeparture() throws InterruptedException {
        lock.lock();
        cond.signal();
        lock.unlock();
        return;
    }
}
