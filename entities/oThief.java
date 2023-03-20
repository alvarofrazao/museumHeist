package entities;

import java.util.Random;
import entities.oStates;
import sharedRegions.*;

import java.lang.Math;

public class oThief extends Thread {

    protected char Sit;// either waiting to join AP ('W') or in party ('P')

    protected int thiefID;

    protected int MD; // thief's maximum displacement

    protected int currentPosition;

    protected boolean carryingCanvas;

    protected int curAP;

    protected int state;

    protected AssaultParty[] arrayAP;

    protected ControlSite controlSite;

    protected ConcentrationSite concentSite;

    protected Museum museum;

    oThief(int thiefID, AssaultParty[] arrayAP, ControlSite controlSite, ConcentrationSite concentSite, Museum museum, int MAX_D,
            int MIN_D) {
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

    public int getMD() {
        return MD;
    }

    public char getCurSit() {
        return Sit;
    }

    public boolean hasPainting() {
        return carryingCanvas;
    }

    public void setCanvas(){
        carryingCanvas = false;
    }

    public void moveIn(int nextPos) {
        currentPosition += nextPos;
    }

    public void moveOut(int nextPos) {
        currentPosition -= nextPos;
    }

    public void setAssaultParty(int apNum){
        curAP = apNum;
    }

    public void setState(int newState) {
        state = newState;
    }

    public void setPos(int pos){
        currentPosition = pos;
    }

    @Override
    public void run()  {
        try {
            while(concentSite.amINeeded()){
                arrayAP[curAP].prepareExcursion();
                arrayAP[curAP].crawlIn();
                carryingCanvas = museum.rollACanvas(arrayAP[curAP].getRoomID()); //possivelmente esperar que todos cheguem?
                arrayAP[curAP].reverseDirection();
                arrayAP[curAP].crawlOut();
                controlSite.handACanvas();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
