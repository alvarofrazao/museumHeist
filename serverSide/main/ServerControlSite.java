package serverSide.main;

import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import genclass.GenericIO;
import infrastructure.ExecParameters;
import interfaces.CCLInterface;
import interfaces.GeneralReposInterface;
import interfaces.Register;
import serverSide.objects.ControlCollectionSite;

public class ServerControlSite {
    public static boolean runFlag;

    public static void main(String[] args) {

        int portNumb = ExecParameters.cclPortNum; // port number for listening to service requests
        String rmiRegHostName = ExecParameters.RegistryHostName; // name of the platform where is located the RMI
                                                                 // registering service
        int rmiRegPortNumb = ExecParameters.registryPortNum;
        String regObject = ExecParameters.nameEntryRegisterObject;
        runFlag = true;

        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        GenericIO.writelnString("Security manager was installed!");

        /* instantiate a registration remote object and generate a stub for it */

        Registry registry = null;
        Register reg = null;
        GeneralReposInterface gRepos = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            GenericIO.writelnString("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("RMI registry was created!");

        try {
            reg = (Register) registry.lookup(regObject);
        } catch (RemoteException e) {
            GenericIO.writelnString("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            gRepos = (GeneralReposInterface) registry.lookup(ExecParameters.nameEntryGeneralRepos);
        } catch (Exception e) {
            // TODO: handle exception
        }

        ControlCollectionSite ccl = new ControlCollectionSite(gRepos,5,6);

        CCLInterface cclStub = null;

        try {
            cclStub = (CCLInterface) UnicastRemoteObject.exportObject(ccl, portNumb);
        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            reg.bind(ExecParameters.nameEntryControlSite, cclStub);
        } catch (RemoteException e) {
            GenericIO.writelnString("ControlSite registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            GenericIO.writelnString("ControlSite already bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("ControlSite object was registered!");

        /* wait for the end of operations */

        GenericIO.writelnString("ControlSite is in operation!");

        try {
            while (runFlag)
                synchronized (Class.forName("serverSide.main.ServerControlSite")) {
                    try {
                        (Class.forName("serverSide.main.ServerControlSite")).wait();
                    } catch (InterruptedException e) {
                        GenericIO.writelnString("ControlSite main thread was interrupted!");
                    }
                }
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("The data type ServerControlSite was not found (blocking)!");
            e.printStackTrace();
            System.exit(1);
        }

        /* server shutdown */

        boolean shutdownDone = false; // flag signalling the shutdown of the general repository service

        try {
            reg.unbind(ExecParameters.nameEntryControlSite);
        } catch (RemoteException e) {
            GenericIO.writelnString("ControlSite deregistration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("ControlSite not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("ControlSite was deregistered!");

        try {
            shutdownDone = UnicastRemoteObject.unexportObject(ccl, true);
        } catch (NoSuchObjectException e) {
            GenericIO.writelnString("ControlSite unexport exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        if (shutdownDone)
            GenericIO.writelnString("ControlSite was shutdown!");
    }

    public static void shutdown() {
        runFlag = false;
        try {
            synchronized (Class.forName("serverSide.main.ServerControlSite")) {
                (Class.forName("serverSide.main.ServerControlSite")).notify();
            }
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("The data type ServerControlSite was not found (waking up)!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
