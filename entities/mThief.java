package entities;

import sharedRegions.AssaultParty;
import sharedRegions.ConcentrationSite;
import sharedRegions.ControlCollectionSite;
import entities.mStates;

public class mThief extends Thread {

    private int state;

    private AssaultParty[] assaultParties;

    private ControlCollectionSite controlSite;

    private ConcentrationSite concentrationSite;

    mThief(AssaultParty[] assaultParties, ControlCollectionSite controlSite, ConcentrationSite concentrationSite) {
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
        while (heistRun){
            try {
                switch(controlSite.appraiseSit()){
                    case 0:
                        concentrationSite.prepareAssaultParty();
                        concentrationSite.sendAssaultParty();
                        break;
                    case 1:
                        controlSite.takeARest();
                        controlSite.collectACanvas();
                        break;
                    case 2:
                        controlSite.sumUpResults();
                        heistRun = false;
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
