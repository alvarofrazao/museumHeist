package src.entities;

import src.sharedRegions.AssaultParty;
import src.sharedRegions.ConcentrationSite;
import src.sharedRegions.ControlCollectionSite;

public class mThief extends Thread {

    /**
     * Master Thief state variable
     */
    private int state;

    /**
     * Reference to the Assault Party array
     */
    private AssaultParty[] assaultParties;

    /**
     * Reference to the Collection and Control Site shared region
     */
    private ControlCollectionSite controlSite;

    /**
     * Reference to the Concentration Site shared region
     */
    private ConcentrationSite concentrationSite;

    /***
     * Master Thief Thread Instantiation
     * @param assaultParties Reference to the array containing all AssaultParty shared memory regions
     * @param controlSite Reference to the ControlCollectionSite shared memory region
     * @param concentSite Reference to the ConcentrationSite shared memory region
     */
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

    /**
     * Lifecycle of the Master Thief thread
     */
    @Override
    public void run() {
        controlSite.startOperations();
        boolean heistRun = true;
        int party;
        while (heistRun){
            try {
                switch(controlSite.appraiseSit()){
                    case 0:
                        party = controlSite.prepareAssaultParty();         
                        assaultParties[party].setupParty(controlSite.getNextRoom());
                        concentrationSite.sendAssaultParty();
                        assaultParties[party].signalDeparture();
                        break;
                    case 1:
                        controlSite.takeARest();
                        controlSite.collectACanvas();
                        break;
                    case 2:
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
    }
}
