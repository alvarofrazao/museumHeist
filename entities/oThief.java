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

    oThief(int thiefID, AssaultParty[] arrayAP, ControlSite controlSite, ConcentrationSite concentSite, int MAX_D,
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
    }

    public int getCurrentPosition() { // thief distance to assigned room, may not be reasonable to directly assign
        return currentPosition;
    }

    public void incrementPosition() {
        currentPosition++;
    }

    public int getThiefID() {
        return thiefID;
    }

    public void decrementPosition() {
        currentPosition--;
    }

    public boolean hasPainting() {
        return carryingCanvas;
    }

    public char getCurSit() {
        return Sit;
    }

    public int getMD() {
        return MD;
    }

    public void setState(int newState) {
        state = newState;
    }

    @Override
    public void run() {
        try {
            while (true) {
                switch (state) {
                    case (oStates.CONCENTRATION_SITE): {
                        while (concentSite.amINeeded()) {
                            curAP = concentSite.prepareExcursion();
                        }
                        break;
                    }
                    case (oStates.CRAWLING_INWARDS): {
                        while (arrayAP[curAP].crawlIn());
                        // crawlIn is both a blocking method and a
                        // state transition method
                        break;
                    }
                    case (oStates.AT_A_ROOM): {
                        while(arrayAP[curAP].crawlOut());
                        // call and block on rollACanvas
                        // reverseDirection is the state changing method
                        break;
                    }
                    case (oStates.CRAWLING_OUTWARDS): {
                        break;
                    }
                    case (oStates.COLLECTION_SITE): {
                        // call and block on handACanvas
                        // amINeeded call changes state
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
