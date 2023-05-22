package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import serverSide.objects.*;
import interfaces.*;
import infrastructure.ExecParameters;
import genclass.GenericIO;

/**
 *   Instantiation and registering of an object that enables the registration of other objects located
 *   in the same or other processing nodes of a parallel machine in the local RMI registry service.
 *
 *     Communication is based on Java RMI.
 */

public class ServerRegisterRemoteObject
{
  /**
   *  Main method.
   *
   *    @param args runtime arguments
   *        args[0] - port number for listening to service requests
   *        args[1] - name of the platform where is located the RMI registering service
   *        args[2] - port nunber where the registering service is listening to service requests
   */

   public static void main(String[] args)
   {
      int portNumb = ExecParameters.registerPortNum;                                             // port number for listening to service requests
      String rmiRegHostName = ExecParameters.RegistryHostName;                                         // name of the platform where is located the RMI registering service
      int rmiRegPortNumb = ExecParameters.registryPortNum;                                       // port number where the registering service is listening to service requests

     /* create and install the security manager */

      if (System.getSecurityManager () == null)
         System.setSecurityManager (new SecurityManager ());
      GenericIO.writelnString ("Security manager was installed!");

     /* instantiate a registration remote object and generate a stub for it */

      RegisterRemoteObject regEngine = new RegisterRemoteObject (rmiRegHostName, rmiRegPortNumb);  // object that enables the registration
                                                                                                   // of other remote objects
      Register regEngineStub = null;                                                               // remote reference to it

      try
      { regEngineStub = (Register) UnicastRemoteObject.exportObject (regEngine, portNumb);
      }
      catch (RemoteException e)
      { GenericIO.writelnString ("RegisterRemoteObject stub generation exception: " + e.getMessage ());
        System.exit (1);
      }
      GenericIO.writelnString ("Stub was generated!");

     /* register it with the local registry service */

      String nameEntry = ExecParameters.nameEntryRegisterObject;                          // public name of the remote object that enables
                                                                     // the registration of other remote objects
      Registry registry = null;                                      // remote reference for registration in the RMI registry service

      try
      { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
      }
      catch (RemoteException e)
      { GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
        System.exit (1);
      }
      GenericIO.writelnString ("RMI registry was created!");

      try
      { registry.rebind (nameEntry, regEngineStub);
      }
      catch (RemoteException e)
      { GenericIO.writelnString ("RegisterRemoteObject remote exception on registration: " + e.getMessage ());
        System.exit (1);
      }
      GenericIO.writelnString ("RegisterRemoteObject object was registered!");
   }
}