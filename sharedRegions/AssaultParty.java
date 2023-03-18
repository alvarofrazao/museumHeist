package sharedRegions;

import java.util.Arrays;
import java.util.Comparator;
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

    public void addThief(oThief t) {
        lock.lock();
        thieves[currentThiefNum] = t;
        currentThiefNum++;
        lock.unlock();
        return;
    }

    public int getRoomID() {
        return currentRoomID;
    }

    // rever o crawlIn e o crawlOut
    public void crawlIn() throws InterruptedException {
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        int curThiefPartyPos = 0;
        int nextMove = 1;
        int nextPos;
        boolean canMove = true;
        boolean canIncrement = true;
        while(canMove){
            /*incrementa o que poder mas bloqueia se estiver numa situaçao em que nao poderia incrementar se proseguisse */
            if(!canIncrement){
                curThread.moveIn(nextMove);
                lock.unlock();
                cond.await();
                lock.lock();
                canIncrement = true;
                nextMove = 1;
                curThiefPartyPos = 0;
                curThread = (oThief) Thread.currentThread();
            }

            //organizar o array por distancia ao objetivo
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
    // crawlIn original
    /*
     * public boolean crawlIn() {
     * lock.lock();
     * oThief curThread = (oThief) Thread.currentThread();
     * int curThiefPartyPos = 0;
     * 
     * if (hasArrived == thiefMax) {
     * for (int i = 0; i < currentThiefNum; i++) {
     * thieves[i].setState(oStates.AT_A_ROOM);
     * }
     * lock.unlock();
     * return false;
     * } else {
     * int nextMove = 1;
     * int nextPos;
     * boolean canMove = true;
     * for (; curThiefPartyPos < currentThiefNum; curThiefPartyPos++) {
     * if (thieves[curThiefPartyPos].getThiefID() == curThread.getThiefID()) {
     * curThiefPartyPos = curThread.getThiefID();
     * break;
     * }
     * }
     * for (; nextMove <= curThread.getMD(); nextMove++) {
     * nextPos = curThread.getCurrentPosition() + nextMove;
     * 
     * if (nextPos < museum.getRoomDistance(currentRoomID)) {
     * if (curThiefPartyPos != currentThiefNum) {
     * if (nextPos > (thieves[curThiefPartyPos + 1].getCurrentPosition() + S)) {
     * nextMove--;
     * break;
     * }
     * }
     * 
     * for (oThief thf : thieves) {
     * if (thf.getCurrentPosition() == nextPos) {
     * canMove = false;
     * }
     * }
     * 
     * } else {
     * hasArrived++;
     * nextMove--;
     * break;
     * }
     * }
     * if(!canMove)
     * {
     * curThread.moveIn(nextMove-1);
     * }else{
     * curThread.moveIn(nextMove);
     * }
     * }
     * lock.unlock();
     * return true;
     * }
     */

   public void crawlOut() throws InterruptedException {
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        int curThiefPartyPos = 0;
        int nextMove = 1;
        int nextPos;
        boolean canMove = true;
        boolean canIncrement = true;
        while(canMove){
            /*incrementa o que poder mas bloqueia se estiver numa situaçao em que nao poderia incrementar se proseguisse */
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
            //organizar o array por distancia ao destino
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

    public void reverseDirection() throws InterruptedException {
        cond.signal();
        if(hasArrived < currentThiefNum){
            cond.await();
        }
    }

    public void prepareExcursion() throws InterruptedException {
        if (this.isFull()) {
            cond.signalAll();
        } else {
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
}
