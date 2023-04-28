package src.infrastructure;

public class MessageType {

    /**---
     * Start Operations request int type, int id
     */
    public static final int STARTOP = 1;

    /**
     * Appraise Situation request int type
     */
    public static final int APSIT= 2;

    /**
     * Appraise Situation reply int type, int return
     */
    public static final int APSITREP= 3;

    /**
     * Prepare Assault Party request int type, int id
     */
    public static final int PREPAP= 4;

    /**
     * Prepare Assault Party reply int type, int id, int ap
     */
    public static final int PREPAPREP = 5;

    /**
     * Setup Party request int type
     */
    public static final int SETP= 6;

    /**
     * Send Assault party request int type, int id
     */
    public static final int SNDPTY = 7;
      
    /**
     * Signal departure request int type, int ap
     */
    public static final int SIGNDEP= 8;
       
    /**
     * Take a rest request int type, int id
     */
    public static final int TKREST= 9;

    /**
     * Collect a canvas request int type, int id
     */
    public static final int COLCANV = 10;
       
    /**
     * Sum up results request int type, int id
     */
    public static final int SUMRES = 11;
    
    /**
     * Am I needed request int type, int id, boolean fc, char pstate
     */
    public static final int AIN = 11;

    /**
     * Am I needed reply int type, boolean result, boolean fc
     */
    public static final int AINREP = 12;
    
    /**
     * Prepare excursion request int type, int id
     */
    public static final int PREPEX = 13;
    
    /**
     * Prepare excursion reply int type, int nextparty
     */
    public static final int PREPEXREP = 14;
    
    /**
     * addThief request int type, int id, int party
     */
    public static final int ADDTH = 15;
    
    /**
     * addThief reply int type, int id, int partypos, int room
     */
    public static final int ADDTHREP = 16;
    
    /**
     * getRoomDistance request int type, int room
     */
    public static final int GETDIST = 17;
    
    /**
     * getRoomDistance reply int type, int dist
     */
    public static final int GETDISTREP= 18;
    
    /**
     * crawlIn request int type, int id, int dist, int ap, int partypos
     */
    public static final int CRIN = 19;
   
    /**
     * rollACanvas request int type, int id, int room
     */
    public static final int ROLLCAN = 20;
    
    /**
     * rollACanvas reply int type, boolean result
     */
    public static final int ROLLCANREP = 21;

    /**
     * reverseDirection request int type, int id, int ap, int partypos
     */
    public static final int REVDIR = 22;

    /**
     * crawlOut request int type, int id, int ap, int dist, int partypos
     */
    public static final int CROUT = 23;


    /**
     * handACanvas request int type, int id, boolean canvas, int room
     */
    public static final int HNDCAN = 24;

    /**
     * Logfile initialization request int type, string fname
     */
    public static final int LOGINIT = 25;

    /**
     * setOrdinaryThiefState request int type, int id, int state
     */
    public static final int SETOTSTT = 26;

    /**
     * setOrdinaryThiefPartyState request int type, int id, char state
     */
    public static final int SETOTPSTT= 27;

    /**
     * setOrdinaryThiefMD request int type, int id, int md
     */
    public static final int SETTHMD = 28;

    /**
     * setMasterThiefState request int type, int id, int state
     */
    public static final int SETMTHSTT= 29;

    /**
     * setAssaultPartyRoom request int type, int ap, int room
     */
    public static final int SETAPRM = 39;

     /**
      * Addthief to assault party request int type, int ap, int ppos
      */
    public static final int ADDTHAP = 30;

    /**
     * removeThiefFromAssaultParty request int type, int id, int ap
     */
    public static final int REMTHAP = 31;

    /**
     * setThiefPosition request int type, int id, int pos
     */
    public static final int SETTHPOS= 32;

    /**
     * setThiefCanvas request int type, int id, boolean canvas
     */
    public static final int SETTHCAN= 33;

    /**
     * setNumPaintingsInRoom request int type, int room, int numpaint
     */
    public static final int SETPNTSRM = 34;

    /**
     * setRoomDistance request int type, int room, int roomdist
     */
    public static final int SETRMDIS = 35;

    /**
     * setRoomDistanceAndPaintings request int type, int room, int dist, int numpaint
     */
    public static final int SETRDISTPNTS = 36;

    /**
     * finalResult request int type
     */
    public static final int FINRES = 37;

    /**
     * Generic void Acknowledge int type,
     */
    public static final int ACK = 38;
}
