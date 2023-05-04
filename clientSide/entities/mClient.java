package clientSide.entities;


import clientSide.stubs.*;

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
                        System.out.println("prepap");
                        party = controlSite.prepareAssaultParty();         
                        System.out.println("setup-ap");
                        assaultParties[party].setupParty(controlSite.getNextRoom());
                        System.out.println("sendap");
                        concentrationSite.sendAssaultParty();
                        System.out.println("signdep");
                        assaultParties[party].signalDeparture();
                        break;
                    case 1:
                        System.out.println("tkrst");
                        controlSite.takeARest();
                        System.out.println("colcanv");
                        controlSite.collectACanvas();
                        break;
                    case 2:
                        System.out.println("sumupres");
                        controlSite.sumUpResults();
                        heistRun = false;
                        break;
                    default:
                        break;
                }

        }
    }
}
