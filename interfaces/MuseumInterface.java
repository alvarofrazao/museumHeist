package interfaces;

import java.rmi.*;

public interface MuseumInterface extends Remote {

    /**
     * Returns the distance to a specific room
     * 
     * @param roomID room to get distance to
     * @return distance to chosen room
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public ReturnInt getRoomDistance(int roomID) throws RemoteException;

    /**
     * Returns current paintins in a specific room
     * 
     * @param roomID room to get number of paintings from
     * @return number of paintings in chosen room
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public ReturnInt getPaintsInRoom(int roomID) throws RemoteException;

    /**
     * Computes whether or not the thief successfully stole a canvas from the room
     * 
     * @param roomID
     * @return Whether or not the thief thread managed to get a canvas
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public ReturnBoolean rollACanvas(int roomID, int thid, int ap) throws RemoteException;

    /**
     * Operation server shutdown.
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void shutdown() throws RemoteException;
}
