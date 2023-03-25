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
    private Condition setupCond;

    private int currentRoomID;
    private Museum museum;
    //private oThief[] thieves;
    private int[] thiefDist;
    private int currentThiefNum;
    private int thiefMax;
    private GeneralRepos repos;
    private int hasArrived;
    private int S;
    private int id;
    private boolean isRunning;

    public AssaultParty(int id, int partySize, int thiefMax, int S, Museum museum, GeneralRepos repos) {
        this.id = id;
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.reverseCond = lock.newCondition();
        this.setupCond = lock.newCondition();
        //this.thieves = new oThief[partySize];
        this.thiefDist = new int[partySize];
        this.museum = museum;
        this.repos = repos;
        this.currentThiefNum = 0;
        this.thiefMax = thiefMax;
        this.hasArrived = 0;
        this.S = S;
        this.isRunning = false;
    }

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

    public void setupParty(int roomID) {
        lock.lock();
        //System.out.println("setupParty");
        currentThiefNum = 0;
        isRunning = false;
        currentRoomID = roomID;
        while(currentThiefNum < 3){
            setupCond.signalAll();
            lock.unlock();
            lock.lock();
        }
        lock.unlock();
    }

    public int addThief() throws InterruptedException {
        lock.lock();
        setupCond.await();
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        //System.out.println("addthief " + curThread.getThiefID()+" currentThiefNum = " +currentThiefNum );
        //thieves[currentThiefNum] = curThread;
        curThread.setPartyPos(currentThiefNum);
        thiefDist[currentThiefNum++] = 0;
        cond.await();
        return currentRoomID;
    }


    public void crawlIn() throws InterruptedException {
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        //log state crawling inwards
        int move = 1;
        int curIdx = curThread.getPartyPos();
        int behindDist;
        int nextPos = 0;
        int roomDist = museum.getRoomDistance(curThread.getCurRoom());
        System.out.println("crawlIn "+ curThread.getCurAP() + " " + curThread.getThiefID() + " " + curIdx + " " + thiefDist[curIdx]);
        boolean canMove = true;

        for (; move <= roomDist; move++) {
            behindDist = -1;

            if(!canMove){
                move--;
                thiefDist[curIdx] += move;
                System.out.println("crawlIn "+ curThread.getCurAP() + " " + curThread.getThiefID() + " " + curIdx + " " + thiefDist[curIdx]);
                cond.signal();
                cond.await();
                lock.lock();
                canMove = true;
                curThread = (oThief) Thread.currentThread();
                curIdx = curThread.getPartyPos();
                behindDist = thiefDist[curIdx];
                move = 1;
            }

            nextPos = move + thiefDist[curIdx];

            if (nextPos >= roomDist) {
                //curThread.setPos(roomDist);
                thiefDist[curIdx] = roomDist;
                //log state at a room
                cond.signal();
                lock.unlock();
                break;
            }

            for(int i = 0;i < 3; i++){
                if(thiefDist[i] < thiefDist[curIdx]){
                    if(thiefDist[i] > behindDist){
                        behindDist = thiefDist[i];
                    }
                }
            }

            for(int i = 0; i < 3; i++){
                if(nextPos == thiefDist[i]){
                    break;
                }
            }

            if(nextPos > (behindDist + S)){
                canMove = false;
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
        //System.out.println("revdir");
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

    public void signalPrevious(){
        lock.lock();
        //System.out.println("sigprev");
        cond.signal();
        lock.unlock();
    }    

    public void revSignalPrevious(){
        lock.lock();
        //System.out.println("sigrev");
        reverseCond.signal();
        lock.unlock();
    }    

    public void signalDeparture() throws InterruptedException {
        lock.lock();
        System.out.println("signalDep");
        cond.signal();
        lock.unlock();
        return;
    }
}
