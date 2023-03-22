package sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import entities.oThief;
import entities.mStates;
import entities.mThief;

import infrastructure.*;

public class ControlCollectionSite { 

    private ReentrantLock lock;
    private Condition cond;
    private AssaultParty[] aParties;
    private Museum museum;
    private GeneralRepos repos;
    private boolean[] emptyRooms;
    private int totalPaintings;
    private int availableThieves;
    private MemFIFO<oThief> waitingQueue;
    private int waitingQueueSize;
    private boolean heistRun;


    public ControlCollectionSite(AssaultParty[] aParties, GeneralRepos repos, int roomNumber,int thiefMax) throws MemException {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.aParties = aParties;
        this.repos = repos;
        this.emptyRooms = new boolean[roomNumber];
        this.totalPaintings = 0;
        this.availableThieves = thiefMax;
        this.waitingQueue = new MemFIFO<oThief>(new oThief[thiefMax]);
        this.waitingQueueSize = 0;
        this.heistRun = true;
    }

    public int getNextRoom(){
        for(int i = 0; i < emptyRooms.length;i++){
            if(!emptyRooms[i]){
                return i;
            }
        }
        return -1;
    }

    public boolean getHeistStatus(){
        return heistRun;
    }

    public void takeARest() throws InterruptedException {
        lock.lock();
        Thread.sleep(100);
        //log state
        lock.unlock();
        return;
    }

    public void collectACanvas() throws InterruptedException{
        lock.lock();
        oThief lastThief;
        boolean collect = true;
        while(collect){
            try {
                lastThief = waitingQueue.read();
                if(lastThief.hasPainting()){
                    totalPaintings++;
                }
                else{
                    emptyRooms[lastThief.getCurRoom()] = true;
                }
                collect = false;
                waitingQueueSize--;
                break;
            } catch (MemException e) {
                cond.await();
                lock.lock();
            }
        }
        //log state
        cond.signal();
        lock.unlock();
    }

    public void handACanvas() throws MemException, InterruptedException {
        lock.lock();
        oThief curThread = (oThief)Thread.currentThread();
        waitingQueue.write(curThread);
        waitingQueueSize++;
        cond.signal();
        cond.await();
        lock.lock();
        availableThieves++;
        lock.unlock();
    }

    public int appraiseSit() throws InterruptedException {
        lock.lock();
        //log state
        int emptyCounter = 0;
        int returnValue = 1;
        int runCounter = 0;
        for(int i = 0; i < emptyRooms.length;i++){
            if(emptyRooms[i])
            {
                emptyCounter++;
            }
        }

        if(emptyCounter == emptyRooms.length)
        {
            while(availableThieves < 6){
                cond.await();
                lock.lock();
            }
            lock.unlock();
            return 2;
        }

        for(AssaultParty a: aParties){
            if(a.getStatus()){
                runCounter++;
            }
        }

        if(runCounter >= 1)
        {
            if(waitingQueueSize > 0){
                lock.unlock();
                return 1;
            } 
        }

        while(availableThieves < 3)
        {
            cond.await();
            lock.lock();
        }
        returnValue = 0;
        lock.unlock();
        return returnValue;
    }

    public void startOperations() {
        lock.lock();
        mThief curThread = (mThief)Thread.currentThread();
        curThread.setState(mStates.DECIDING_WHAT_TO_DO);
        lock.unlock();
        return;
    }

    public void sumUpResults() {
        lock.lock();
        //log state
        mThief curThread = (mThief)Thread.currentThread();
        //print heist results
        cond.signalAll();
        lock.unlock();
        // to copy and paste into other methods requiring this action

    }
}

