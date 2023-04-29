package src.entities;

import genclass.GenericIO;
import src.infrastructure.ServerCom;
import src.sharedRegions.GeneralReposInterface;

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
      cl = Class.forName("src.entities.ccsClientProxy");
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
}
