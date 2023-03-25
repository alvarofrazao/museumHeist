package src.sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import src.entities.mStates;
import src.entities.mThief;
import src.entities.oThief;
import src.infrastructure.*;

public class ControlCollectionSite {

    private ReentrantLock lock;
    private Condition cond;
    private Condition canvasCond;
    private Condition prepAssaultCond;
    private Condition readyCond;
    private Condition canvasRecvCond;
    private Condition signalCond;

    private AssaultParty[] aParties;
    private Museum museum;
    private GeneralRepos repos;

    private MemFIFO<oThief> waitingQueue;
    private boolean[] emptyRooms;
    private boolean[] partyRunStatus;
    private MemFIFO<Boolean> canvasHandInQueue;
    private MemFIFO<Integer> roomHandInQueue;

    private int totalPaintings;
    private int nextParty;
    private int nextRoom;
    private int lastRoom;
    private int availableThieves;
    private int waitingQueueSize;
    private int signalNum;
    private int thiefSlots;
    private boolean handIn;
    private boolean heistRun;


    public ControlCollectionSite(AssaultParty[] aParties, GeneralRepos repos, int roomNumber, int thiefMax)
            throws MemException {

        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
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
        this.waitingQueueSize = 0;
        this.nextParty = 0;
        this.lastRoom = -1;
        this.signalNum = 0;
    }

    public int computeNextRoom() {
        for (int i = 0; i < emptyRooms.length; i++) {
            if (!emptyRooms[i] && (i != lastRoom)) {
                lastRoom = i;
                return i;
            }
        }
        return -1;
    }

    public int getNextRoom(){
        return nextRoom;
    }

    public boolean amINeeded() throws InterruptedException {
        lock.lock();
        availableThieves++;
        oThief curThread = (oThief) Thread.currentThread();
        System.out.println("amINeeded " + curThread.getThiefID());
        // log state concentration site
        readyCond.signal();
        prepAssaultCond.await();
        if(thiefSlots >= 0){
            signalCond.signal();
            thiefSlots--;
            availableThieves--;
        }
        /* else{
            prepAssaultCond.await();
            signalCond.signal();
            thiefSlots--;
            availableThieves--;            
        } */
        //System.out.println("leaving amINeeded " + curThread.getThiefID());
        lock.unlock();
        if (heistRun) {
            return true;
        } else {
            return false;
        }
    }

    public int prepareAssaultParty() throws InterruptedException {
        lock.lock();
        //System.out.println("prepareAssaultParty");
        nextRoom = this.computeNextRoom();
        thiefSlots = 3;
        if (nextRoom == -1) {
            return -1;
        }
        for (int i = 0; i < 3; i++) {
          //  System.out.println("prep signal done");
            prepAssaultCond.signal();
            if(thiefSlots >= 0){
                signalCond.await();
                lock.lock();
            }
        }
        //log state
        partyRunStatus[nextParty] = true;
        lock.unlock();
        return nextParty++;
    }

    public boolean getHeistStatus() {
        return heistRun;
    }

    public void takeARest() throws InterruptedException {
        //lock.lock();
        System.out.println("takeARest");
        Thread.sleep(100);
        // log state
        //lock.unlock();
        return;
    }

    public void collectACanvas() throws InterruptedException {
        lock.lock();
        System.out.println("collectACanvas");
        int roomRead;
        boolean canvasRead;
        //outra opçao e fazer varias fifos para as varias informaçoes que tem que se armazenar (fifo para sala e fifo para bools)
        boolean collect = true;
        while (collect) {
            try {
                //lastThief = waitingQueue.read();
                roomRead = roomHandInQueue.read();
                canvasRead = canvasHandInQueue.read();
                System.out.println("non emtpy Q");
                if (canvasRead) {
                    totalPaintings++;
                } else {
                    emptyRooms[roomRead] = true;
                }
                collect = false;
                handIn = true;
                waitingQueueSize--;
                break;
            } catch (MemException e) {
                System.out.println("empty Q");
                canvasCond.signal();
                canvasRecvCond.await();
                lock.lock();
            }
        }
        // log state
        
        canvasCond.signal();
        // System.out.println("collectACanvas final signal");
        lock.unlock();
    }

    public void handACanvas() throws MemException, InterruptedException {
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        while(!handIn){
            System.out.println("waiting in hac " +curThread.getCurAP() + " " + curThread.getThiefID());
            canvasRecvCond.signal();
            canvasCond.await();
            lock.lock();
        } 
        handIn = false;
        System.out.println("handACanvas " + curThread.getCurAP() + " " + curThread.getThiefID());
        roomHandInQueue.write(curThread.getCurRoom());
        canvasHandInQueue.write(curThread.hasPainting());
        canvasRecvCond.signal();
        //waitingQueue.write(curThread);
        //waitingQueueSize++;
        canvasCond.await();
    }

    public int appraiseSit() throws InterruptedException {
        lock.lock();
        System.out.println("appraiseSit");
        // log state
        int emptyCounter = 0;
        int returnValue;

        for (int i = 0; i < emptyRooms.length; i++) {
            if (emptyRooms[i]) {
                emptyCounter++;
            }
        }

        if (emptyCounter == emptyRooms.length) {
            while (availableThieves < 6) {
                readyCond.await();
                lock.lock();
            }
            this.heistRun = false;
            lock.unlock();
            return 2;
        }

        if(availableThieves >= 3){
            lock.unlock();
            return 0;
        }

        for(int i = 0; i < aParties.length;i++){
            if(partyRunStatus[i]){
                lock.unlock();
                return 1;
            }
        }

        /* if (waitingQueueSize > 0) {
            lock.unlock();
            return 1;
        } */

        while (availableThieves < 3) {
            System.out.println("mt waiting in  appraise");
            readyCond.await();
            lock.lock();
        }
        System.out.println("leaving appraiseSit");
        returnValue = 0;
        lock.unlock();
        return returnValue;
    }

    public void startOperations() {
        lock.lock();
        System.out.println("startOperations");
        mThief curThread = (mThief) Thread.currentThread();
        curThread.setState(mStates.DECIDING_WHAT_TO_DO);
        lock.unlock();
        return;
    }

    public void sumUpResults() {
        lock.lock();
        System.out.println("sumResults");
        // log state
        mThief curThread = (mThief) Thread.currentThread();
        // print heist results
        cond.signalAll();
        lock.unlock();
        // to copy and paste into other methods requiring this action

    }
}
