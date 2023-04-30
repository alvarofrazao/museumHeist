package src.sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import src.entities.*;
import src.infrastructure.MemException;
import src.stubs.GeneralReposStub;

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
    //private final GeneralRepos repos;

    /**
     * Stub of the general repository
     */
    private final GeneralReposStub grStub;

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
     * 
     * @param aParties reference to an array containing references to both Assault
     *                 Party shared memory regions
     * @param repos    reference to GeneralRepository shared memory region
     */

    public ConcentrationSite(AssaultParty[] aParties, GeneralReposStub grStub) {
        this.lock = new ReentrantLock();
        this.partyRdyCond = lock.newCondition();
        //this.repos = repos;
        this.grStub = grStub;
        this.nextParty = 0;
        this.thiefCount = 0;
    }

    /***
     * Signals all Ordinary Thief threads waiting for the party to be sent and
     * determines which party to form next
     * 

     */

    public void sendAssaultParty(){
        lock.lock();
        while (thiefCount < 3) {
            try {
                partyRdyCond.await();
            } catch (Exception e) {
                // TODO: handle exception
            }
            
        }
        nextParty++;
        if (nextParty > 1) {
            nextParty = 0;
        }

        thiefCount = 0;
        //repos.setMasterThiefState(mStates.DECIDING_WHAT_TO_DO);
        grStub.setMasterThiefState(((ccsClientProxy) Thread.currentThread()).getThId(),mStates.DECIDING_WHAT_TO_DO);
        lock.unlock();
    }

    /***
     * Prepares the thief to be added to the current working Assault Party
     * 
     * @return aParties index of the assigned Assault Party
     * @throws InterruptedException
     * @throws MemException
     */

    public int prepareExcursion(){
        lock.lock();
        thiefCount++;
        //repos.setOrdinaryThiefPartyState(((oThief) Thread.currentThread()).getThiefID(), 'P');
        grStub.setOrdinaryThiefPartyState(((ccsClientProxy) Thread.currentThread()).getThId(), 'P');
        partyRdyCond.signal();
        lock.unlock();
        return nextParty;
    }
}
