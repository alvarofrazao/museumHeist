package serverSide.entities;

import genclass.GenericIO;
import infrastructure.Message;
import infrastructure.MessageException;
import infrastructure.ServerCom;
import serverSide.sharedRegions.GeneralReposInterface;

public class GeneralReposClientProxy extends Thread {

  /**
   * Number of instantiayed threads.
   */

  private static int nProxy = 0;

  /**
   * Communication channel.
   */

  private ServerCom sconi;

  /**
   * Control Site interface
   */
  private GeneralReposInterface gInter;

  /**
   * thread id
   */
  private int thId;

  /**
   * thread state
   */
  private int thState;

  public GeneralReposClientProxy(ServerCom sconi, GeneralReposInterface gInter) {
    super("GeneralReposClientProxy" + GeneralReposClientProxy.getProxyId());
    this.sconi = sconi;
    this.gInter = gInter;
  }

  /**
   * Generation of the instantiation identifier.
   *
   * @return instantiation identifier
   */

  private static int getProxyId() {
    Class<?> cl = null; // representation of the BarberShopClientProxy object in JVM
    int proxyId; // instantiation identifier

    try {
      cl = Class.forName("serverSide.entities.ccsClientProxy");
    } catch (ClassNotFoundException e) {
      GenericIO.writelnString("Data type ccsClientProxy was not found!");
      e.printStackTrace();
      System.exit(1);
    }
    synchronized (cl) {
      proxyId = nProxy;
      nProxy += 1;
    }
    return proxyId;
  }

   /**
      * Life cycle of the service provider agent.
      */

      @Override
      public void run() {
           Message inMessage = null, // service request
                     outMessage = null; // service reply
 
           /* service providing */
 
           inMessage = (Message) sconi.readObject(); // get service request
           try {
                outMessage = gInter.processAndReply(inMessage); // process it
           } catch (MessageException e) {
                GenericIO.writelnString("Thread " + currentThread().getId() + ": " + e.getMessage() + "!");
                GenericIO.writelnString(e.getMessageVal().toString());
                System.exit(1);
           }
           sconi.writeObject(outMessage); // send service reply
           sconi.close(); // close the communication channel
      }
}
