package src.entities;

import src.stubs.AssaultPartyStub;
import src.stubs.ConcentrationSiteStub;
import src.stubs.ControlCollectionSiteStub;
import src.stubs.MuseumStub;

import java.lang.Math;

public class oClient extends Thread{
    
    /**
     * Reference to the Assault Party array
     */
    private AssaultPartyStub[] arrayAP;

    /**
     * Reference to the Collection and Control Site shared region
     */
    private ControlCollectionSiteStub controlSite;

    /**
     * Reference to the Concentration Site shared region
     */
    private ConcentrationSiteStub concentSite;

    /**
     * Reference to the Museum shared region
     */
    private MuseumStub museum;

    /**
     * Current situation of the thief thread: either 'W' for "waiting for a party" or 'P' for "currently in a party" 
     */
    private char Sit;

    /**
     * Index of the thief in the thiefArray
     */
    private int thiefID;

    /**
     * Maximum displacement of the thief thread
     */
    private int MD;

    /**
     * Current index of the thief in the party its assigned to
     */
    private int partyPos;

    /**
     * Flag for canvas posession
     */
    private boolean carryingCanvas;

    /**
     * Whether or not its the first lifecycle iteration of the thread
     */
    private boolean firstCycle;

    /**
     * Currently assigned party index
     */
    private int curAP;

    /**
     * Index of the room assigned to the current party
     */
    private int currentRoomID;

    /**
     * 
     */
    private int dist;

    /**
     * State variable
     */
    private int state;

    public oClient(int thiefID, AssaultPartyStub[] arrayAP, ControlCollectionSiteStub controlSite, ConcentrationSiteStub concentSite, MuseumStub museum, int MAX_D, int MIN_D) {
        this.thiefID = thiefID;
        this.MD = (int) ((Math.random() * (MAX_D - MIN_D)) + MIN_D);
        this.Sit = 'W';

        this.partyPos = -1;
        this.carryingCanvas = false;
        this.state = 0;
        this.dist = -1;
        this.arrayAP = arrayAP;
        this.controlSite = controlSite;
        this.concentSite = concentSite;
        this.museum = museum;
        this.firstCycle = true;
    }

    public int getPartyPos(){
        return partyPos;
    }

    public int getThiefID() {
        return thiefID;
    }

    public int getCurAP(){
        return curAP;
    }

    public int getCurRoom(){
        return currentRoomID;
    }

    public int getMD() {
        return MD;
    }

    public char getCurSit() {
        return Sit;
    }

    public boolean hasPainting() {
        return carryingCanvas;
    }

    public void setCanvas(boolean canvas){
        carryingCanvas = canvas;
    }

    public void setState(int newState) {
        state = newState;
    }

    public int getThiefState() {
        return state;
    }

    public void setPartyPos(int pos){
        partyPos = pos;
    }

    public boolean isFirstCycle() {
        return firstCycle;
    }

    public void setFirstCycle(boolean firstCycle) {
        this.firstCycle = firstCycle;
    }

     /**
     * Lifecycle of the Ordinary Thief thread
     */
    @Override
    public void run()  {
            while(controlSite.amINeeded()){
                curAP = concentSite.prepareExcursion();
                currentRoomID = arrayAP[curAP].addThief();
                dist = museum.getRoomDistance(currentRoomID);
                arrayAP[curAP].crawlIn(dist);
                carryingCanvas = museum.rollACanvas(currentRoomID);
                arrayAP[curAP].reverseDirection();
                arrayAP[curAP].crawlOut(dist);
                controlSite.handACanvas();
            }
    }
}
