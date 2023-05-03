package serverSide.entities;

import genclass.GenericIO;
import serverSide.infrastructure.Message;
import serverSide.infrastructure.MessageException;
import serverSide.infrastructure.ServerCom;
import serverSide.sharedRegions.ControlCollectionSiteInterface;

public class cclClientProxy extends Thread {

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
     private ControlCollectionSiteInterface cclInter;

     /**
      * thread id
      */
     private int thId;

     /**
      * thread state
      */
     private int thState;

     private int room;

     private int thAP;

     private char pstate;

     private boolean hasCanvas;

     private boolean fc;

     public cclClientProxy(ServerCom sconi, ControlCollectionSiteInterface cclInter) {
          super("cclClientProxy" + cclClientProxy.getProxyId());
          this.sconi = sconi;
          this.cclInter = cclInter;
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
               cl = Class.forName("serverSide.entities.cclClientProxy");
          } catch (ClassNotFoundException e) {
               GenericIO.writelnString("Data type cclClientProxy was not found!");
               e.printStackTrace();
               System.exit(1);
          }
          synchronized (cl) {
               proxyId = nProxy;
               nProxy += 1;
          }
          return proxyId;
     }

     public void setThId(int id) {
          thId = id;
     }

     public int getThId() {
          return thId;
     }

     public void setThState(int state) {
          thState = state;
     }

     public int getThState() {
          return thState;
     }

     public void setRoom(int room) {
          this.room = room;
     }

     public int getRoom() {
          return room;
     }

     public void setThAP(int ap) {
          this.thAP = ap;
     }

     public int getAP() {
          return thAP;
     }

     public void setPS(char pstate) {
          this.pstate = pstate;
     }

     public char getPS() {
          return pstate;
     }

     public void setFC(boolean fc) {
          this.fc = fc;
     }

     public boolean getFC() {
          return fc;
     }

     public void setHC(boolean hc) {
          this.hasCanvas = hc;
     }

     public boolean getHC() {
          return hasCanvas;
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
               outMessage = cclInter.processAndReply(inMessage); // process it
          } catch (MessageException e) {
               GenericIO.writelnString("Thread " + getThId() + ": " + e.getMessage() + "!");
               GenericIO.writelnString(e.getMessageVal().toString());
               System.exit(1);
          }
          sconi.writeObject(outMessage); // send service reply
          sconi.close(); // close the communication channel
     }

}
