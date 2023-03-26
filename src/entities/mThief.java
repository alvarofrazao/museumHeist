package src.entities;

import src.entities.mStates;
import src.sharedRegions.AssaultParty;
import src.sharedRegions.ConcentrationSite;
import src.sharedRegions.ControlCollectionSite;

public class mThief extends Thread {

    private int state;

    private AssaultParty[] assaultParties;

    private ControlCollectionSite controlSite;

    private ConcentrationSite concentrationSite;

    public mThief(AssaultParty[] assaultParties, ControlCollectionSite controlSite, ConcentrationSite concentrationSite) {
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
        int party;
        while (heistRun){
            try {
                switch(controlSite.appraiseSit()){
                    case 0:
                        System.out.println("case 0 - assembling a group ");
                        party = controlSite.prepareAssaultParty();
                        if(party == -1){
                            controlSite.printRoomStatus();
                            break;
                        }
                        System.out.println("party chosen " + party + " heist status " + controlSite.getHeistStatus());
                       
                        assaultParties[party].setupParty(controlSite.getNextRoom());
                        concentrationSite.sendAssaultParty();
                        assaultParties[party].signalDeparture();
                        break;
                    case 1:
                        //System.out.println("case 1 - waiting for arrival ");
                        controlSite.takeARest();
                        controlSite.collectACanvas();
                        break;
                    case 2:
                        //System.out.println("case 2 - sumresults ");
                        controlSite.sumUpResults();
                        heistRun = false;
                        break;
                    default:
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
