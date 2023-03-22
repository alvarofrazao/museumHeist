package entities;

import java.util.Random;
import entities.oStates;
import infrastructure.MemException;
import sharedRegions.*;

import java.lang.Math;

public class oThief extends Thread {

    private char Sit;// either waiting to join AP ('W') or in party ('P')

    private int thiefID;

    private int MD; // thief's maximum displacement

    private int currentPosition;

    private boolean carryingCanvas;

    private int curAP;

    private int currentRoomID;

    private int state;

    private AssaultParty[] arrayAP;

    private ControlCollectionSite controlSite;

    private ConcentrationSite concentSite;

    private Museum museum;

    oThief(int thiefID, AssaultParty[] arrayAP, ControlCollectionSite controlSite, ConcentrationSite concentSite, Museum museum, int MAX_D, int MIN_D) {
        this.thiefID = thiefID;
        this.MD = (int) ((Math.random() * (MAX_D - MIN_D)) + MIN_D);
        this.Sit = 'W';
        this.currentPosition = 0;
        this.carryingCanvas = false;
        this.state = 0;
        this.arrayAP = arrayAP;
        this.controlSite = controlSite;
        this.concentSite = concentSite;
        this.museum = museum;
    }

    

    public int getCurrentPosition() { // thief distance to assigned room, may not be reasonable to directly assign
        return currentPosition;
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

    public void moveIn(int nextPos) {
        currentPosition += nextPos;
    }

    public void moveOut(int nextPos) {
        currentPosition -= nextPos;
    }

    public void setState(int newState) {
        state = newState;
    }

    public void setPos(int pos){
        currentPosition = pos;
    }

    public void setInfo(int curParty,int roomID){
        currentRoomID = roomID;
        curAP = curParty;
    }

    @Override
    public void run()  {
        try {
            while(concentSite.amINeeded()){
                concentSite.prepareExcursion();
                //arrayAP[curAP].crawlIn();
                carryingCanvas = museum.rollACanvas(arrayAP[curAP].getRoomID()); //possivelmente esperar que todos cheguem?
                arrayAP[curAP].reverseDirection();
                //arrayAP[curAP].crawlOut();
                controlSite.handACanvas();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
