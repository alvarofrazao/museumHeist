package sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import entities.mThief;
import entities.oThief;

public class ConcentrationSite {

    private ReentrantLock lock;
    private Condition cond;
    private AssaultParty[] aParties;
    private GeneralRepos repos;
    private int availableThieves;
    private int totalThieves;


    ConcentrationSite(AssaultParty[] aParties, int totalNum) {
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.aParties = aParties;
        this.totalThieves = totalNum;
        this.availableThieves = totalNum; 
    }

    public int getAvailableThieves(){
        return availableThieves;
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
                    availableThieves--;
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
}
