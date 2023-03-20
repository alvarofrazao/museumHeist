package entities;

import sharedRegions.AssaultParty;
import sharedRegions.ConcentrationSite;
import sharedRegions.ControlSite;
import entities.mStates;

public class mThief extends Thread {

    protected int state;

    protected AssaultParty[] assaultParties;

    protected ControlSite controlSite;

    protected ConcentrationSite concentrationSite;

    mThief(AssaultParty[] assaultParties, ControlSite controlSite, ConcentrationSite concentrationSite) {
        this.state = mStates.PLANNING_THE_HEIST;
        this.assaultParties = assaultParties;
        this.controlSite = controlSite;
        this.concentrationSite = concentrationSite;
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
        while (controlSite.getHeistStatus()){
            switch(controlSite.appraiseSit()){
                case 0:
                    con.prepareAssaultParty();
                    controlSite.sendAssaultParty();
                    break;
                case 1:
                    controlSite.takeARest();
                    controlSite.collectACanvas();
                    break;
                case 2:
                    controlSite.sumUpResults();
                    break;
            }
        }
        join();
    }
}
