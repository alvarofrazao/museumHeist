package serverSide.entities;

import genclass.GenericIO;
import serverSide.infrastructure.Message;
import serverSide.infrastructure.MessageException;
import serverSide.infrastructure.ServerCom;
import serverSide.sharedRegions.*;

public class ccsClientProxy extends Thread {

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
     private ConcentrationSiteInterface ccInter;

     /**
      * thread id
      */
     private int thId;

     /**
      * thread state
      */
     private int thState;

     public ccsClientProxy(ServerCom sconi, ConcentrationSiteInterface ccInter) {
          super("ccsClientProxy_" + ccsClientProxy.getProxyId());
          this.sconi = sconi;
          this.ccInter = ccInter;
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
      * Set Thread ID - >=6 is mThief, [0,5] is oThief
      * @param id
      */
     public void setThId(int id) {
          thId = id;
     }

     /**
      * Get Thread ID - >=6 is mThief, [0,5] is oThief
      * @return thread id
      */
     public int getThId() {
          return thId;
     }

     /**
      * Set Thread state
      * @param state
      */
     public void setThState(int state) {
          thState = state;
     }

     /**
      * Get Thread State
      * @return thread state
      */
     public int getThState() {
          return thState;
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
               outMessage = ccInter.processAndReply(inMessage); // process it
          } catch (MessageException e) {
               GenericIO.writelnString("Thread " + getThId() + ": " + e.getMessage() + "!");
               GenericIO.writelnString(e.getMessageVal().toString());
               System.exit(1);
          }
          sconi.writeObject(outMessage); // send service reply
          sconi.close(); // close the communication channel
     }

}
