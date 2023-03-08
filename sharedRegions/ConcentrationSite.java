package sharedRegions;

import java.util.concurrent.locks.ReentrantLock;

public class ConcentrationSite {

    private ReentrantLock lock;
    private AssaultParty[] aParties;

    ConcentrationSite(ReentrantLock lock, AssaultParty[] aParties) {
        this.lock = lock;
        this.aParties = aParties;
    }

    public int prepareExcursion() {
        int id;
        int i = 0;
        for (AssaultParty x : aParties) {
            if (!x.isFull()) {
                i++;
                break;
            }
        }
        id = i;
        return id;
    }

    public boolean amINeeded() {
        int i = 0;
        for (AssaultParty x : aParties) {
            if (!x.isFull()) {
                i++;
                break;
            }
        }
        if (i == aParties.length) {
            return false;
        } else
            return true;
    }
}
