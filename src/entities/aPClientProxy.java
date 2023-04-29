package src.entities;

import genclass.GenericIO;
import src.infrastructure.Message;
import src.infrastructure.MessageException;
import src.infrastructure.ServerCom;
import src.sharedRegions.AssaultPartyInterface;

public class aPClientProxy extends Thread{

    /**
   *  Number of instantiayed threads.
   */

   private static int nProxy = 0;

   /**
    *  Communication channel.
    */
 
    private ServerCom sconi;

    private AssaultPartyInterface apInter;

    private int thId;

    private int thState;

    private int mD;

    private int ppos;

    private int ap;

    public aPClientProxy(ServerCom sconi, AssaultPartyInterface apInter){
        super ("aPClientProxy" + aPClientProxy.getProxyId());
        this.sconi = sconi;
        this.apInter = apInter;
    }

     /**
   *  Generation of the instantiation identifier.
   *
   *     @return instantiation identifier
   */

   private static int getProxyId ()
   {
      Class<?> cl = null;                                            // representation of the BarberShopClientProxy object in JVM
      int proxyId;                                                   // instantiation identifier

      try
      { cl = Class.forName ("src.entities.aPClientProxy");
      }
      catch (ClassNotFoundException e)
      { GenericIO.writelnString ("Data type aPClientProxy was not found!");
        e.printStackTrace ();
        System.exit (1);
      }
      synchronized (cl)
      { proxyId = nProxy;
        nProxy += 1;
      }
      return proxyId;
   }

   public void setId(int id){
    this.thId = id;
   }

   public int getThId(){
    return thId;
   } 

   public void setState(int state){
    this.thState = state;
   }

   public int getThState(){
    return thState;
   }

   public void setMd(int md){
    this.mD = md;
   }

   public int getMd(){
    return mD;
   }

   public void setPpos(int ppos){
    this.ppos = ppos;
   }

   public int getPpos(){
    return ppos;
   }

   public void setAp(int ap){
    this.ap = ap;
   }

   public int getAp(){
    return ap;
   }


   @Override
   public void run ()
   {
      Message inMessage = null,                                      // service request
              outMessage = null;                                     // service reply

     /* service providing */

      inMessage = (Message) sconi.readObject ();                     // get service request
      try
      { outMessage = apInter.processAndReply (inMessage);         // process it
      }
      catch (MessageException e)
      { GenericIO.writelnString ("Thread " + getThId() + ": " + e.getMessage() + "!");
        GenericIO.writelnString (e.getMessageVal ().toString ());
        System.exit (1);
      }
      sconi.writeObject (outMessage);                                // send service reply
      sconi.close ();                                                // close the communication channel
   }
    
}
