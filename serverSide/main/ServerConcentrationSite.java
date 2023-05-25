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
import interfaces.GeneralReposInterface;
import interfaces.CCSInterface;
import interfaces.Register;
import serverSide.objects.ConcentrationSite;

public class ServerConcentrationSite {
    public static boolean runFlag;

  public static void main(String[] args) {
  
      int portNumb = ExecParameters.ccsPortNum; // port number for listening to service requests
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

      ConcentrationSite ccs = new ConcentrationSite(gRepos);

      CCSInterface ccsStub = null;

      try{
        ccsStub = (CCSInterface) UnicastRemoteObject.exportObject(ccs,portNumb);
      }catch (Exception e) {
        // TODO: handle exception
      }

      try {
        reg.bind(ExecParameters.nameEntryConcentrationSite, ccsStub);
      } catch (RemoteException e) {
        GenericIO.writelnString("ConcentrationSite registration exception: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
      } catch (AlreadyBoundException e) {
        GenericIO.writelnString("ConcentrationSite already bound exception: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
      }
      GenericIO.writelnString("ConcentrationSite object was registered!");
  
      /* wait for the end of operations */
  
      GenericIO.writelnString("ConcentrationSite is in operation!");
  
      try {
        while (runFlag)
          synchronized (Class.forName("serverSide.main.ServerConcentrationSite")) {
            try {
              (Class.forName("serverSide.main.ServerConcentrationSite")).wait();
            } catch (InterruptedException e) {
              GenericIO.writelnString("ConcentrationSite main thread was interrupted!");
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
        reg.unbind(ExecParameters.nameEntryConcentrationSite);
      } catch (RemoteException e) {
        GenericIO.writelnString("ConcentrationSite deregistration exception: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
      } catch (NotBoundException e) {
        GenericIO.writelnString("ConcentrationSite not bound exception: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
      }
      GenericIO.writelnString("ConcentrationSite was deregistered!");
  
      try {
        shutdownDone = UnicastRemoteObject.unexportObject(gRepos, true);
      } catch (NoSuchObjectException e) {
        GenericIO.writelnString("ConcentrationSite unexport exception: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
      }
  
      if (shutdownDone)
        GenericIO.writelnString("ConcentrationSite was shutdown!");
    }

  public static void shutdown() {
    runFlag = false;
    try {
      synchronized (Class.forName("serverSide.main.ServerConcentrationSite")) {
        (Class.forName("serverSide.main.ServerConcentrationSite")).notify();
      }
    } catch (ClassNotFoundException e) {
      GenericIO.writelnString("The data type ServerConcentrationSite was not found (waking up)!");
      e.printStackTrace();
      System.exit(1);
    }
  }
}
