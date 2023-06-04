package interfaces;

import java.rmi.*;

public interface CCLInterface extends Remote{
    
    public ReturnInt getNextRoom() throws RemoteException;

    public ReturnBoolean getHeistStatus() throws RemoteException;

    public ReturnBoolean amINeeded(int thid,boolean fc, int ap) throws RemoteException;

    public ReturnInt prepareAssaultParty() throws RemoteException;

    public void takeARest() throws RemoteException;

    public void collectACanvas() throws RemoteException;

    public void handACanvas(int thid, boolean canvas, int ap, int room) throws RemoteException;

    public ReturnInt appraiseSit() throws RemoteException;

    public void startOperations() throws RemoteException;

    public void sumUpResults() throws RemoteException;

    public void shutdown() throws RemoteException;
}
