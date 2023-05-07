package infrastructure;

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

    // Message types das instantiaçoes estao incorretos, alguns em falta, outros nos
    // sitios errados
    /**
     * Type 1 Instantiation
     * 
     * @param type message type:
     */
    public Message(int type) {

        msgType = type;
    }

    /**
     * Type 2 instantiation
     * 
     * @param type
     * @param id   entity ID or integer value
     */
    public Message(int type, int val1) {

        msgType = type;
        /*
         * switch(msgType){
         * default:
         * this.thId = val1;
         * break;
         * case MessageType.APSITREP:
         * this.retValInt1 = val1;
         * break;
         * case MessageType.PREPEXREP:
         * this.oThAP = val1;
         * break;
         * case MessageType.PREPAPREP:
         * this.oThAP = val1;
         * case MessageType.GETDIST:
         * this.oThRoom = val1;
         * break;
         * case MessageType.SETP:
         * this.oThRoom = val1;
         * break;
         * case MessageType.GETDISTREP:
         * this.roomDist = val1;
         * break;
         * case MessageType.FINRES:
         * this.retValInt1 = val1;
         * break;
         * case MessageType.GETNRMREP:
         * this.oThRoom = val1;
         * break;
         * }
         */ // (msgType == MessageType.)
        if ((msgType == MessageType.FINRES) || (msgType == MessageType.APSITREP)) {
            this.retValInt1 = val1;
        } else if ((msgType == MessageType.SETP) || (msgType == MessageType.GETNRMREP)
                || (msgType == MessageType.GETDIST)) {
            this.oThRoom = val1;
        } else if ((msgType == MessageType.PREPAPREP) || (msgType == MessageType.PREPEXREP)) {
            this.oThAP = val1;
        } else if ((msgType == MessageType.GETDISTREP)) {
            this.retValInt1 = val1;
        } else {
            this.thId = val1;
        }
    }

    /**
     * Type 3 Instantiation
     * 
     * @param type
     * @param id   entity ID (room/thread/party)
     * @param val1 integer value
     */
    public Message(int type, int val1, int val2) {

        msgType = type;
        /*
         * switch (msgType) {
         * case MessageType.ADDTHREP:
         * this.oThPartyPos = val1;
         * this.oThRoom = val2;
         * break;
         * case MessageType.REVDIR:
         * this.oThPartyPos = val2;
         * this.thId = val1;
         * break;
         * case MessageType.SETTHMD:
         * this.thId = val1;
         * this.oThMaxDist = val2;
         * break;
         * case MessageType.REMTHAP:
         * this.thId = val1;
         * this.oThAP = val2;
         * break;
         * case MessageType.SETAPRM:
         * this.oThAP = val1;
         * this.oThRoom = val2;
         * break;
         * case MessageType.SETPNTSRM:
         * this.oThRoom = val1;
         * this.retValInt1 = val2;
         * break;
         * default:
         * this.thId = val1;
         * this.thState = val2;
         * }
         */
        if ((msgType == MessageType.ADDTHREP)) {
            this.oThPartyPos = val1;
            this.oThRoom = val2;
        } else if ((msgType == MessageType.REVDIR)) {
            this.thId = val1;
            this.oThPartyPos = val2;
        } else if ((msgType == MessageType.SETTHMD)) {
            this.thId = val1;
            this.oThMaxDist = val2;
        } else if ((msgType == MessageType.REMTHAP)) {
            this.thId = val1;
            this.oThAP = val2;
        } else if ((msgType == MessageType.SETAPRM)) {
            this.oThAP = val1;
            this.oThRoom = val2;
        } else if ((msgType == MessageType.SETPNTSRM)) {
            this.oThRoom = val1;
            this.retValInt1 = val2;
        } else {
            this.thId = val1;
            this.thState = val2;
        }
    }

    /**
     * Type 4 Instantiation
     * 
     * @param type Message type:
     * @param id
     * @param val1
     * @param val2
     */
    public Message(int type, int val1, int val2, int val3) {

        msgType = type;
        if ((msgType == MessageType.ROLLCAN)) {
            this.thId = val1;
            this.oThAP = val2;
            this.oThRoom = val3;
        } else if ((msgType == MessageType.ADDTHAP)) {
            this.thId = val1;
            this.oThPartyPos = val3;
            this.oThAP = val2;
        } else if ((msgType == MessageType.SETTHPOS)) {
            this.thId = val1;
            this.oThAP = val2;
            this.retValInt1 = val3;
        } else if ((msgType == MessageType.SETTHCAN)) {
            this.thId = val1;
            this.oThAP = val2;
            this.retValInt1 = val3;
        } else if ((msgType == MessageType.SETRDISTPNTS)) {
            this.oThRoom = val1;
            this.roomDist = val2;
            this.retValInt1 = val3;
        } else {
            this.thId = val1;
            this.roomDist = val2;
            this.oThPartyPos = val3;
        }

    }

    /**
     * Type 5 Instantiation
     * 
     * @param type message type
     * @param id   thread id
     * @param fc   firstCycle flag
     * @param ap   assaultParty id
     */
    public Message(int type, int id, boolean fc, int ap) {
        this.msgType = type;
        this.thId = id;
        this.oThFC = fc;
        this.oThAP = ap;
    }

    /**
     * Type 6 instantiation
     * 
     * @param type   message type- AINREP
     * @param fc     firstCycle flag
     * @param result flag for whether the thief is needed or not
     */
    public Message(int type, boolean fc, boolean result) {
        this.msgType = type;
        this.oThFC = fc;
        this.retBoolVal = result;
    }

    /**
     * Type 7 Instantiation
     * 
     * @param type        message type - HNDCAN
     * @param id          thread id
     * @param ap          thread ap
     * @param room        room visited
     * @param hasPainting got painting flag
     */
    public Message(int type, int id, int ap, int room, boolean hasPainting) {
        this.msgType = type;
        this.thId = id;
        this.oThAP = ap;
        this.oThRoom = room;
        this.oThCanvas = hasPainting;
    }

    /**
     * Type 8 Instantiation
     * 
     * @param type      message type - ROLLCAN
     * @param gotCanvas got painting from room flag
     */
    public Message(int type, boolean gotCanvas) {
        this.msgType = type;
        this.retBoolVal = gotCanvas;
    }

    public Message(int type, int id, char pstate) {
        this.msgType = type;
        this.thId = id;
        this.oThSit = pstate;
    }

    public int getMsgType() {
        return msgType;
    }

    public int getRI1() {
        return retValInt1;
    }

    public int getRI2() {
        return retValInt2;
    }

    public boolean getRB() {
        return retBoolVal;
    }

    public String getLogName() {
        return logName;
    }

    public int getThId() {
        return thId;
    }

    public int getThState() {
        return thState;
    }

    public char getoThSit() {
        return oThSit;
    }

    public int getoThMaxDist() {
        return oThMaxDist;
    }

    public int getoThPartyPos() {
        return oThPartyPos;
    }

    public boolean getoThCanvas() {
        return oThCanvas;
    }

    public boolean getoThFC() {
        return oThFC;
    }

    public int getoThAP() {
        return oThAP;
    }

    public int getoThRoom() {
        return oThRoom;
    }

    public int getroomDist() {
        return roomDist;
    }

    @Override

    public String toString() {
        return ("Message type = " + msgType
                + "\nThread id = " + thId
                + "\nThread state = " + thState
                + "\noClient Sit = " + oThSit
                + "\nAP = " + oThAP
                + "\nMD = " + oThMaxDist
                + "\nPpos = " + oThPartyPos
                + "\nRoom = " + oThRoom
                + "\nCanvas = " + oThCanvas
                + "\nFC = " + oThFC
                + "\nRoom Dist = " + roomDist
                + "\nLogName = " + logName
                + "\nRI1 = " + retValInt1
                + "\nRI2 = " + retValInt2
                + "\nRB = " + retBoolVal);
    }
}
