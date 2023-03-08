package entities;

import java.util.Random;
import entities.oStates;
import sharedRegions.*;

public class oThief extends Thread {

    protected char Sit;// either waiting to join AP ('W') or in party ('P')

    protected int MD; // thief's maximum displacement

    protected int currentPosition;

    protected int carryingCanvas;

    protected int curAP;

    protected int state;

    protected AssaultParty[] arrayAP;

    protected ControlSite controlSite;

    protected ConcentrationSite concentSite;

    oThief(AssaultParty[] arrayAP, ControlSite controlSite, ConcentrationSite concentSite) {
        Random rand = new Random();
        this.MD = rand.nextInt(4) + 2;
        this.Sit = 'W';
        this.currentPosition = 99; // (?)
        this.carryingCanvas = 0;
        this.state = 0;
        this.arrayAP = arrayAP;
        this.controlSite = controlSite;
        this.concentSite = concentSite;
    }

    public int getCurPos() {
        return currentPosition;
    }

    public int hasPainting() {
        return carryingCanvas;
    }

    public char getCurSit() {
        return Sit;
    }

    public int getMD() {
        return MD;
    }



    @Override
    public void run() {
        try {
            while (true) {
                switch (state) {
                    case (oStates.CONCENTRATION_SITE): {
                        while(concentSite.amINeeded())
                        {
                            curAP = concentSite.prepareExcursion();
                        }
                        break;
                    }
                    case (oStates.CRAWLING_INWARDS): {
                        while(arrayAP[curAP].crawlIn());
                        
                        break;
                    }
                    case (oStates.AT_A_ROOM): {
                        break;
                    }
                    case (oStates.CRAWLING_OUTWARDS): {
                        break;
                    }
                    case (oStates.COLLECTION_SITE): {
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
