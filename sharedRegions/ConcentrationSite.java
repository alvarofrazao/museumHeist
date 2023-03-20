package sharedRegions;

import java.util.ResourceBundle.Control;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import entities.mThief;
import entities.oThief;

public class ConcentrationSite {

    private ReentrantLock lock;
    private Condition cond;
    private AssaultParty[] aParties;
    private ControlSite controlSite;
    private GeneralRepos repos;
    private int nextParty;


    ConcentrationSite(AssaultParty[] aParties, int totalNum) {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.aParties = aParties;
        this.nextParty = 0;
    }


    //ver melhor esta logica: nao estou a perceber muito bem como e que os blocks vao funcionar
    public void amINeeded() throws InterruptedException { 
        lock.lock();
        int i;
        oThief curThread = (oThief)Thread.currentThread();
        cond.signalAll();
        while(true){
            i = 0;
            for (AssaultParty x : aParties) {
                if (!x.isFull()) {
                    x.addThief(curThread);
                    curThread.setAssaultParty(i);
                    if(aParties[i].wasILast()){
                        cond.signalAll();
                        lock.unlock();
                    }
                    else{
                        lock.unlock();
                    }
                    return;                
                }
                else{
                    i++;
                }
            }
        }
        //lock.unlock();
    }


    public boolean amINeeded(){
        lock.lock();
        if(controlSite.getNextRoom() != -1){
            cond.signalAll();
            lock.unlock();
            return true;
        }
        else{
            lock.unlock();
            return false;
        }
    }

    public void sendAssaultParty() {
        lock.lock();
        aParties[nextParty].setReady();
        aParties[nextParty].signalThieves();
        nextParty++;
        lock.unlock();
        //log state
    }

    public void prepareAssaultParty() throws InterruptedException {
        lock.lock();
        mThief curThread = (mThief)Thread.currentThread();
        int nextRoom = controlSite.getNextRoom();
        if(nextRoom != -1){
            if(nextParty > 1){
                aParties[nextParty].setupParty(nextRoom);                
            }
        }
        cond.signalAll();
        cond.await();
    }
}
