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
    private int retValInt2 = -1;

    /**
     * Boolean return value slot
     */
    private boolean retBoolVal = false;

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
     * Distance to current destination
     */
    private int roomDist = -1;

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
     * @param type Message type: STARTOP,APSITREP,SNDPTY,SIGNDEP,TKREST,PREPAP
     *                           COLCANV,SUMRES,PREPEX,PREPEXREP,GETDIST,GETDISTREP
     * @param id entity ID or integer value 
     */
    public Message(int type, int val1){
        
        msgType = type; 
        switch(msgType){
            default:
                this.thId = val1;
                break;
            case MessageType.APSITREP:
                this.retValInt1 = val1;
                break;
            case MessageType.SIGNDEP:
                this.oThAP = val1;
                break;
            case MessageType.PREPEXREP:
                this.retValInt1 = val1;
                break;
            case MessageType.GETDIST:
                this.oThRoom = val1;
                break;
            case MessageType.GETDISTREP:
                this.retValInt1 = val1;
                break;
        }
    }

    /**
     * Type 3 Instantiation
     * @param type Message type: PREPAPREP,ADDTH,ROLLCAN,SETOTSTT,SETTHMD,SETAPRM,CRINREP,REVDIREP,STARTOPREP,TKRESTREP,SNDPTYREP
     *                           SETMTHSTT,ADDTHAP,REMTHAP,SETPNTSRM,SETRMDIS,HNDCANREP,CROUTREP,COLCANREP,SUMRESREP,SIGNDEPREP,SETPREP
     * @param id entity ID (room/thread/party)
     * @param val1 integer value
     */
    public Message(int type, int id, int val1){
        
        msgType = type;
        switch(msgType){
            case MessageType.PREPAPREP:
                this.oThAP = val1;
                this.thId = id;
                break;
            case MessageType.ADDTH:
                this.oThAP = val1;
                this.thId = id;
                break;
            case MessageType.ROLLCAN:
                this.thId = id;
                this.oThRoom = val1;
                break;
            case MessageType.SETOTSTT:
                this.thId = id;
                this.thState = val1;
                break;
            case MessageType.SETTHMD:
                this.thId = id;
                this.oThMaxDist = val1;
                break;
            case MessageType.SETMTHSTT:
                this.thId = id;
                this.thState = val1;
                break;
            case MessageType.ADDTHAP:
                this.oThAP = id;
                this.oThRoom = val1;
                break;
            case MessageType.REMTHAP:
                this.thId = id;
                this.oThAP = val1;
                break;
            case MessageType.SETAPRM:
                this.oThAP = id;
                this.oThRoom = val1;
                break;
            default:
                this.thId = id;
                this.thState = val1;
        }
    }

    /**
     * Type 4 Instantiation
     * @param type Message type: ADDTHREP,REVDIR,SETRDISTPNTS,SETTHPOS,SETTHCAN
     * @param id
     * @param val1
     * @param val2
     */
    public Message(int type, int id, int val1, int val2){

        msgType = type;
        switch(msgType){
            case MessageType.ADDTHREP:
                this.thId = id;
                this.oThPartyPos = val1;
                this.oThRoom = val2;
                break;
            case MessageType.REVDIR:
                this.thId = id;
                this.oThAP = val1;
                this.oThPartyPos = val2;
                break;
            case MessageType.SETRDISTPNTS:
                this.oThRoom = id;
                this.roomDist = val1;
                this.retValInt1 = val2;
                break;
            case MessageType.SETTHPOS:
                this.thId = id;
                this.oThPartyPos = val2;
                this.oThAP = val1;
                break;
            case MessageType.SETTHCAN:
                this.thId = id;
                this.oThAP = val1;
                this.retValInt1 = val2;
                break;
        }
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
        this.thId = id;
        this.roomDist = dist;
        this.oThAP = ap;
        this.oThPartyPos = partypos;
    }

    /**
     * Type 6 Instantiation
     * @param type Message type: ROLLCANREP
     * @param hasCanvas
     */
    public Message(int type, boolean hasCanvas){
        
        msgType = type;
        this.retBoolVal = hasCanvas;
    }
    
    /**
     * Type 7 Instantiation
     * @param type Message type: LOGINIT
     * @param fname
     */
    public Message(int type, String fname){

        msgType = type;
        this.logName = fname;
    }

    /**
     * Type 8 Instantiation
     * @param type Message type: AINREP
     * @param result
     * @param fc
     */
    public Message(int type, boolean result, boolean fc){

        msgType = type;
        this.retBoolVal = result;
        this.oThFC = fc;
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
        this.thId = id;
        this.oThCanvas = canvas;
        this.oThRoom = room;
    }

    /**
     * Type 10 Instantiation
     * @param type Message type: SETOTPSTT
     * @param id
     * @param state
     */
    public Message(int type, int id, char pstate){

        msgType = type;
        this.thId = id;
        this.oThSit = pstate;
    }

    /**
     * Type 11 Instantiation
     * @param type Message type: AIN
     * @param id
     * @param fc first cycle flag
     * @param pstate oThief party state
     */
    public Message(int type, int id, boolean fc, char pstate){

        msgType = type;
        this.thId = id;
        this.oThFC = fc;
        this.oThSit = pstate;
    }

    public int getMsgType(){
        return msgType;
    }

    public int getRI1(){
        return retValInt1;
    }

    public int getRI2(){
        return retValInt2;
    }

    public boolean getRB(){
        return retBoolVal;
    }

    public String getLogName(){
        return logName;
    }

    public int getThId(){
        return thId;
    }

    public int getThState(){
        return thState;
    }

    public char getoThSit(){
        return oThSit;
    }

   
    public int getoThMaxDist(){
        return oThMaxDist;
    } 


    public int getoThPartyPos(){
        return oThPartyPos;
    } 


    public boolean getoThCanvas(){
        return oThCanvas;
    }


    public boolean getoThFC() {
        return oThFC;
    }


    public int getoThAP() {
        return oThAP;
    }


    public int getoThRoom(){
        return oThRoom;
    }
    

    public int getroomDist(){
        return roomDist;
    }
}
