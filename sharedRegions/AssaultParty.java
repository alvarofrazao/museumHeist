package sharedRegions;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import entities.oThief;
import infrastructure.MemException;
import infrastructure.MemFIFO;
import entities.oStates;

public class AssaultParty {

    private int currentRoomID;
    private Museum museum;
    private MemFIFO thieves;
    private int currentThiefNum;
    private int thiefMax;
    private ReentrantLock lock;
    private GeneralRepos repos;
    private int hasArrived;
    private Condition cond;
    private int S;
    private boolean isRunning;  
    private boolean isReady;

    public AssaultParty(int id,int partySize, int thiefMax, int S) throws MemException {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.thieves = new MemFIFO(partySize);
        this.currentThiefNum = 0;
        this.thiefMax = thiefMax;
        this.hasArrived = 0;
        this.S = S;
        this.isRunning = false;
        this.isReady = false;
    }

    Comparator<oThief> crawlInComparator=new Comparator<oThief>(){
        @Override public int compare(oThief t1,oThief t2){
            return Integer.compare(t2.getCurrentPosition(),t1.getCurrentPosition());
        }
    };

    Comparator<oThief> crawlOutComparator=new Comparator<oThief>(){
        @Override public int compare(oThief t1,oThief t2){
            return Integer.compare(t1.getCurrentPosition(),t2.getCurrentPosition());
        }
    };

    public boolean isFull() {
        if (currentThiefNum >= thiefMax) {
            return true;
        } else
            return false;
    }

    public int getPartySize(){
        return thiefMax;
    }

    public boolean getStatus(){
        return isRunning;
    }

    public int getRoomID() {
        return currentRoomID;
    }

    public void setReady(){
        isReady = true;
    }

    public void addThief(oThief t) throws MemException {
        lock.lock();
        thieves.write(t);
        currentThiefNum++;
        lock.unlock();
        return;
    }
/* 
    public void crawlIn() throws InterruptedException {
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        int curThiefPartyPos = 0;
        int nextMove = 1;
        int nextPos;
        boolean canMove = true;
        boolean canIncrement = true;

        while(canMove){
           if(!canIncrement){
                curThread.moveIn(nextMove);
                cond.signalAll();
                cond.await();
                lock.lock();
                canIncrement = true;
                nextMove = 1;
                curThiefPartyPos = 0;
                curThread = (oThief) Thread.currentThread();
            }

            Arrays.sort(thieves,crawlInComparator);
            for (; curThiefPartyPos < currentThiefNum; curThiefPartyPos++) {
                if (thieves[curThiefPartyPos].getThiefID() == curThread.getThiefID()){
                    curThiefPartyPos = curThread.getThiefID();
                    break;
                }
            }

            for (; nextMove <= curThread.getMD(); nextMove++){
                nextPos = curThread.getCurrentPosition() + nextMove;

                if (curThiefPartyPos != currentThiefNum) {
                    if (nextPos > (thieves[curThiefPartyPos + 1].getCurrentPosition() + S)) {
                        nextMove--;
                        canIncrement = false;
                        break;
                    }
                }

                for (oThief thf : thieves) {
                    if (thf.getCurrentPosition() == nextPos) {
                        nextMove--;
                        canIncrement = false;
                    }
                }
            }
            if(curThread.getCurrentPosition() > museum.getRoomDistance(currentRoomID)){
                canMove = false;
                hasArrived++;
                curThread.setPos(museum.getRoomDistance(currentRoomID));
                curThread.setState(oStates.AT_A_ROOM);
                lock.unlock();
                return;
            }       
        }
    }   


   public void crawlOut() throws InterruptedException {
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        int curThiefPartyPos = 0;
        int nextMove = 1;
        int nextPos;
        boolean canMove = true;
        boolean canIncrement = true;

        while(canMove){
            if(!canIncrement){
                curThread.moveOut(nextMove);
                lock.unlock();
                cond.await();
                lock.lock();
                canIncrement = true;
                curThread = (oThief) Thread.currentThread();
                nextMove = 1;
                curThiefPartyPos = 0;
            }
            Arrays.sort(thieves,crawlOutComparator);
            for (; curThiefPartyPos < currentThiefNum; curThiefPartyPos++) {
                if (thieves[curThiefPartyPos].getThiefID() == curThread.getThiefID()){
                    curThiefPartyPos = curThread.getThiefID();
                    break;
                }
            }

            for (; nextMove <= curThread.getMD(); nextMove++){
                nextPos = curThread.getCurrentPosition() + nextMove;

                if (curThiefPartyPos != currentThiefNum) {
                    if (nextPos > (thieves[curThiefPartyPos + 1].getCurrentPosition() - S)) {
                        nextMove--;
                        canIncrement = false;
                        break;
                    }
                }
                
                for (oThief thf : thieves) {
                    if (thf.getCurrentPosition() == nextPos) {
                        nextMove--;
                        canIncrement = false;
                    }
                }
            } 
            if(curThread.getCurrentPosition() < 0){
                canMove = false;
                hasArrived++;
                curThread.setPos(0);
                curThread.setState(oStates.COLLECTION_SITE);
                lock.unlock();
                return;
            }      
        }
    } 
*/
    

    public void reverseDirection() throws InterruptedException {
        cond.signal();
        if(hasArrived < currentThiefNum){
            cond.await();
        }
    }

    public boolean wasILast(){
        if(currentThiefNum == thiefMax)
        {
            return true;
        }
        else{
            
            return false;
        }
    }

    public void setupParty(int roomID){
        lock.lock();
        currentThiefNum = 0;
        currentRoomID = roomID;       
        lock.unlock();
    }

    public void signalThieves(){
        lock.lock();
        cond.signalAll();
        lock.unlock();
        return;
    }

    public void waitForSend() throws InterruptedException{
        lock.lock();
        while(!isReady){
            lock.lock();
            cond.wait();
        }
        return;
    }
}
