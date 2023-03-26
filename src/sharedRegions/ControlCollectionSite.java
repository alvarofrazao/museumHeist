package src.sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import src.entities.mStates;
import src.entities.mThief;
import src.entities.oStates;
import src.entities.oThief;
import src.infrastructure.*;

public class ControlCollectionSite {

    private ReentrantLock lock;
    private Condition canvasCond;
    private Condition prepAssaultCond;
    private Condition readyCond;
    private Condition canvasRecvCond;
    private Condition signalCond;

    private AssaultParty[] aParties;
    private final GeneralRepos repos;

    private boolean[] emptyRooms;
    private boolean[] partyRunStatus;
    private MemFIFO<Boolean> canvasHandInQueue;
    private MemFIFO<Integer> roomHandInQueue;

    private int totalPaintings;
    private int nextParty;
    private int nextRoom;
    private int lastRoom;
    private int availableThieves;
    private int thiefSlots;
    private int queueSize;
    private boolean handIn;
    private boolean heistRun;


    public ControlCollectionSite(AssaultParty[] aParties, GeneralRepos repos, int roomNumber, int thiefMax)
            throws MemException {

        this.lock = new ReentrantLock();
        this.canvasCond = lock.newCondition();
        this.prepAssaultCond = lock.newCondition();
        this.readyCond = lock.newCondition();
        this.canvasRecvCond = lock.newCondition();
        this.signalCond = lock.newCondition();

        this.aParties = aParties;
        this.repos = repos;
        this.emptyRooms = new boolean[roomNumber];
        this.partyRunStatus = new boolean[aParties.length];

        try {
            //this.waitingQueue = new MemFIFO<oThief>(new oThief[thiefMax]);
            this.canvasHandInQueue = new MemFIFO<Boolean>(new Boolean[thiefMax]);
            this.roomHandInQueue = new MemFIFO<Integer>(new Integer[thiefMax]);      
        } catch (MemException e) {
        }

        this.heistRun = true;
        this.handIn = true;

        this.totalPaintings = 0;
        this.thiefSlots = 3;
        this.availableThieves = 0;
        this.nextParty = -1;
        this.lastRoom = -1;
        this.nextRoom = -1;
        this.queueSize = 0;
    }

    /* public int computeNextRoom() {
        lock.lock();
        int emptyCounter = 0;
        for (int i = 0; i < emptyRooms.length; i++) {
            if (!emptyRooms[i] && (i != lastRoom)) {
                lastRoom = i;
                lock.unlock();
                return i;
            }
            else{
                emptyCounter++;
            }
            if(emptyCounter == emptyRooms.length - 1){
                lock.unlock();
                return emptyRooms.length-1;
            }
        }
        lock.unlock();
        return -1;
    } */


    public int computeNextRoom(){
        lock.lock();
        for(int i = 0;i < emptyRooms.length; i++){
            if(!emptyRooms[i]){
                if(i != lastRoom){
                    lastRoom = i;
                    break;
                }   
            }
        }
        return lastRoom;
    }
    public void printRoomStatus(){
        for(boolean x: emptyRooms){
            System.out.println("Room status " + x);
        }
    }

    public int getNextRoom(){
        return nextRoom;
    }

    public boolean amINeeded() throws InterruptedException {
        lock.lock();
        availableThieves++;
        oThief curThread = (oThief) Thread.currentThread();
        if (!curThread.isFirstCycle()){
            repos.setOrdinaryThiefState(curThread.getThiefID(), oStates.CONCENTRATION_SITE);
            repos.setOrdinaryThiefPartyState(curThread.getThiefID(), 'W');
            repos.removeThiefFromAssaultParty(curThread.getThiefID(), curThread.getCurAP());
            curThread.setFirstCycle(false);
        }
        readyCond.signal();
        prepAssaultCond.await();
        if (heistRun) {
            if(thiefSlots >= 0){
                signalCond.signal();
                thiefSlots--;
                availableThieves--;
            }
            lock.unlock();
            return true;
        } else {
            prepAssaultCond.signal();
            lock.unlock();
            return false;
        }
    }

    public int prepareAssaultParty() throws InterruptedException {
        lock.lock();
        nextParty++;
        if(nextParty > 1){
            nextParty = 0;
        }
        repos.setMasterThiefState(mStates.ASSEMBLING_A_GROUP);
        nextRoom = this.computeNextRoom();
        thiefSlots = 3;
        if (nextRoom == -1) {
            return -1;
        }
        repos.setAssaultPartyRoom(nextParty, nextRoom+1);
        for (int i = 0; i < 3; i++) {
            prepAssaultCond.signal();
            if(thiefSlots >= 0){
                signalCond.await();
                lock.lock();
            }
        }
        //log state

        partyRunStatus[nextParty] = true;
        lock.unlock();
        return nextParty;
    }

    public boolean getHeistStatus() {
        return heistRun;
    }

    public void takeARest() throws InterruptedException {
        lock.lock();
        repos.setMasterThiefState(mStates.WAITING_FOR_GROUP_ARRIVAL);
        Thread.sleep(100);
        lock.unlock();
        return;
    }

    public void collectACanvas() throws InterruptedException {
        lock.lock();
        int roomRead;
        boolean canvasRead;
        boolean collect = true;
        while (collect) {
            try {
                roomRead = roomHandInQueue.read();
                canvasRead = canvasHandInQueue.read();
                queueSize--;
                if (canvasRead) {
                    totalPaintings++;
                    
                } else {
                    emptyRooms[roomRead] = true;
                }
                collect = false;
                handIn = true;
                break;
            } catch (MemException e) {
                canvasCond.signalAll();
                canvasRecvCond.await();
            }
        }
        repos.setMasterThiefState(mStates.DECIDING_WHAT_TO_DO);
        
        canvasCond.signalAll();
        lock.unlock();
    }

    public void handACanvas() throws MemException, InterruptedException {
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        queueSize++;
        while(!handIn){
            canvasRecvCond.signal();
            canvasCond.await();
        } 
        handIn = false;
        roomHandInQueue.write(curThread.getCurRoom());
        canvasHandInQueue.write(curThread.hasPainting());
        repos.setThiefCanvas(curThread.getCurAP(),curThread.getThiefID(), 0);
        canvasRecvCond.signal();
        canvasCond.await();
        repos.removeThiefFromAssaultParty(curThread.getThiefID(), curThread.getCurAP());
        lock.unlock();
    }

    public int appraiseSit() throws InterruptedException {
        lock.lock();
        System.out.println("appraiseSit");
        // log state
        int emptyCounterSit = 0;
        int returnValue;

        for (int i = 0; i < emptyRooms.length; i++) {
            if (emptyRooms[i]) {
                emptyCounterSit++;
            }
        }

        if (emptyCounterSit == emptyRooms.length) {
            //System.out.println("im in emptyCounter ==");
            if(queueSize != 0 ){
                lock.unlock();
                return 1;
            }
            while (availableThieves < 6) {
                readyCond.await();
            }
            this.heistRun = false;
            
            lock.unlock();
            return 2;
        }

        if(availableThieves >= 3){
            //System.out.println("enoughthieves");
            lock.unlock();
            return 0;
        }

        for(int i = 0; i < aParties.length;i++){
            if(partyRunStatus[i]){
                //System.out.println("im in partyrun");
                lock.unlock();
                return 1;
            }
        }
        if(availableThieves >= 3){
            lock.unlock();
            return 0;
        }

        while (availableThieves < 3) {
            //System.out.println("mt waiting in  appraise");
            readyCond.await();
        }

        //System.out.println("leaving appraiseSit");
        returnValue = 0;
        lock.unlock();
        return returnValue;
    }

    public void startOperations() {
        lock.lock();
        System.out.println("startOperations");
        mThief curThread = (mThief) Thread.currentThread();
        curThread.setState(mStates.DECIDING_WHAT_TO_DO);
        repos.setMasterThiefState(mStates.DECIDING_WHAT_TO_DO);
        lock.unlock();
        return;
    }

    public void sumUpResults() {
        lock.lock();
        System.out.println("sumResults");
        repos.setMasterThiefState(mStates.PRESENTING_THE_REPORT);
        repos.finalResult(this.totalPaintings);
        prepAssaultCond.signal();
        lock.unlock();
    }
}
