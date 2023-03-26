package src.sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import src.entities.oThief;
import src.infrastructure.MemException;

public class ConcentrationSite {

    private ReentrantLock lock;
    //private Condition siteCond;
    //private Condition orgCond;
    private Condition partyRdyCond;
    //private AssaultParty[] aParties;
    //private ControlCollectionSite controlSite;
    private final GeneralRepos repos;
    private int nextParty;
    private int thiefCount;


    public ConcentrationSite(AssaultParty[] aParties, GeneralRepos repos) {
        this.lock = new ReentrantLock();
        //this.siteCond = lock.newCondition();
        //this.orgCond = lock.newCondition();
        this.partyRdyCond = lock.newCondition();
        this.repos = repos;
        //this.aParties = aParties;
        this.nextParty = 0;
        this.thiefCount = 0;
    }

    public void sendAssaultParty() throws InterruptedException { // se calhar faria mais sentido este metodo ser da party em si???
        lock.lock();
        //System.out.println("sendparty");
        //mThief curThread = (mThief) Thread.currentThread();
        //log state
        while(thiefCount < 3){
            //System.out.println("waiting rdy cond" + thiefCount  + " " + curThread.getId());
            partyRdyCond.await();
            // lock.lock();
        }
        nextParty++;
        if(nextParty > 1){
            nextParty = 0;
        }

        thiefCount = 0;
        //repos.setMasterThiefState(mStates.DECIDING_WHAT_TO_DO);
        lock.unlock();
    }

    

    public int prepareExcursion() throws InterruptedException, MemException {
        lock.lock();
        //System.out.println("prepexcursion");
        thiefCount++;
        //repos.setOrdinaryThiefPartyState(((oThief) Thread.currentThread()).getThiefID(), 'P');
        partyRdyCond.signal();
        lock.unlock();
        return nextParty;
    }
}
