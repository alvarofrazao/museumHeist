package sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
        }
    }

    public boolean rollACanvas(int roomID) {
        lock.lock();
        // oThief curThread = (oThief)Thread.currentThread();
        if (museumRoomsPaintings[roomID] > 0) // tentative implementation, most likely incorrect
        {
            museumRoomsPaintings[roomID] -= 1;
            lock.unlock();
            return true;
        } else {
            lock.unlock();
            return false;
        }
    }

    public int getRoomDistance(int roomID) {
        return museumRoomsDistance[roomID];
    }

    public int getPaintsInRoom(int roomID) {
        return museumRoomsPaintings[roomID];
    }
}