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
        controlSite.startOperations();
        boolean heistRun = true;
        while (heistRun){
            switch(controlSite.appraiseSit()){
                case 0:
                    controlSite.prepareAssaultParty();
                    controlSite.sendAssaultParty();
                    break;
                case 1:
                    controlSite.takeARest();
                    controlSite.collectACanvas();
                    break;
                case 2:
                    heistRun = false;
                    controlSite.sumUpResults();
                    break;
            }
        }
        join();
    }
}
