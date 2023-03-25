package src.sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import src.entities.oThief;

public class Museum {
    private int[] museumRoomsDistance;
    private int[] museumRoomsPaintings;
    private GeneralRepos repos;
    private ReentrantLock lock;
    private Condition cond;

    public Museum(int numberOfRooms, int MAX_D, int MIN_D, int MAX_P, int MIN_P) {
        this.museumRoomsDistance = new int[numberOfRooms];
        this.museumRoomsPaintings = new int[numberOfRooms];
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();

        for (int i = 0; i < numberOfRooms; i++) {
            museumRoomsDistance[i] = (int) ((Math.random() * (MAX_D - MIN_D)) + MIN_D);
            museumRoomsPaintings[i] = (int) ((Math.random() * (MAX_P - MIN_P)) + MIN_P);
            //System.out.println("room number " + i + " " + museumRoomsDistance[i] + " " + museumRoomsPaintings[i]);
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
        System.out.println("rollACanvas " + curThread.getCurAP() + " " +  curThread.getThiefID());
        if (museumRoomsPaintings[roomID] > 0) 
        {
            //System.out.println("Got Canvas" + curThread.getCurAP() + " " + curThread.getPartyPos());
            museumRoomsPaintings[roomID] -= 1;
            curThread.setCanvas(true);
            lock.unlock();
            return true;
        } else {
            //System.out.println("didnt got Canvas" + curThread.getThiefID());
            curThread.setCanvas(false);
            lock.unlock();
            return false;
        }
    }


}