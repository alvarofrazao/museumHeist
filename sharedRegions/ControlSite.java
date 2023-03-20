package sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import entities.oThief;
import entities.mStates;
import entities.mThief;

public class ControlSite { // this shared region houses both what is dubbed the Control Site
                           // as well as the Collection Site

    private ReentrantLock lock;
    private Condition cond;
    //do these variables make sense? check later
    private AssaultParty[] aParties;
    private Museum museum;
    private GeneralRepos repos;
    private boolean[] emptyRooms;
    private int totalPaintings;
    private int availableThieves;
    private boolean heistRun;
    private int nextParty;


    public ControlSite(AssaultParty[] aParties, GeneralRepos repos, int roomNumber,int thiefMax) {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.aParties = aParties;
        this.repos = repos;
        this.emptyRooms = new boolean[roomNumber];
        this.totalPaintings = 0;
        this.availableThieves = thiefMax;
        this.heistRun = true;
        this.nextParty = 0;
    }

    public boolean checkEmptyRooms(){
        int emptyCounter = 0;
        for(boolean x: emptyRooms){
            if(x){
                emptyCounter++;
            }
        }
        if (emptyCounter == emptyRooms.length){
            return true;
        }
        else{
            return false;
        }
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
        mThief.sleep(); //sleep a random amount of time
        lock.unlock();
    }

    public void collectACanvas() {
        lock.lock();

    }

    public void handACanvas() throws InterruptedException {
        lock.lock();
        oThief curThread = (oThief)Thread.currentThread();
        if(curThread.hasPainting()){
            totalPaintings++;
            curThread.setCanvas();
        }
        else{
            emptyRooms[aParties[curThread.getCurAP()].getRoomID()] = false;
        }
        cond.signalAll();
        cond.await();
        curThread.setCanvas();
    }

    public int appraiseSit() {
        lock.lock();
        boolean prepParty = true;
        int returnvalue;
        if(checkEmptyRooms() && (availableThieves == 6) ){
            heistRun = false;
            return 2;
        }else{
            if(availableThieves >= 3){
                prepParty =  true;
            }
            else{
                prepParty =  false;
            }
        }
        if(prepParty){
            returnvalue = 0;
        }
        else{
            returnvalue = 1;
        }

        return returnvalue;
    }

    public void startOperations() {
        lock.lock();
        mThief curThread = (mThief)Thread.currentThread();
        curThread.setState(mStates.DECIDING_WHAT_TO_DO);
        //log state
        return;
    }

    public void sumUpResults() {
        // mThief curThread = (mThief)Thread.currentThread();
        //log final master state
        //log end of operations
        //signal all threads to be joined

    }
}