package src.sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import src.entities.mStates;
import src.entities.oThief;
import src.infrastructure.MemException;
import src.entities.mStates;

public class ConcentrationSite {

    /**
     * Main monitor object of the class
     */
    private ReentrantLock lock;

    /**
     * Condition used for detecting if enough thieves have woken up to form a party
     */
    private Condition partyRdyCond;
    
    /**
     * Reference to the General Repository shared region
     */
    private final GeneralRepos repos;

    /**
     * Next party to be formed
     */
    private int nextParty;

    /**
     * Number of thieves currently woken up 
     */
    private int thiefCount;

    /***
     * Instantiation of ConcentrationSite object
     * @param aParties reference to an array containing references to both Assault Party shared memory regions
     * @param repos    reference to GeneralRepository shared memory region
     */

    public ConcentrationSite(AssaultParty[] aParties, GeneralRepos repos) {
        this.lock = new ReentrantLock();
        this.partyRdyCond = lock.newCondition();
        this.repos = repos;
        this.nextParty = 0;
        this.thiefCount = 0;
    }
    /***
     * Signals all Ordinary Thief threads waiting for the party to be sent and determines which party to form next
     * @throws InterruptedException
     */

    public void sendAssaultParty(){ 
        lock.lock();
        while(thiefCount < 3){
            try {
                partyRdyCond.await();   
            } catch (InterruptedException e) {
                System.out.println("Interrupted exception at mThief call of sendAssaultParty");
            }
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
     * Prepares the thief to be added to the current working Assault Party
     * @return aParties index of the assigned Assault Party
     */

    public int prepareExcursion(){
        lock.lock();
        thiefCount++;
        repos.setOrdinaryThiefPartyState(((oThief) Thread.currentThread()).getThiefID(), 'P');
        partyRdyCond.signal();
        lock.unlock();
        return nextParty;
    }
}
