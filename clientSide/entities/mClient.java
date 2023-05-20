package clientSide.entities;


import java.rmi.RemoteException;

import interfaces.*;
import genclass.*;

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
    private APInterface[] assaultParties;

    /**
     * Reference to the Collection and Control Site shared region
     */
    private CCLInterface controlSite;

    /**
     * Reference to the Concentration Site shared region
     */
    private CCSInterface concentrationSite;
    
    public mClient(APInterface[] assaultParties, CCLInterface controlSite, CCSInterface concentrationSite, int id) {
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
        startOperations();
        boolean heistRun = true;
        int party;
        int nextroom;
        while (heistRun){
                System.out.println("apsit");
                switch(appraiseSit()){
                    case 0:
                        System.out.println("prepap");
                        party = prepareAssaultParty();         
                        System.out.println("party: " + party);
                        System.out.println("setup-ap");
                        nextroom = getNextRoom();
                        setupParty(party,nextroom);
                        System.out.println("sendap");
                        sendAssaultParty();
                        System.out.println("signdep");
                        signalDeparture(party);
                        break;
                    case 1:
                        System.out.println("tkrst");
                        takeARest();
                        System.out.println("colcanv");
                        collectACanvas();
                        break;
                    case 2:
                        System.out.println("sumupres");
                        sumUpResults();
                        heistRun = false;
                        break;
                    default:
                        break;
                }

        }
    }

    private void startOperations(){
        try {
            controlSite.startOperations();
        } catch (RemoteException e) {
            GenericIO.writelnString("masterThief" + " remote exception on startOperations: " + e.getMessage());
            System.exit(1);
        }
    }

    private int appraiseSit(){

        ReturnInt ret = null;

        try {
            ret = controlSite.appraiseSit();
        } catch (RemoteException e) {
            GenericIO.writelnString("masterThief" + " remote exception on appraiseSit: " + e.getMessage());
            System.exit(1);
        }
        return ret.getIntVal();
    }

    private int prepareAssaultParty(){

        ReturnInt ret = null;

        try {
            ret = controlSite.prepareAssaultParty();
        } catch (RemoteException e) {
            GenericIO.writelnString("masterThief" + " remote exception on prepareAssaultParty: " + e.getMessage());
            System.exit(1);
        }
        return ret.getIntVal();
    }

    private void setupParty(int party,int roomId){
        try {
            assaultParties[party].setupParty(roomId);
        } catch (RemoteException e) {
            GenericIO.writelnString("masterThief" + " remote exception on setupParty: " + e.getMessage());
            System.exit(1);
        }
    }

    private void sendAssaultParty(){
        try {
            concentrationSite.sendAssaultParty();
        } catch (RemoteException e) {
            GenericIO.writelnString("masterThief" + " remote exception on sendAssaultParty: " + e.getMessage());
            System.exit(1);
        }
    }

    private void signalDeparture(int party){
        try {
            assaultParties[party].signalDeparture();
        } catch (RemoteException e) {
            GenericIO.writelnString("masterThief" + " remote exception on signalDeparture: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private void takeARest(){
        try {
            controlSite.takeARest();
        } catch (RemoteException e) {
            GenericIO.writelnString("masterThief" + " remote exception on takeARest: " + e.getMessage());
            System.exit(1);
        }
    }

    private void collectACanvas(){
        try {
            controlSite.collectACanvas();
        } catch (RemoteException e) {
            GenericIO.writelnString("masterThief" + " remote exception on collectACanvas: " + e.getMessage());
            System.exit(1);
        }
    }

    private void sumUpResults(){
        try {
            controlSite.sumUpResults();
        } catch (RemoteException e) {
            GenericIO.writelnString("masterThief" + " remote exception on sumUpResults: " + e.getMessage());
            System.exit(1);
        }
    }

    private int getNextRoom(){
        ReturnInt ret = null;

        try {
            ret = controlSite.getNextRoom();
        } catch (RemoteException e) {
            GenericIO.writelnString("masterThief" + " remote exception on getNextRoom: " + e.getMessage());
            System.exit(1);
        }
        return ret.getIntVal();
    }
}
