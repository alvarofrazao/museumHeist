package interfaces;

import java.rmi.*;

public interface MuseumInterface extends Remote {
    
    public ReturnInt getRoomDistance(int roomID) throws RemoteException;

    public ReturnInt getPaintsInRoom(int roomID) throws RemoteException;

    public ReturnBoolean rollACanvas(int roomID)throws RemoteException;

    public void shutdown() throws RemoteException;
}
