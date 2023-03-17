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
            while (controlSite.checkEmptyRooms()) {
            /* startOperations
             * prepareAssaultParty
             * sendAssaultParty
             * appraiseSit
             * takeARest
             * collectACanvas
             * appraiseSit
             */
            }
            //sumUpResults
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
