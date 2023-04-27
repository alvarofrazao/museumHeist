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
    private int retValInt1 = -1;

    /**
     * Integer type return value 2
     */
    private boolean retValInt2 = false;

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
    private int oThMaxDist = -1;

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
     * Logging file name
     */
    private String logName = null;

    /**
     * Type 1 Instantiation
     * @param type message type: APSIT,SETP,FINRES,ACK
     */
    public Message(int type){
        
        msgType = type;
    }

    /**
     * Type 2 instantiation
     * @param type Message type: STARTOP,APSITREP,SNDPTY,SIGNDEP,TKREST,
     *                           COLCANV,SUMRES,PREPEX,PREPEXREP,GETDIST,GETDISTREP
     * @param id entity ID or integer value 
     */
    public Message(int type, int id){
        
        msgType = type;  

    }

    /**
     * Type 3 Instantiation
     * @param type Message type: PREPAPREP,ADDTH,ROLLCAN,SETOTSTT,SETTHMD,
     *                           SETMTHSTT,ADDTHAP,REMTHAP,SETTHPOS,SETPNTSRM,SETRMDIS
     * @param id entity ID (room/thread)
     * @param val1 integer value
     */
    public Message(int type, int id, int val1){
        
        msgType = type;
    }

    /**
     * Type 4 Instantiation
     * @param type Message type: ADDTHREP,REVDIR,SETRDISTPNTS
     * @param id
     * @param val1
     * @param val2
     */
    public Message(int type, int id, int val1, int val2){

        msgType = type;
    }


    /**
     * Type 5 Instantiation
     * @param type Message type: CRIN, CROUT
     * @param id
     * @param dist
     * @param ap
     * @param partypos
     */
    public Message(int type, int id, int dist, int ap, int partypos){

        msgType = type;
    }

    /**
     * Type 6 Instantiation
     * @param type Message type: ROLLCANREP
     * @param hasCanvas
     */
    public Message(int type, boolean hasCanvas){
        
        msgType = type;
    }
    
    /**
     * Type 7 Instantiation
     * @param type Message type: LOGINIT
     * @param fname
     */
    public Message(int type, String fname){

        msgType = type;
    }

    /**
     * Type 8 Instantiation
     * @param type Message type: AINREP
     * @param result
     * @param fc
     */
    public Message(int type, boolean result, boolean fc){

        msgType = type;
    }

    /**
     * Type 9 Instantiation
     * @param type Message type: HNDCAN
     * @param id
     * @param canvas
     * @param room
     */
    public Message(int type, int id, boolean canvas, int room){

        msgType = type;
    }

    /**
     * Type 10 Instantiation
     * @param type Message type: SETOTPSTT
     * @param id
     * @param state
     */
    public Message(int type, int id, char state){

        msgType = type;
    }

    /**
     * Type 11 Instantiation
     * @param type Message type: SETTHCAN
     * @param id
     * @param canvas
     */
    public Message(int type, int id, boolean canvas){

        msgType = type;
    }

    /**
     * Type 12 Instantiation
     * @param type Message type: AIN
     * @param id
     * @param fc
     * @param pstate
     */
    public Message(int type, int id, boolean fc, char pstate){

        msgType = type;
    }





}
