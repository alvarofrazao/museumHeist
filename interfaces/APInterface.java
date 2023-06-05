package interfaces;

import java.rmi.*;

public interface APInterface extends Remote {

    public ReturnBoolean getStatus() throws RemoteException;

    public ReturnInt getRoomID() throws RemoteException;

    /***
     * Setups up AssaultParty variables for party formation
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     * @param roomID index of the room assigned to the party
     */
    public void setupParty(int roomID) throws RemoteException;

    /***
     * Assigns thief to party, and blocks waiting for departure signal.
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     * @return Room index that was assigned to the party
     */
    public ReturnInt addThief(int thid) throws RemoteException;

    /***
     * Method for the ingoing movement, each thief moves until it is S units away
     * from the one behind them, then stops and signals another thief to move,
     * repeating the process until all three arrive at the Museum Room
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void crawlIn(int dist, int thid, int ppos) throws RemoteException;

    /***
     * Method for the outgoing movement, each thief moves until it is S units away
     * from the one behind them, then stops and signals another thief to move,
     * repeating the process until all three arrive at the Collection Site
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void crawlOut(int dist, int thid, int ppos) throws RemoteException;

    /***
     * Method for preparing and initiating the outgoing movement:
     * the last thread to call the method signals one other thread waiting on the
     * condition to initiate the movement
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void reverseDirection(int thid, int ppos) throws RemoteException;

    /***
     * Method that signals the start of the party's ingoing movement: the Master
     * signals a single thread to start moving
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void signalDeparture() throws RemoteException;

    /**
     * Operation server shutdown.
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void shutdown() throws RemoteException;

}
