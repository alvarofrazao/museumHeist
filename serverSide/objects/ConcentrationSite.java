package serverSide.objects;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import infrastructure.MemException;
import interfaces.*;
import serverSide.entities.*;
import serverSide.main.ServerConcentrationSite;

public class ConcentrationSite implements CCSInterface{

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
    // private final GeneralRepos repos;

    /**
     * Stub of the general repository
     */
    private final GeneralReposInterface grStub;

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

    public ConcentrationSite(GeneralReposInterface grStub) {
        this.lock = new ReentrantLock();
        this.partyRdyCond = lock.newCondition();
        // this.repos = repos;
        this.grStub = grStub;
        this.nextParty = 0;
        this.thiefCount = 0;
    }

    /***
     * Signals all Ordinary Thief threads waiting for the party to be sent and
     * determines which party to form next
     * 
     * 
     */

    public void sendAssaultParty() {
        lock.lock();
        while (thiefCount < 3) {
            try {
                partyRdyCond.await();
            } catch (Exception e) {
            }

        }
        nextParty++;
        if (nextParty > 1) {
            nextParty = 0;
        }

        thiefCount = 0;
        try {
            grStub.setMasterThiefState(mStates.DECIDING_WHAT_TO_DO);
        } catch (Exception e) {
        }
        lock.unlock();
    }

    /***
     * Prepares the thief to be added to the current working Assault Party
     * 
     * @return aParties index of the assigned Assault Party
     * @throws InterruptedException
     * @throws MemException
     */

    public ReturnInt prepareExcursion(int thid) {
        try {
            ReturnInt ret;
            lock.lock();
            thiefCount++;
            try {
                grStub.setOrdinaryThiefPartyState(thid, 'P');    
            } catch (Exception e) {
            }
            partyRdyCond.signal();
            ret = new ReturnInt(nextParty, 99);
            return ret;
        } finally {
            lock.unlock();
        }

    }

    public void shutdown() {
        ServerConcentrationSite.shutdown();
    }
}
