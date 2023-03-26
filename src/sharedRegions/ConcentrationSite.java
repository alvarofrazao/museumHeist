package src.sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import src.entities.mStates;
import src.entities.oThief;
import src.infrastructure.MemException;
import src.entities.mStates;

public class ConcentrationSite {

    private ReentrantLock lock;

    private Condition partyRdyCond;

    private final GeneralRepos repos;
    private int nextParty;
    private int thiefCount;


    public ConcentrationSite(AssaultParty[] aParties, GeneralRepos repos) {
        this.lock = new ReentrantLock();
        this.partyRdyCond = lock.newCondition();
        this.repos = repos;
        this.nextParty = 0;
        this.thiefCount = 0;
    }

    public void sendAssaultParty() throws InterruptedException { 
        lock.lock();
        while(thiefCount < 3){
            partyRdyCond.await();
        }
        nextParty++;
        if(nextParty > 1){
            nextParty = 0;
        }

        thiefCount = 0;
        repos.setMasterThiefState(mStates.DECIDING_WHAT_TO_DO);
        lock.unlock();
    }

    /***
     * 
     * @return
     * @throws InterruptedException
     * @throws MemException
     */

    public int prepareExcursion() throws InterruptedException, MemException {
        lock.lock();
        thiefCount++;
        repos.setOrdinaryThiefPartyState(((oThief) Thread.currentThread()).getThiefID(), 'P');
        partyRdyCond.signal();
        lock.unlock();
        return nextParty;
    }
}
