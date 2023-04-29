package src.entities;


import src.stubs.*;

public class mClient extends Thread{

    /**
     * Master Thief state variable
     */
    private int state;

    /**
     * ID variable
     */
    private int id;

    /**
     * Reference to the Assault Party array
     */
    private AssaultPartyStub[] assaultParties;

    /**
     * Reference to the Collection and Control Site shared region
     */
    private ControlCollectionSiteStub controlSite;

    /**
     * Reference to the Concentration Site shared region
     */
    private ConcentrationSiteStub concentrationSite;
    
    public mClient(AssaultPartyStub[] assaultParties, ControlCollectionSiteStub controlSite, ConcentrationSiteStub concentrationSite, int id) {
        this.state = mStates.PLANNING_THE_HEIST;
        this.assaultParties = assaultParties;
        this.controlSite = controlSite;
        this.concentrationSite = concentrationSite;
        this.id = id;
    }

    public int getCurState() {
        return state;
    }

    public void setState(int i) {
        state = i;
    }

    public int getID(){
        return id;
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

        }
    }
}
