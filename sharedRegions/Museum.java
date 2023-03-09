package sharedRegions;

import infrastructure.MuseumRoom;

public class Museum {
    private MuseumRoom[] museumRooms;

    public Museum(int numberOfRooms) {
        this.museumRooms = new MuseumRoom[numberOfRooms];
        for(int i = 0; i < numberOfRooms; i++)
        {
            museumRooms[i] = new MuseumRoom(i);
        }
    }

    public boolean rollACanvas(int roomID)
    {
        if(museumRooms[roomID].getPaintsInRoom() > 0) //tentative implementation, most likely incorrect
        {
            museumRooms[roomID].paintTaken();
            return true;
        }
        else return false;
    }
    
    public MuseumRoom getRoom(int roomID)
    {
        return museumRooms[roomID];
    }
}
