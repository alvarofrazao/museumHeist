package src.sharedRegions;

import java.util.ResourceBundle.Control;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import src.entities.mThief;
import src.entities.oThief;
import src.infrastructure.MemException;

public class ConcentrationSite {

    private ReentrantLock lock;
    private Condition siteCond;
    private Condition orgCond;
    private Condition partyRdyCond;
    private AssaultParty[] aParties;
    private ControlCollectionSite controlSite;
    private GeneralRepos repos;
    private int nextParty;
    private int thiefCount;


    public ConcentrationSite(AssaultParty[] aParties) {
        this.lock = new ReentrantLock();
        this.siteCond = lock.newCondition();
        this.orgCond = lock.newCondition();
        this.partyRdyCond = lock.newCondition();

        this.aParties = aParties;
        this.nextParty = 0;
        this.thiefCount = 0;
    }

    public void sendAssaultParty() throws InterruptedException { // se calhar faria mais sentido este metodo ser da party em si???
        lock.lock();
        System.out.println("sendparty");
        //log state
        if(thiefCount < 3){
            partyRdyCond.await();
        }
        
        if(nextParty > 1){
            nextParty = 0;
            nextParty++;
        }
        else{
            nextParty++;
        }
        thiefCount = 0;
        lock.unlock();
        aParties[nextParty].setReady();
    }

    

    public int prepareExcursion() throws InterruptedException, MemException {
        lock.lock();
        System.out.println("prepexcursion");
        thiefCount++;
        if(thiefCount >= 3){
            partyRdyCond.signal();
            lock.unlock();
            return nextParty;
        }else{
            lock.unlock();
            return nextParty;
        }
    }
}
