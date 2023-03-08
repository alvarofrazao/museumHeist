package infrastructure;

import java.util.Random;

public class MuseumRoom {
    private int roomID;

    private int paintsInRoom;

    private int distanceToSite;
    


    public MuseumRoom(int roomID)
    {
        Random rand = new Random();
        this.paintsInRoom = ((rand.nextInt(1) + 1) * 9) - 1;
        this.roomID = roomID;
        this.distanceToSite = ((rand.nextInt(1) + 1) * 16) - 1;
    }

    public int getID()
    {
        return roomID;
    }

    public void paintTaken()
    {
        paintsInRoom--;
    }

    public int getPaintsInRoom()
    {
        return paintsInRoom;
    }

    public int getDistance()
    {
        return distanceToSite;
    }
}
