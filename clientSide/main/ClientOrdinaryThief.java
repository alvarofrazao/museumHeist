package clientSide.main;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import clientSide.entities.oClient;
import genclass.GenericIO;
import infrastructure.ExecParameters;
import interfaces.APInterface;
import interfaces.CCLInterface;
import interfaces.CCSInterface;
import interfaces.MuseumInterface;

public class ClientOrdinaryThief {
    
    public static void main(String[] args){
        String rmiRegHostName = ExecParameters.RegistryHostName;
        int rmiRegPortNumb = ExecParameters.registryPortNum;

        Registry registry = null;
        CCLInterface cclStub = null;
        CCSInterface ccsStub = null;
        MuseumInterface museumStub = null;
        APInterface[] ap = new APInterface[2];

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            GenericIO.writelnString("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            cclStub = (CCLInterface) registry.lookup(ExecParameters.nameEntryControlSite);
        } catch (RemoteException e) {
            GenericIO.writelnString("BarberShop lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("BarberShop not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            ccsStub = (CCSInterface) registry.lookup(ExecParameters.nameEntryConcentrationSite);
        } catch (RemoteException e) {
            GenericIO.writelnString("BarberShop lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("BarberShop not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            museumStub = (MuseumInterface) registry.lookup(ExecParameters.nameEntryMuseum);
        } catch (RemoteException e) {
            GenericIO.writelnString("BarberShop lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("BarberShop not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            ap[0] = (APInterface) registry.lookup(ExecParameters.nameEntryAssaultParty0);
        } catch (RemoteException e) {
            GenericIO.writelnString("BarberShop lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("BarberShop not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            ap[1] = (APInterface) registry.lookup(ExecParameters.nameEntryAssaultParty1);
        } catch (RemoteException e) {
            GenericIO.writelnString("BarberShop lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("BarberShop not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        oClient[] thieves = new oClient[6];

        for(int i = 0; i < 6; i++){
            thieves[i] = new oClient(i, ap, cclStub, ccsStub, museumStub, 6, 2);
        }

        for(int i = 0; i < 6; i++){
            thieves[i].start();
        }

        /* waiting for the end of the simulation */

      for (int i = 0; i < 6; i++)
      { try
        { thieves[i].join ();
        }
        catch (InterruptedException e) {}
        GenericIO.writelnString ("The thief " + (i+1) + " has terminated.");
      }
      GenericIO.writelnString ();

       
    }
}
