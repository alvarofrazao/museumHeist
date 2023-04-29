package src.entities;

import genclass.GenericIO;
import src.infrastructure.Message;
import src.infrastructure.MessageException;
import src.infrastructure.ServerCom;
import src.sharedRegions.MuseumInterface;

public class museumClientProxy extends Thread {

  /**
   * Number of instantiayed threads.
   */

  private static int nProxy = 0;

  /**
   * Communication channel.
   */

  private ServerCom sconi;

  private MuseumInterface mInter;

  private int id;

  private int state;

  private int ap;

  private int room;

  public museumClientProxy(ServerCom sconi, MuseumInterface mInter) {
    super("museumClientProxy" + museumClientProxy.getProxyId());
    this.sconi = sconi;
    this.mInter = mInter;
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
      cl = Class.forName("src.entities.museumClientProxy");
    } catch (ClassNotFoundException e) {
      GenericIO.writelnString("Data type museumClientProxy was not found!");
      e.printStackTrace();
      System.exit(1);
    }
    synchronized (cl) {
      proxyId = nProxy;
      nProxy += 1;
    }
    return proxyId;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getThId() {
    return id;
  }

  public void setState(int state) {
    this.state = state;
  }

  public int getThState() {
    return state;
  }

  public void setAp(int ap) {
    this.ap = ap;
  }

  public int getAp() {
    return ap;
  }

  public void setRoom(int room) {
    this.room = room;
  }

  public int getRoom() {
    return room;
  }

  @Override
  public void run() {
    Message inMessage = null, // service request
        outMessage = null; // service reply

    /* service providing */

    inMessage = (Message) sconi.readObject(); // get service request
    try {
      outMessage = mInter.processAndReply(inMessage); // process it
    } catch (MessageException e) {
      GenericIO.writelnString("Thread " + getId() + ": " + e.getMessage() + "!");
      GenericIO.writelnString(e.getMessageVal().toString());
      System.exit(1);
    }
    sconi.writeObject(outMessage); // send service reply
    sconi.close(); // close the communication channel
  }

}
