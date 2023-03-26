package src.sharedRegions;

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
    private final GeneralRepos repos;
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
        lock.lock();
        lock.unlock();
        return isRunning;
    }

    public int getRoomID() {
        return currentRoomID;
    }

    public void setupParty(int roomID) {
        lock.lock();
        //System.out.println("setupParty");
        currentThiefNum = 0;
        hasArrived = 0;
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
        oThief curThread = (oThief) Thread.currentThread();
        //System.out.println("addthief " + curThread.getThiefID()+" currentThiefNum = " +currentThiefNum );
        //thieves[currentThiefNum] = curThread;
        curThread.setPartyPos(currentThiefNum);
        repos.addThiefToAssaultParty(curThread.getThiefID(), this.id, currentThiefNum);
        thiefDist[currentThiefNum++] = 0;
        cond.await();
        //repos.setOrdinaryThiefState(curThread.getThiefID(), oStates.CRAWLING_INWARDS);
        lock.unlock();
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
        //System.out.println("crawlIn "+ curThread.getCurAP() + " " + curThread.getThiefID() + " " + curIdx + " " + thiefDist[curIdx]);
        boolean canMove = true;

        for (; move <= roomDist; move++) {
            behindDist = -1;

            if(!canMove){
                move--;
                thiefDist[curIdx] += move;
                System.out.println("ganza crawlin");
                repos.setThiefPosition(curThread.getCurAP(), curThread.getThiefID(), thiefDist[curIdx]);
                System.out.println("crawlIn "+ curThread.getCurAP() + " " + curThread.getThiefID() + " " + curIdx + " " + thiefDist[curIdx]);
                cond.signal();
                cond.await();
                canMove = true;
                curIdx = curThread.getPartyPos();
                behindDist = thiefDist[curIdx];
                move = 1;
            }

            nextPos = move + thiefDist[curIdx];

            if (nextPos >= roomDist) {
                //curThread.setPos(roomDist);
                thiefDist[curIdx] = roomDist;
                System.out.println("ganza chegou ao room");
                repos.setThiefPosition(curThread.getCurAP(), curThread.getThiefID(), roomDist);
                repos.setOrdinaryThiefState(curThread.getThiefID(), oStates.AT_A_ROOM);
                hasArrived += 1;
                // System.out.println("hasarrgived crawlIn = " + hasArrived);
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
        oThief curThread = (oThief) Thread.currentThread();
        int move = 1;
        int curIdx = curThread.getPartyPos();
        int behindDist;
        int nextPos = 0;
        int roomDist = museum.getRoomDistance(curThread.getCurRoom());
        //System.out.println("crawlOut "+ curThread.getCurAP() + " " + curThread.getThiefID() + " " + curIdx + " " + thiefDist[curIdx]);
        boolean canMove = true;

        for (; move <= roomDist; move++) {
            behindDist = -1;

            if(!canMove){
                move--;
                thiefDist[curIdx] -= move;
                System.out.println("ganza crawlout");
                repos.setThiefPosition(curThread.getCurAP(), curThread.getThiefID(), thiefDist[curIdx]);
                System.out.println("crawlOut "+ curThread.getCurAP() + " " + curThread.getThiefID() + " " + curIdx + " " + thiefDist[curIdx]);
                reverseCond.signal();
                reverseCond.await();
                // lock.lock();
                canMove = true;
                curThread = (oThief) Thread.currentThread();
                curIdx = curThread.getPartyPos();
                behindDist = roomDist;
                move = 1;
            }

            nextPos = move - thiefDist[curIdx];

            if (nextPos <= 0) {
                //curThread.setPos(roomDist);
                thiefDist[curIdx] = 0;
                System.out.println(curThread.getThiefID()+" ganza chegou ao control");
                repos.setThiefPosition(curThread.getCurAP(), curThread.getThiefID(), 0);
                repos.setOrdinaryThiefState(curThread.getThiefID(), oStates.COLLECTION_SITE);
                hasArrived++;
                System.out.println("hasArrived crawlout "+ id + " " +hasArrived);
                if(hasArrived == 3){
                    isRunning = false;
                }
                //log state at a room
                reverseCond.signal();
                lock.unlock();
                break;
            }

            for(int i = 0;i < 3; i++){
                if(thiefDist[i] > thiefDist[curIdx]){
                    if(thiefDist[i] < behindDist){
                        behindDist = thiefDist[i];
                    }
                }
            }

            for(int i = 0; i < 3; i++){
                if(nextPos == thiefDist[i]){
                    break;
                }
            }

            if(nextPos < (behindDist -S )){
                canMove = false;
            }
        }
        return;
    }
    
    

    public void reverseDirection() throws InterruptedException {
        lock.lock();
        hasArrived--;
        if(hasArrived > 0){
            //System.out.println("no signal in revdir");
            reverseCond.await();
            //repos.setOrdinaryThiefState(((oThief) Thread.currentThread()).getThiefID(), oStates.CRAWLING_OUTWARDS);
            //System.out.println("proceeded");
            lock.unlock();
            return;
        }else{
            //System.out.println("signal in revdir");
            reverseCond.signal();
            reverseCond.await();
            //repos.setOrdinaryThiefState(((oThief) Thread.currentThread()).getThiefID(), oStates.CRAWLING_OUTWARDS);
            //System.out.println("lastThread in revdir");
            lock.unlock();
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



    public void signalDeparture() throws InterruptedException {
        lock.lock();
        isRunning = true;
        //System.out.println("signalDep");
        cond.signal();
        lock.unlock();
        return;
    }
}
