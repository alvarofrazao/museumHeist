package serverSide.sharedRegions;

import java.util.concurrent.locks.ReentrantLock;

import genclass.GenericIO;
import genclass.TextFile;
import infrastructure.ExecParameters;
import serverSide.main.ServerGeneralRepos;

/**
 * General Repository.
 *
 * It is responsible to keep the visible internal state of the problem and to
 * provide means for it
 * to be printed in the logging file.
 * It is implemented using ReentrantLock for synchronization.
 * All public methods are executed in mutual exclusion.
 * There are no internal synchronization points.
 * 
 * Variables that are but shouldnt be hardcoded: number of thieves, number of
 * rooms, number of thieves in party, number of assault parties
 */
public class GeneralRepos {
    /**
     * Name of the logging file.
     */
    private String logFileName;

    private final int[] oState;
    private final char[] oParty;
    private final int[] oMD;
    
    private final int[] apRoom;
    private final int[][][] apDetails;

    private final int[][] museumDetails;

    private int mState;

    private final ReentrantLock lock;

    /***
     * Instantiation of a general repository object.
     * 
     * @param logPath name of the logging file
     */
    public GeneralRepos() {
        oState = new int[6];
        oParty = new char[6];
        oMD = new int[6];
        apRoom = new int[2];
        apDetails = new int[2][3][3];
        museumDetails = new int[5][2];
        logFileName = ExecParameters.logName;
        for (int i = 0; i < 6; i++) {
            oParty[i] = 'W';
        }
        this.lock = new ReentrantLock();
    }

    /**
     * Write the header to the logging file.
     * Master thief planning the Heist and the ordinaries are blocked on
     * concentrarion area
     * Internal operation.
     */
    public void logInit() {
        TextFile log = new TextFile();

        if (!log.openForWriting(".", logFileName)) {
            GenericIO.writelnString("The operation of creating the file " + logFileName + " failed!");
            System.exit(1);
        }

        log.writelnString(
                "                                                                       Heist to the Museum - Description of the internal state\n");
        log.writelnString(
                "                                                                                        Assault party 1                      Assault party 2                                   Museum");
        log.writelnString(
                "MstT   Thief 1      Thief 2      Thief 3      Thief 4      Thief 5      Thief 6            Elem 1     Elem 2     Elem 3         Elem 1     Elem 2     Elem 3    Room 1  Room 2  Room 3  Room 4  Room 5");
        log.writelnString(
                "Stat  Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    RId  Id Pos Cv  Id Pos Cv  Id Pos Cv  RId  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   NP DT   NP DT");
        if (!log.close()) {
            GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
            System.exit(1);
        }
        logState();

    }

    /**
     * Write a state line at the end of the logging file.
     * The current state of all thieves, situation and max displacement of ordinary
     * thieves is organized in a line to be printed.
     * In another line print assault party related info (thieves, destination room,
     * etc) and room information (current painting and distance from site)
     * Internal operation.
     */
    private void logState() {
        TextFile log = new TextFile(); // instantiation of a text file handler

        String lineStatus = ""; // state line to be printed

        if (!log.openForAppending(".", logFileName)) {
            GenericIO.writelnString("The operation of opening for appending the file " + logFileName + " failed!");
            System.exit(1);
        }

        lineStatus += mState + "     ";

        for (int i = 0; i < 6; i++) {
            lineStatus += oState[i] + "    " + oParty[i] + "  " + oMD[i] + "    ";
        }

        for (int i = 0; i < 2; i++) {
            lineStatus += "  " + apRoom[i] + "  ";
            for (int j = 0; j < 3; j++) {
                String temp = String.format("%2d", apDetails[i][j][1]);
                lineStatus += " " + apDetails[i][j][0] + "  " + temp + "  " + apDetails[i][j][2] + "  ";
            }
        }
        lineStatus += " ";

        for (int i = 0; i < 5; i++) {
            lineStatus += String.format("%2d %2d   ", museumDetails[i][0], museumDetails[i][1]);
        }

        log.writelnString(lineStatus);

        if (!log.close()) {
            GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
            System.exit(1);
        }
    }

    /**
     * Set ordinary thief state
     * 
     * @param id    id of ordinary thief
     * @param state new state of ordinary thief
     */
    public void setOrdinaryThiefState(int id, int state) {
        lock.lock();
        this.oState[id] = state;
        logState();
        lock.unlock();
    }

    /**
     * Set ordinary thief party state
     * 
     * @param id    id of ordinary thief
     * @param state new state of ordinary thief's party 'W' for waiting and 'P' for
     *              in party
     */
    public void setOrdinaryThiefPartyState(int id, char state) {
        lock.lock();
        this.oParty[id] = state;
        logState();
        lock.unlock();
    }

    /**
     * Set ordinary thief maximum displacement
     * 
     * @param id id of ordinary thief
     * @param md max displacement of ordinary thief
     */
    public void setOrdinaryThiefMD(int id, int md) {
        lock.lock();
        this.oMD[id] = md;
        logState();
        lock.unlock();
    }

    /**
     * Set master thief state
     * 
     * @param state new state of master thief
     */
    public void setMasterThiefState(int state) {
        lock.lock();
        this.mState = state;
        logState();
        lock.unlock();
    }

    /**
     * Set the room that assault party is currently robbing
     * 
     * @param ap   assault party id
     * @param room room id
     */
    public void setAssaultPartyRoom(int ap, int room) {
        lock.lock();
        this.apRoom[ap] = room;
        logState();
        lock.unlock();
    }

    /**
     * Associate thief with an assault party, places thief in specified space.
     * Open spaces in assault party are determined by value 0
     * 
     * @param thief id of thief to associate
     * @param ap    id of assault party to associate to
     * @param pos   position to place thief in
     */
    public void addThiefToAssaultParty(int thief, int ap, int pos) {
        lock.lock();
        if (this.apDetails[ap][pos][0] != 0) {
            this.apDetails[ap][pos][2] = 0;
        }
        this.apDetails[ap][pos][0] = thief + 1;
        logState();
        lock.unlock();
    }

    /**
     * Removes thief from an assault party. Replaces thief's id with 0 to indicate
     * empty space
     * 
     * @param thief id of thief to remove
     * @param ap    id of assault party to remove from
     */
    public void removeThiefFromAssaultParty(int thief, int ap) {
        boolean removedThief = false;
        lock.lock();
        for (int i = 0; i < 3; i++) {
            if (this.apDetails[ap][i][0] == (thief + 1)) {
                this.apDetails[ap][i][0] = 0;
                removedThief = true;
                break;
            }
        }
        if (!removedThief) {
        }
        logState();
        lock.unlock();
    }

    /**
     * Updates thief's position and returns, or ends program if thief isnt in
     * specified assault party details.
     * If position is 0, then thief is in concentration or collection site
     * 
     * @param ap    assault party id
     * @param thief this id
     * @param pos   new position
     */
    public void setThiefPosition(int ap, int thief, int pos) {
        lock.lock();
        for (int i = 0; i < 3; i++) {
            if (this.apDetails[ap][i][0] == thief + 1) {
                this.apDetails[ap][i][1] = pos;
                logState();
                lock.unlock();
                return;
            }
        }

        lock.unlock();
    }

    /**
     * Updates thief's canvas status and returns or ends program if thief isnt in
     * specified assault party details
     * 
     * @param ap     assault party id
     * @param thief  thief id
     * @param canvas has canvas or not, should be 1 for yes and 0 for no
     */
    public void setThiefCanvas(int ap, int thief, int canvas) {
        lock.lock();
        for (int i = 0; i < 3; i++) {
            if (this.apDetails[ap][i][0] == thief + 1) {
                this.apDetails[ap][i][2] = canvas;
                logState();
                lock.unlock();
                return;
            }
        }

        lock.unlock();
        // System.exit (1);
    }

    /**
     * Sets the number of paintings still available in a given room
     * 
     * @param room      room id
     * @param num_pntgs number of painting currently available
     */
    public void setNumPaintingsInRoom(int room, int num_pntgs) {
        lock.lock();
        this.museumDetails[room][0] = num_pntgs;
        logState();
        lock.unlock();
    }

    /**
     * Sets the distance of the museum room from the concentration site
     * 
     * @param room     room id
     * @param distance room distance
     */
    public void setRoomDistance(int room, int distance) {
        lock.lock();
        this.museumDetails[room][1] = distance;
        logState();
        lock.unlock();
    }

    /**
     * Sets both room distance and number of painting at once
     * 
     * @param room          room id
     * @param distance      distance from concentration site
     * @param num_paintings number of paintings currently in room
     */
    public void setRoomDistanceAndPaintings(int room, int distance, int num_paintings) {
        lock.lock();
        this.museumDetails[room][0] = num_paintings;
        this.museumDetails[room][1] = distance;
        lock.unlock();
    }

    /**
     * Appends to log file the total number of stolen paitings
     * 
     * @param total_paintings number of stolen paitings
     */
    public void finalResult(int total_paintings) {
        TextFile log = new TextFile(); // instantiation of a text file handler

        String lineStatus = ""; // state line to be printed

        if (!log.openForAppending(".", logFileName)) {
            GenericIO.writelnString("The operation of opening for appending the file " + logFileName + " failed!");
            System.exit(1);
        }

        lineStatus += String.format("My friends, tonight's effort produced %2d priceless paintings!", total_paintings);

        lock.lock();
        log.writelnString(lineStatus);
        lock.unlock();

        if (!log.close()) {
            GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
            System.exit(1);
        }

    }

    public void shutdown(){
        try {
            lock.lock();
            ServerGeneralRepos.waitConnection = false;
        }finally{
            lock.unlock();
        }
    }

    /*
     * assaultparty1 assaultparty2
     * e1 e2 e3 e1 e2 e3
     * id,pos,canv id,pos,canv id,pos,canv id,pos,canv id,pos,canv id,pos,canv
     * [ [5,20,1] , [1,2,3], [1,2,3] ] , [ [1,2,3] , [1,2,3], [1,2,3] ]
     */

    /*
     * museum
     * room1 room2 room3
     * num paint, distance num paint, distance num paint, distance
     * [ [3,5] , [3,8] , [5,9] ]
     */
}
