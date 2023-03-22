package sharedRegions;

import java.util.ResourceBundle.Control;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import entities.mThief;
import entities.oThief;
import infrastructure.MemException;

public class ConcentrationSite {

    private ReentrantLock lock;
    private Condition cond;
    private AssaultParty[] aParties;
    private ControlCollectionSite controlSite;
    private GeneralRepos repos;
    private int nextParty;


    ConcentrationSite(AssaultParty[] aParties, int totalNum) {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.aParties = aParties;
        this.nextParty = 0;
    }


    public boolean amINeeded(){
        lock.lock();
        cond.signal();
        if(!controlSite.getHeistStatus()){
            cond.signalAll();
            lock.unlock();
            return true;
        }
        else{
            lock.unlock();
            return false;
        }
    }

    public void sendAssaultParty() throws InterruptedException {
        lock.lock();
        while(!aParties[nextParty].isFull()){
            cond.await();
            lock.lock();
        }
        aParties[nextParty].setReady();
        nextParty++;
        lock.unlock();
        //log state
    }

    public void prepareAssaultParty() throws InterruptedException {
        lock.lock();
        int nextRoom = controlSite.getNextRoom();
        if(nextRoom != -1){
            if(nextParty <= 1){
                aParties[nextParty].setupParty(nextRoom);                
            }
        }
        for(int i = 0; i < 3; i++){
            cond.signal();
        }
        //log state
        cond.await();
    }

    public void prepareExcursion() throws InterruptedException, MemException {
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        aParties[nextParty].addThief(curThread);
        curThread.setAssaultParty(nextParty);
        if(aParties[nextParty].isFull()){
            cond.signal();
            lock.unlock();
            aParties[nextParty].waitForSend();
            return;
        }else{
            lock.unlock();
            aParties[nextParty].waitForSend();
            return;
        }
    }
}
