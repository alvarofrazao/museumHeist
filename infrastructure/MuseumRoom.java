package infrastructure;

public class MuseumRoom {
    private int roomID;

    private int paintsInRoom;

    private int distanceToSite;


    public MuseumRoom(int roomID,int paintsInRoom, int distanceToSite)
    {
        this.roomID = roomID;
        this.paintsInRoom = paintsInRoom;
        this.distanceToSite = distanceToSite;
    }

    public int getID()
    {
        return roomID;
    }

    public void decrementPaints()
    {
        this.paintsInRoom--;
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
