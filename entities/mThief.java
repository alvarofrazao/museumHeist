package entities;

import sharedRegions.AssaultParty;
import entities.mStates;

public class mThief extends Thread {

    protected int state;

    protected AssaultParty ap;

    mThief() {
        this.state = 0;
    }

    public void changeCurState(int n) {
        this.state = n;
    }

    public int getCurState() {
        return state;
    }

    public void setMState(int i)
    {
        state = i;
    }

    public void setParty(AssaultParty party) {
        ap = party;
    }

    @Override
    public void run() {
        try {
            while (state != mStates.PRESENTING_THE_REPORT) {
                switch (state) {
                    case (mStates.PLANNING_THE_HEIST): {
                        break;
                    }
                    case (mStates.DECIDING_WHAT_TO_DO): {
                        break;
                    }
                    case (mStates.ASSEMBLING_A_GROUP): {
                        break;
                    }
                    case (mStates.WAITING_FOR_GROUP_ARRIVAL): {
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
