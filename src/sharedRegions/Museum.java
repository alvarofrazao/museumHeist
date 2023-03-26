package src.sharedRegions;

import java.util.concurrent.locks.ReentrantLock;

import src.entities.oThief;

public class Museum {
    private int[] museumRoomsDistance;
    private int[] museumRoomsPaintings;
    private final GeneralRepos repos;
    private ReentrantLock lock;

    public Museum(int numberOfRooms, int MAX_D, int MIN_D, int MAX_P, int MIN_P, GeneralRepos repos) {
        this.museumRoomsDistance = new int[numberOfRooms];
        this.museumRoomsPaintings = new int[numberOfRooms];
        this.lock = new ReentrantLock();
        this.repos = repos;

        for (int i = 0; i < numberOfRooms; i++) {
            museumRoomsDistance[i] = (int) ((Math.random() * (MAX_D - MIN_D)) + MIN_D);
            museumRoomsPaintings[i] = (int) ((Math.random() * (MAX_P - MIN_P)) + MIN_P);
            lock.lock();
            repos.setRoomDistanceAndPaintings(i, museumRoomsDistance[i],  museumRoomsPaintings[i]);
            lock.unlock();
        }
    }

    public int getRoomDistance(int roomID) {
        return museumRoomsDistance[roomID];
    }

    public int getPaintsInRoom(int roomID) {
        return museumRoomsPaintings[roomID];
    }

    public boolean rollACanvas(int roomID) {

        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        //System.out.println("rollACanvas " + curThread.getCurAP() + " " +  curThread.getThiefID() + " " + curThread.getCurRoom());
        if (museumRoomsPaintings[roomID] > 0) 
        {
            museumRoomsPaintings[roomID] -= 1;
            curThread.setCanvas(true);
            repos.setNumPaintingsInRoom(roomID, museumRoomsPaintings[roomID]);
            repos.setThiefCanvas(curThread.getCurAP(), curThread.getThiefID(), 1);
            lock.unlock();
            return true;
        } else {
            //System.out.println("didnt get Canvas" + curThread.getThiefID());
            curThread.setCanvas(false);
            lock.unlock();
            return false;
        }
    }


}