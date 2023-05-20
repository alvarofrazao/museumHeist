package interfaces;

import java.rmi.*;

public interface GeneralRepossInterface extends Remote{
    
    public void logInit() throws RemoteException;

    public void setOrdinaryThiefState(int id, int state) throws RemoteException;

    public void setOrdinaryThiefPartyState(int id, char state) throws RemoteException;

    public void setOrdinaryThiefMD(int id, int md) throws RemoteException;

    public void setMasterThiefState(int state) throws RemoteException;

    public void setAssaultPartyRoom(int ap, int room) throws RemoteException;

    public void addThiefToAssaultParty(int thief, int ap, int pos) throws RemoteException;

    public void removeThiefFromAssaultParty(int thief, int ap) throws RemoteException;

    public void setThiefPosition(int ap, int thief, int pos) throws RemoteException;

    public void setThiefCanvas(int ap, int thief, int canvas) throws RemoteException;

    public void setRoomDistance(int room, int distance) throws RemoteException;

    public void setRoomDistanceAndPaintings(int room, int distance, int num_paintings) throws RemoteException;

    public void finalResult(int total_paintings) throws RemoteException;

    public void shutdown() throws RemoteException;
}
