package serverSide.sharedRegions;

import java.util.concurrent.locks.ReentrantLock;

import serverSide.entities.museumClientProxy;
import serverSide.main.ServerMuseum;
import serverSide.stubs.GeneralReposStub;

public class Museum {

    private int[] museumRoomsDistance;
    private int[] museumRoomsPaintings;
    // private final GeneralRepos repos;
    private final GeneralReposStub grStub;
    private ReentrantLock lock;

    /***
     * Instatiation of Museum object
     * 
     * @param numberOfRooms
     * @param MAX_D         maximum range for room distance random number generation
     * @param MIN_D         minimum range for room distance random number generation
     * @param MAX_P         maximum range for number of paintings random number
     *                      generation
     * @param MIN_P         minimum range for number of paintings random number
     *                      generation
     * @param repos         reference to a GeneralRepository shared memory region
     */
    public Museum(int numberOfRooms, int MAX_D, int MIN_D, int MAX_P, int MIN_P, GeneralReposStub grStub/*
                                                                                                         * GeneralRepos
                                                                                                         * repos
                                                                                                         */) {
        this.museumRoomsDistance = new int[numberOfRooms];
        this.museumRoomsPaintings = new int[numberOfRooms];
        this.lock = new ReentrantLock();
        this.grStub = grStub;

        for (int i = 0; i < numberOfRooms; i++) {
            museumRoomsDistance[i] = (int) ((Math.random() * (MAX_D - MIN_D)) + MIN_D);

            museumRoomsPaintings[i] = (int) ((Math.random() * (MAX_P - MIN_P)) + MIN_P);
            lock.lock();
            // repos.setRoomDistanceAndPaintings(i, museumRoomsDistance[i],
            // museumRoomsPaintings[i]);
            grStub.setRoomDistanceAndPaintings(i, museumRoomsDistance[i], museumRoomsPaintings[i]);
            lock.unlock();
        }
    }

    public int getRoomDistance(int roomID) {
        return museumRoomsDistance[roomID];
    }

    public int getPaintsInRoom(int roomID) {
        return museumRoomsPaintings[roomID];
    }

    /**
     * Computes whether or not the thief successfully stole a canvas from the room
     * 
     * @param roomID
     * @return Whether or not the thief thread managed to get a canvas
     */
    public boolean rollACanvas(int roomID) {
        try {
            lock.lock();
            museumClientProxy curThread = (museumClientProxy) Thread.currentThread();

            if (museumRoomsPaintings[roomID] > 0) {
                museumRoomsPaintings[roomID] -= 1;
                curThread.setCanvas(true);
                grStub.setNumPaintingsInRoom(roomID, museumRoomsPaintings[roomID]);
                grStub.setThiefCanvas(curThread.getAp(), curThread.getThId(), 1);
                return true;
            } else {
                curThread.setCanvas(false);
                return false;
            }
        } finally {
            lock.unlock();
        }

    }

    public void shutdown() {
        try {
            lock.lock();
            ServerMuseum.waitConnection = false;
        } finally {
            lock.unlock();
        }
    }

}