package interfaces;

import java.rmi.*;

public interface GeneralReposInterface extends Remote {

    /**
     * Write the header to the logging file.
     * Master thief planning the Heist and the ordinaries are blocked on
     * concentrarion area
     * Internal operation.
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void logInit() throws RemoteException;

    /**
     * Set ordinary thief state
     * 
     * @param id    id of ordinary thief
     * @param state new state of ordinary thief
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void setOrdinaryThiefState(int id, int state) throws RemoteException;

    /**
     * Set ordinary thief party state
     * 
     * @param id    id of ordinary thief
     * @param state new state of ordinary thief's party 'W' for waiting and 'P' for
     *              in party
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void setOrdinaryThiefPartyState(int id, char state) throws RemoteException;

    /**
     * Set ordinary thief maximum displacement
     * 
     * @param id id of ordinary thief
     * @param md max displacement of ordinary thief
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void setOrdinaryThiefMD(int id, int md) throws RemoteException;

    /**
     * Set master thief state
     * 
     * @param state new state of master thief
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void setMasterThiefState(int state) throws RemoteException;

    /**
     * Set the room that assault party is currently robbing
     * 
     * @param ap   assault party id
     * @param room room id
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void setAssaultPartyRoom(int ap, int room) throws RemoteException;

    /**
     * Associate thief with an assault party, places thief in specified space.
     * Open spaces in assault party are determined by value 0
     * 
     * @param thief id of thief to associate
     * @param ap    id of assault party to associate to
     * @param pos   position to place thief in
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void addThiefToAssaultParty(int thief, int ap, int pos) throws RemoteException;

    /**
     * Removes thief from an assault party. Replaces thief's id with 0 to indicate
     * empty space
     * 
     * @param thief id of thief to remove
     * @param ap    id of assault party to remove from
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void removeThiefFromAssaultParty(int thief, int ap) throws RemoteException;

    /**
     * Updates thief's position and returns, or ends program if thief isnt in
     * specified assault party details.
     * If position is 0, then thief is in concentration or collection site
     * 
     * @param ap    assault party id
     * @param thief this id
     * @param pos   new position
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void setThiefPosition(int ap, int thief, int pos) throws RemoteException;

    /**
     * Updates thief's canvas status and returns or ends program if thief isnt in
     * specified assault party details
     * 
     * @param ap     assault party id
     * @param thief  thief id
     * @param canvas has canvas or not, should be 1 for yes and 0 for no
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void setThiefCanvas(int ap, int thief, int canvas) throws RemoteException;

    /**
     * Sets the number of paintings still available in a given room
     * 
     * @param room      room id
     * @param num_pntgs number of painting currently available
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void setRoomDistance(int room, int distance) throws RemoteException;

    /**
     * Sets the distance of the museum room from the concentration site
     * 
     * @param room     room id
     * @param distance room distance
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void setNumPaintingsInRoom(int room, int paints) throws RemoteException;

    /**
     * Sets both room distance and number of painting at once
     * 
     * @param room          room id
     * @param distance      distance from concentration site
     * @param num_paintings number of paintings currently in room
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void setRoomDistanceAndPaintings(int room, int distance, int num_paintings) throws RemoteException;

    /**
     * Appends to log file the total number of stolen paitings
     * 
     * @param total_paintings number of stolen paitings
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void finalResult(int total_paintings) throws RemoteException;

    /**
     * Operation server shutdown.
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry
     *                         service fails
     */
    public void shutdown() throws RemoteException;
}
