package clientSide.main;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import genclass.GenericIO;
import infrastructure.ExecParameters;
import interfaces.*;
import clientSide.entities.mClient;

public class ClientMasterThief {

    public static void main(String[] args) {

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

        mClient masterThief = new mClient(ap, cclStub, ccsStub, 6);

        masterThief.start();

        while (masterThief.isAlive()) {
            try {
                museumStub.shutdown();
            } catch (Exception e) {
                GenericIO.writelnString("Museum generator remote exception on shutdown: " + e.getMessage());
                System.exit(1);
            }

            try {
                cclStub.shutdown();
            } catch (Exception e) {
                GenericIO.writelnString("CCS generator remote exception on shutdown: " + e.getMessage());
                System.exit(1);
            }

            try {
                ccsStub.shutdown();
            } catch (Exception e) {
                GenericIO.writelnString("CCS generator remote exception on shutdown: " + e.getMessage());
                System.exit(1);
            }

            try {
                ap[0].shutdown();
            } catch (Exception e) {
                GenericIO.writelnString("AP0 generator remote exception on shutdown: " + e.getMessage());
                System.exit(1);
            }

            try {
                ap[1].shutdown();
            } catch (Exception e) {
                GenericIO.writelnString("AP1 generator remote exception on shutdown: " + e.getMessage());
                System.exit(1);
            }
            Thread.yield();

            try {
                masterThief.join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("The masterThief has terminated.");
        }
    }
}
