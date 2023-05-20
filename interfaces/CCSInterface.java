package interfaces;

import java.rmi.*;

public interface CCSInterface extends Remote{
    
    public void sendAssaultParty() throws RemoteException;

    public ReturnInt prepareExcursion() throws RemoteException;

    public void shutdown() throws RemoteException;
}
