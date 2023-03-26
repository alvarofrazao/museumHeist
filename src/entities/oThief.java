package src.entities;

import src.infrastructure.MemException;
import src.sharedRegions.*;

import java.lang.Math;

public class oThief extends Thread {

    private char Sit;// either waiting to join AP ('W') or in party ('P')

    private int thiefID;

    private int MD; // thief's maximum displacement

    //private int currentPosition;
    private int partyPos;

    private boolean carryingCanvas;

    private boolean firstCycle;

    private int curAP;

    private int currentRoomID;

    private int state;

    private AssaultParty[] arrayAP;

    private ControlCollectionSite controlSite;

    private ConcentrationSite concentSite;

    private Museum museum;

    public oThief(int thiefID, AssaultParty[] arrayAP, ControlCollectionSite controlSite, ConcentrationSite concentSite, Museum museum, int MAX_D, int MIN_D) {
        this.thiefID = thiefID;
        this.MD = (int) ((Math.random() * (MAX_D - MIN_D)) + MIN_D);
        this.Sit = 'W';
        //this.currentPosition = 0;
        this.partyPos = -1;
        this.carryingCanvas = false;
        this.state = 0;
        this.arrayAP = arrayAP;
        this.controlSite = controlSite;
        this.concentSite = concentSite;
        this.museum = museum;
        this.firstCycle = true;
    }

    

    /* public int getCurrentPosition() { // thief distance to assigned room, may not be reasonable to directly assign
        return currentPosition;
    } */

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

    /* public void moveIn(int nextPos) {
        currentPosition += nextPos;
    }

    public void moveOut(int nextPos) {
        currentPosition -= nextPos;
    }
 */
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


    @Override
    public void run()  {
        try {
            while(controlSite.amINeeded()){
                curAP = concentSite.prepareExcursion();
                // System.out.println("thief curAP " + curAP+" "+thiefID);
                currentRoomID = arrayAP[curAP].addThief();
                arrayAP[curAP].crawlIn();
                carryingCanvas = museum.rollACanvas(currentRoomID); //possivelmente esperar que todos cheguem?
                arrayAP[curAP].reverseDirection();
                arrayAP[curAP].crawlOut();
                controlSite.handACanvas();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemException e) {
            e.printStackTrace();
        }
    }
}