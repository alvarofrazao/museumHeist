package entities;

import sharedRegions.AssaultParty;
import sharedRegions.ControlSite;
import entities.mStates;

public class mThief extends Thread {

    protected int state;

    protected AssaultParty[] assaultParties;

    protected ControlSite controlSite;

    mThief(AssaultParty[] assaultParties, ControlSite controlSite) {
        this.state = mStates.PLANNING_THE_HEIST;
        this.assaultParties = assaultParties;
        this.controlSite = controlSite;
    }

    public int getCurState() {
        return state;
    }

    public void setState(int i) {
        state = i;
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
