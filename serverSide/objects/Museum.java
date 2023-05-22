package serverSide.objects;

import java.rmi.RemoteException;
import java.util.concurrent.locks.ReentrantLock;

import genclass.GenericIO;
import interfaces.GeneralReposInterface;
import interfaces.MuseumInterface;
import interfaces.ReturnBoolean;
import interfaces.ReturnInt;

public class Museum implements MuseumInterface{

    private int[] museumRoomsDistance;
    private int[] museumRoomsPaintings;
    // private final GeneralRepos repos;
    private final GeneralReposInterface grStub;
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
    public Museum(int numberOfRooms, int MAX_D, int MIN_D, int MAX_P, int MIN_P, GeneralReposInterface grStub/*
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
            try {
                grStub.setRoomDistanceAndPaintings(i, museumRoomsDistance[i], museumRoomsPaintings[i]);
            } catch (Exception e) {
                // TODO: handle exception
            }
            lock.unlock();
        }
    }

    public ReturnInt getRoomDistance(int roomID) {
        ReturnInt ret = new ReturnInt(museumRoomsDistance[roomID], 99);
        return ret;
    }

    public ReturnInt getPaintsInRoom(int roomID) {
        ReturnInt ret = new ReturnInt(museumRoomsPaintings[roomID], 99);
        return ret;
    }

    /**
     * Computes whether or not the thief successfully stole a canvas from the room
     * 
     * @param roomID
     * @return Whether or not the thief thread managed to get a canvas
     */
    public ReturnBoolean rollACanvas(int roomID,int thid, int ap) {
        try {
            lock.lock();
            boolean result;
            ReturnBoolean ret;
            if (museumRoomsPaintings[roomID] > 0) {
                museumRoomsPaintings[roomID] -= 1;
                result = true;
                try {
                    grStub.setNumPaintingsInRoom(roomID, museumRoomsPaintings[roomID]);
                } catch (RemoteException e) {
                    GenericIO.writelnString("Thief " + thid + " remote exception on reverseDirection: " + e.getMessage());
                    System.exit(1);
                }
                try {
                    grStub.setThiefCanvas(ap, thid, 1);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            } else {
                result = false;
            }
            ret = new ReturnBoolean(result, false);
            return ret;
        } finally {
            lock.unlock();
        }

    }

    public void shutdown() {
        try {
            lock.lock();
            //ServerMuseum.waitConnection = false;
        } finally {
            lock.unlock();
        }
    }
}