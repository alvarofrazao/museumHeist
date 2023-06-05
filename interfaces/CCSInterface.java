package interfaces;

import java.rmi.*;

public interface CCSInterface extends Remote {

    /***
     * Signals all Ordinary Thief threads waiting for the party to be sent and
     * determines which party to form next
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     * 
     */
    public void sendAssaultParty() throws RemoteException;

    /***
     * Prepares the thief to be added to the current working Assault Party
     * 
     * @return aParties index of the assigned Assault Party
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public ReturnInt prepareExcursion(int thid) throws RemoteException;

    /**
     * Operation server shutdown.
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void shutdown() throws RemoteException;
}
