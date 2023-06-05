package clientSide.entities;

import java.lang.Math;
import java.rmi.RemoteException;

import interfaces.*;
import genclass.*;

public class oClient extends Thread {

    /**
     * Reference to the Assault Party array
     */
    private final APInterface[] arrayAP;

    /**
     * Reference to the Collection and Control Site shared region
     */
    private final CCLInterface controlSite;

    /**
     * Reference to the Concentration Site shared region
     */
    private final CCSInterface concentSite;

    /**
     * Reference to the Museum shared region
     */
    private final MuseumInterface museum;

    /**
     * Current situation of the thief thread: either 'W' for "waiting for a party"
     * or 'P' for "currently in a party"
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

    public oClient(int thiefID, APInterface[] arrayAP, CCLInterface controlSite, CCSInterface concentSite,
            MuseumInterface museum, int MAX_D, int MIN_D) {
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

    public int getPartyPos() {
        return partyPos;
    }

    public int getThiefID() {
        return thiefID;
    }

    public int getCurAP() {
        return curAP;
    }

    public int getCurRoom() {
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

    public void setCanvas(boolean canvas) {
        carryingCanvas = canvas;
    }

    public void setState(int newState) {
        state = newState;
    }

    public int getThiefState() {
        return state;
    }

    public void setPartyPos(int pos) {
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
    public void run() {
        System.out.println(thiefID + " AIN");
        while (amINeeded()) {
            System.out.println(thiefID + " PREPEX");
            curAP = prepareExcursion();
            System.out.println(thiefID + " ADDTH");
            currentRoomID = addThief(curAP);
            System.out.println(thiefID + " GETRDIST");
            dist = getRoomDistance(currentRoomID);
            System.out.println(thiefID + " CRIN");
            crawlIn(dist);
            System.out.println(thiefID + " ROLLCAN");
            carryingCanvas = rollACanvas(currentRoomID);
            System.out.println(thiefID + " CRIN");
            reverseDirection();
            System.out.println(thiefID + " CROUT");
            crawlOut(dist);
            System.out.println(thiefID + " HNDCAN");
            handACanvas();
            System.out.println(thiefID + " AIN");
        }
    }

    /***
     * Controls the lifecycle of the Ordinary Thief threads
     * 
     * Remote operation
     * 
     * @return true or false, depending on the status of the heist
     */
    private boolean amINeeded() {

        ReturnBoolean ret = null; // return value

        try {
            ret = controlSite.amINeeded(this.thiefID, this.firstCycle, this.curAP);
        } catch (RemoteException e) {
            GenericIO.writelnString("Thief " + thiefID + " remote exception on amINeeded: " + e.getMessage());
            System.exit(1);
        }
        return ret.getBooleanVal();
    }

    /***
     * Prepares the thief to be added to the current working Assault Party
     * 
     * Remote operation
     * 
     * @return aParties index of the assigned Assault Party
     */
    private int prepareExcursion() {

        ReturnInt ret = null;

        try {
            ret = concentSite.prepareExcursion(this.thiefID);
        } catch (RemoteException e) {
            GenericIO.writelnString("Thief " + thiefID + " remote exception on prepareExcursion: " + e.getMessage());
            System.exit(1);
        }
        return ret.getIntVal();
    }

    /***
     * Assigns thief to party, and blocks waiting for departure signal.
     * 
     * Remote operation
     * 
     * @return Room index that was assigned to the party
     */
    private int addThief(int partyid) {

        ReturnInt ret = null;

        try {
            ret = arrayAP[partyid].addThief(this.thiefID);
        } catch (RemoteException e) {
            GenericIO.writelnString("Thief " + thiefID + " remote exception on addThief: " + e.getMessage());
            System.exit(1);
        }

        this.setPartyPos(ret.getIntStateVal());
        return ret.getIntVal();
    }

    /**
     * Returns the distance to a specific room
     * 
     * Remote operation
     * 
     * @param roomID room to get distance to
     * @return distance to chosen room
     * 
     */
    private int getRoomDistance(int roomId) {

        ReturnInt ret = null;

        try {
            ret = museum.getRoomDistance(roomId);
        } catch (RemoteException e) {
            GenericIO.writelnString("Thief " + thiefID + " remote exception on getRoomDistance: " + e.getMessage());
            System.exit(1);
        }
        return ret.getIntVal();
    }

    /***
     * Method for the ingoing movement, each thief moves until it is S units away
     * from the one behind them, then stops and signals another thief to move,
     * repeating the process until all three arrive at the Museum Room
     * 
     * Remote operation
     */
    private void crawlIn(int dist) {
        try {
            arrayAP[curAP].crawlIn(dist, this.thiefID, this.partyPos);
        } catch (RemoteException e) {
            GenericIO.writelnString("Thief " + thiefID + " remote exception on crawlIn: " + e.getMessage());
            System.exit(1);
        }
    }

    /***
     * Method for the outgoing movement, each thief moves until it is S units away
     * from the one behind them, then stops and signals another thief to move,
     * repeating the process until all three arrive at the Collection Site
     * 
     * Remote operation
     */
    private void crawlOut(int dist) {

        try {
            arrayAP[curAP].crawlOut(dist, this.thiefID, this.partyPos);
        } catch (RemoteException e) {
            GenericIO.writelnString("Thief " + thiefID + " remote exception on crawlOut: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Computes whether or not the thief successfully stole a canvas from the room
     * 
     * Remote operation
     * 
     * @param roomID
     * @return Whether or not the thief thread managed to get a canvas
     * 
     */
    private boolean rollACanvas(int room) {

        ReturnBoolean ret = null; // return value

        try {
            ret = museum.rollACanvas(room, this.thiefID, curAP);
        } catch (RemoteException e) {
            GenericIO.writelnString("Thief " + thiefID + " remote exception on rollACanvas: " + e.getMessage());
            System.exit(1);
        }
        return ret.getBooleanVal();
    }

    /***
     * Method for the handing in of canvases
     * 
     * Remote operation
     */
    private void handACanvas() {
        try {
            controlSite.handACanvas(this.thiefID, this.carryingCanvas, this.curAP, this.currentRoomID);
        } catch (RemoteException e) {
            GenericIO.writelnString("Thief " + thiefID + " remote exception on handACanvas: " + e.getMessage());
            System.exit(1);
        }
    }

    /***
     * Method that signals the start of the party's ingoing movement: the Master
     * signals a single thread to start moving
     * 
     * Remote operation
     */
    private void reverseDirection() {
        try {
            arrayAP[curAP].reverseDirection(this.thiefID, this.partyPos);
        } catch (RemoteException e) {
            GenericIO.writelnString("Thief " + thiefID + " remote exception on reverseDirection: " + e.getMessage());
            System.exit(1);
        }
    }
}
