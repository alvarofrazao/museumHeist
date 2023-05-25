package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;

import clientSide_msg.stubs.MuseumStub;
import serverSide.objects.*;
import interfaces.*;
import infrastructure.ExecParameters;
import genclass.GenericIO;

public class ServerMuseum {
  public static boolean runFlag;

  public static void main(String[] args) {
  
      int portNumb = ExecParameters.museumPortNum; // port number for listening to service requests
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

      Museum museum = new Museum(5, 30, 15, 16, 8,gRepos);

      MuseumInterface museumStub = null;

      try{
        museumStub = (MuseumInterface) UnicastRemoteObject.exportObject(museum,portNumb);
      }catch (Exception e) {
        // TODO: handle exception
      }

      try {
        reg.bind(ExecParameters.nameEntryMuseum, museumStub);
      } catch (RemoteException e) {
        GenericIO.writelnString("General Repository registration exception: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
      } catch (AlreadyBoundException e) {
        GenericIO.writelnString("General Repository already bound exception: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
      }
      GenericIO.writelnString("General Repository object was registered!");
  
      /* wait for the end of operations */
  
      GenericIO.writelnString("General Repository is in operation!");
  
      try {
        while (runFlag)
          synchronized (Class.forName("serverSide.main.ServerMuseum")) {
            try {
              (Class.forName("serverSide.main.ServerMuseum")).wait();
            } catch (InterruptedException e) {
              GenericIO.writelnString("Museum main thread was interrupted!");
            }
          }
      } catch (ClassNotFoundException e) {
        GenericIO.writelnString("The data type ServerMuseum was not found (blocking)!");
        e.printStackTrace();
        System.exit(1);
      }
  
      /* server shutdown */
  
      boolean shutdownDone = false; // flag signalling the shutdown of the general repository service
  
      try {
        reg.unbind(ExecParameters.nameEntryGeneralRepos);
      } catch (RemoteException e) {
        GenericIO.writelnString("General Repository deregistration exception: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
      } catch (NotBoundException e) {
        GenericIO.writelnString("General Repository not bound exception: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
      }
      GenericIO.writelnString("General Repository was deregistered!");
  
      try {
        shutdownDone = UnicastRemoteObject.unexportObject(gRepos, true);
      } catch (NoSuchObjectException e) {
        GenericIO.writelnString("General Repository unexport exception: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
      }
  
      if (shutdownDone)
        GenericIO.writelnString("General Repository was shutdown!");
    }

  public static void shutdown() {
    runFlag = false;
    try {
      synchronized (Class.forName("serverSide.main.ServerMuseum")) {
        (Class.forName("serverSide.main.ServerMuseum")).notify();
      }
    } catch (ClassNotFoundException e) {
      GenericIO.writelnString("The data type ServerMuseum was not found (waking up)!");
      e.printStackTrace();
      System.exit(1);
    }
  }
}