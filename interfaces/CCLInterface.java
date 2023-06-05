package interfaces;

import java.rmi.*;

public interface CCLInterface extends Remote {

    /**
     * Getter for the index of the next room to target
     * 
     * @throws RemoteException if either the invocation of the remote method, or
     *                         the
     *                         communication with the registry
     *                         service fails
     */
    public ReturnInt getNextRoom() throws RemoteException;

    /**
     * @throws RemoteException if either the invocation of the remote method, or
     *                         the
     *                         communication with the registry
     *                         service fails
     */
    public ReturnBoolean getHeistStatus() throws RemoteException;

    /***
     * Controls the lifecycle of the Ordinary Thief threads
     * 
     * @return true or false, depending on the status of the heist
     * @throws RemoteException if either the invocation of the remote method, or
     *                         the
     *                         communication with the registry
     *                         service fails
     */
    public ReturnBoolean amINeeded(int thid, boolean fc, int ap) throws RemoteException;

    /***
     * Sets up an Assault Party to be formed, assigning it a room and signaling the
     * Ordinary Thief threads
     * 
     * @return index of party currently assembling
     * @throws RemoteException if either the invocation of the remote method, or
     *                         the
     *                         communication with the registry
     *                         service fails
     */
    public ReturnInt prepareAssaultParty() throws RemoteException;

    /***
     * Forces the Master Thief thread to sleep for a pre-determined amount of time
     * 
     * @throws RemoteException if either the invocation of the remote method, or
     *                         the
     *                         communication with the registry
     *                         service fails
     */
    public void takeARest() throws RemoteException;

    /***
     * Method for the retrieval of canvases from the Collection Site. Only one
     * canvas is processed in a method call
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void collectACanvas() throws RemoteException;

    /***
     * Method for the handing in of canvases
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void handACanvas(int thid, boolean canvas, int ap, int room) throws RemoteException;

    /***
     * Method for controlling the lifecycle of the Master Thief Thread
     * 
     * @return Integer value dependent on the current situation of the heist
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public ReturnInt appraiseSit() throws RemoteException;

    /***
     * Transitory method for initiating the simulation
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void startOperations() throws RemoteException;

    /***
     * Transitory method for closing off the simulation: signals all Ordinary Thief
     * threads upon exiting
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void sumUpResults() throws RemoteException;

    /**
     * Operation server shutdown.
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void shutdown() throws RemoteException;
}
