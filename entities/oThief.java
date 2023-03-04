package entities;

import java.util.Random;

public class oThief extends Thread{
    
    protected char Sit;// either waiting to join AP ('W') or in party ('P')

    protected int MD; //thief's maximum displacement

    protected int currentPosition;

    protected int carryingCanvas;

    protected int state;

    oThief()
    {
        
        Random rand = new Random();
        this.MD = rand.nextInt(4)+2;
        this.Sit = 'W'; 
        this.currentPosition = 99; //(?)
        this.carryingCanvas = 0;
        this.state = 0;
    }

    public int getCurPos()
    {
        return currentPosition;
    }

    public int hasPainting()
    {
        return carryingCanvas;
    }

    public char getCurSit()
    {
        return Sit;
    }

    public int getMD()
    {
        return MD;
    }

    public void run()
    {

    }
}
