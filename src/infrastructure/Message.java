package src.infrastructure;

import java.io.Serializable;

public class Message implements Serializable {

    /**
     * Serialization key
    */

    private static final long serialVersionUID = 2021L;


    /**
     * Message type
     */
    private int msgType = -1;

    /**
     * Integer type return value
     */
    private int retValInt = -1;

    /**
     * Boolean type return value
     */
    private boolean retValBool = false;

    /**
     * Thread state (oThief & mThief)
     */
    private int thState = -1;

    /**
     * Thread identification 
     * Id > 6 corresponds do mThief
     * Id in [0;5] corresponds to oThief 
     */
    private int thId = -1;
    /**
     * oThief current situation
     */
    private char oThSit = '\0';

    /**
     * oThief maximum displacement
     */
    private char oThMaxDist = -1;

    /**
     * oThief party position [0;2]
     */
    private int oThPartyPos = -1;

    /**
     * oThief canvas flag
     */
    private boolean oThCanvas = false;

    /**
     * oThief first cycle flag
     */
    private boolean oThFC = false;

    /**
     * oThief index of currently assigned party
     */
    private int oThAP = -1;

    /**
     * oThief current destination room
     */
    private int oThRoom = -1;

    /**
     * Type 1 Instantiation
     * @param type message type
     * startOperations
     * takeARest
     * appraiseSit
     * sumUpResults
     */
    public Message(int type){
        msgType = type;
    }

    /**
     * Type 2 Instantiation
     * @param type Message type
     * @param id Thread ID
     * @param state Thread state
     */
    public Message(int type, int id, int state){
        msgType = type;  
    }
}
