package infrastructure;

import java.util.Random;

import sharedRegions.GeneralRepos;

//this entire class will be removed, with all needed methods implemented in Museum.java

public class MuseumRoom {
    private int roomID;

    private int paintsInRoom;

    private int distanceToSite;

    private GeneralRepos repos;

    public MuseumRoom(int roomID) {
        Random rand = new Random();
        this.paintsInRoom = ((rand.nextInt(1) + 1) * 9) - 1;// change rng
        this.roomID = roomID;
        this.distanceToSite = ((rand.nextInt(1) + 1) * 16) - 1;// change rng
    }

    public int getID() {
        return roomID;
    }

    public void paintTaken() {
        paintsInRoom--;
    }

    public int getPaintsInRoom() {
        return paintsInRoom;
    }

    public int getDistance() {
        return distanceToSite;
    }
}
