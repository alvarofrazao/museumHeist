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
import interfaces.APInterface;
import interfaces.GeneralReposInterface;
import interfaces.Register;
import serverSide.objects.AssaultParty;

public class ServerAssaultParty {
    
    public static boolean runFlag;

    public static void main(String[] args) {

        int portNumb = -1;
        String rmiRegHostName = ExecParameters.RegistryHostName; // name of the platform where is located the RMI
                                                                 // registering service
        int rmiRegPortNumb = ExecParameters.registryPortNum;
        String regObject = ExecParameters.nameEntryRegisterObject;
        runFlag = true;
        String name = null;

        int id = -1;

        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString ("args[0] is not a number!");
            System.exit (1);
        }

        if((id < 0) || (id >= 2)){
            GenericIO.writelnString ("args[0] is not a valid AssaultParty ID");
            System.exit(1);
        }

        if(id == 0){
            portNumb = ExecParameters.aP1PortNum;
            name = ExecParameters.nameEntryAssaultParty0;
        }
        else if(id == 1){
            portNumb = ExecParameters.aP2PortNum;
            name = ExecParameters.nameEntryAssaultParty1;
        }
        else{
            GenericIO.writelnString ("Failed to correctly assign port number and nameEntry");
            System.exit(1);
        }

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

        AssaultParty ap = new AssaultParty(id, 3,3,3, gRepos);

        APInterface apStub = null;

        try {
            apStub = (APInterface) UnicastRemoteObject.exportObject(ap, portNumb);
        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            reg.bind(name, apStub);
        } catch (RemoteException e) {
            GenericIO.writelnString("AssaultParty registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            GenericIO.writelnString("AssaultParty already bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("AssaultParty object was registered!");

        /* wait for the end of operations */

        GenericIO.writelnString("AssaultParty is in operation!");

        try {
            while (runFlag)
                synchronized (Class.forName("serverSide.main.ServerAssaultParty")) {
                    try {
                        (Class.forName("serverSide.main.ServerAssaultParty")).wait();
                    } catch (InterruptedException e) {
                        GenericIO.writelnString("AssaultParty main thread was interrupted!");
                    }
                }
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("The data type ServerAssaultParty was not found (blocking)!");
            e.printStackTrace();
            System.exit(1);
        }

        /* server shutdown */

        boolean shutdownDone = false; // flag signalling the shutdown of the general repository service

        try {
            reg.unbind(name);
        } catch (RemoteException e) {
            GenericIO.writelnString("AssaultParty deregistration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("AssaultParty not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("AssaultParty was deregistered!");

        try {
            shutdownDone = UnicastRemoteObject.unexportObject(ap, true);
        } catch (NoSuchObjectException e) {
            GenericIO.writelnString("AssaultParty unexport exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        if (shutdownDone)
            GenericIO.writelnString("AssaultParty was shutdown!");
    }

    public static void shutdown() {
        runFlag = false;
        try {
            synchronized (Class.forName("serverSide.main.ServerAssaultParty")) {
                (Class.forName("serverSide.main.ServerAssaultParty")).notify();
            }
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("The data type ServerAssaultParty was not found (waking up)!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
