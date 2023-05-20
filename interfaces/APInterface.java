package interfaces;

import java.rmi.*;


public interface APInterface extends Remote {

    public ReturnBoolean getStatus() throws RemoteException;

    public ReturnInt getRoomID()throws RemoteException;

    public void setupParty(int roomID) throws RemoteException;

    public ReturnInt addThief() throws RemoteException;

    public void crawlIn(int dist) throws RemoteException;

    public void crawlOut(int dist) throws RemoteException;

    public void reverseDirection() throws RemoteException;

    public void signalDeparture() throws RemoteException;

    public void shutdown() throws RemoteException;
    
}
