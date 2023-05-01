package src.infrastructure;

public class MessageType {

    /**
     * Start Operations request int type
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
     * Setup Party request int type, int roomid
     */
    public static final int SETP= 6;

    /**
     * Send Assault party request int type, int id
     */
    public static final int SNDPTY = 7;
      
    /**
     * Signal departure request int type
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
     * Sum up results request int type
     */
    public static final int SUMRES = 11;
    
    /**
     * Am I needed request int type, int id, boolean fc, int ap
     */
    public static final int AIN = 12;

    /**
     * Am I needed reply int type, boolean result, boolean fc
     */
    public static final int AINREP = 13;
    
    /**
     * Prepare excursion request int type, int id
     */
    public static final int PREPEX = 14;
    
    /**
     * Prepare excursion reply int type, int nextparty
     */
    public static final int PREPEXREP = 15;
    
    /**
     * addThief request int type, int id
     */
    public static final int ADDTH = 16;
    
    /**
     * addThief reply int type, int id, int partypos, int room
     */
    public static final int ADDTHREP = 17;
    
    /**
     * getRoomDistance request int type, int room
     */
    public static final int GETDIST = 18;
    
    /**
     * getRoomDistance reply int type, int dist
     */
    public static final int GETDISTREP= 19;
    
    /**
     * crawlIn request int type, int id, int dist, (int ap), int partypos
     */
    public static final int CRIN = 20;
   
    /**
     * rollACanvas request int type, int id, int ap, int room
     */
    public static final int ROLLCAN = 21;
    
    /**
     * rollACanvas reply int type, boolean result
     */
    public static final int ROLLCANREP = 22;

    /**
     * reverseDirection request int type, int id, int partypos
     */
    public static final int REVDIR = 23;

    /**
     * crawlOut request int type, int id, (int ap), int dist, int partypos
     */
    public static final int CROUT = 24;

    /**
     * handACanvas request int type, int id, int ap, int room, boolean canvas
     */
    public static final int HNDCAN = 25;

    /**
     * Logfile initialization request int type, string fname
     */
    public static final int LOGINIT = 26;

    /**
     * setOrdinaryThiefState request int type, int id, int state
     */
    public static final int SETOTSTT = 27;

    /**
     * setOrdinaryThiefPartyState request int type, int id, char state
     */
    public static final int SETOTPSTT= 28;

    /**
     * setOrdinaryThiefMD request int type, int id, int md
     */
    public static final int SETTHMD = 29;

    /**
     * setMasterThiefState request int type, int id, int state
     */
    public static final int SETMTHSTT= 30;

    /**
     * setAssaultPartyRoom request int type, int ap, int room
     */
    public static final int SETAPRM = 31;

     /**
      * Addthief to assault party request int type, int id, int ap, int ppos
      */
    public static final int ADDTHAP = 32;

    /**
     * removeThiefFromAssaultParty request int type, int id, int ap
     */
    public static final int REMTHAP = 33;

    /**
     * setThiefPosition request int type, int id, int ap, int pos
     */
    public static final int SETTHPOS= 34;

    /**
     * setThiefCanvas request int type, int id, int ap, int canvas
     */
    public static final int SETTHCAN= 35;

    /**
     * setNumPaintingsInRoom request int type, int room, int numpaint
     */
    public static final int SETPNTSRM = 36;

    /**
     * setRoomDistanceAndPaintings request int type, int room, int dist, int numpaint
     */
    public static final int SETRDISTPNTS = 38;

    /**
     * finalResult request int type
     */
    public static final int FINRES = 39;

    /**
     * Generic void Acknowledge int type,
     */
    public static final int ACK = 40;

    /**
     * handACanvas response int type
     */
    public static final int HNDCANREP = 41;

    /**
     * crawlIn response int type
     */
    public static final int CRINREP = 42;

    /**
     * crawlOut response int type
     */
    public static final int CROUTREP = 43;

    /**
     * reversideDirection response int type
     */
    public static final int REVDIREP = 44;

    /**
     * collectacanvas response int type
     */
    public static final int COLCANREP = 45;

    /**
     * startoperations response int type
     */
    public static final int STARTOPREP = 46;

    /**
     * sumresults response int type
     */
    public static final int SUMRESREP = 47;

    /**
     * takearest response int type
     */
    public static final int TKRESTREP = 48;

    /**
     * signalDeparture response int type
     */
    public static final int SIGNDEPREP = 49;

    /**
     * sendassaultparty response int type
     */
    public static final int SNDPTYREP = 50;

    /**
     * setupparty response int type, int id
     */
    public static final int SETPREP = 51;

    /**
     * getNextRoom request int type
     */
    public static final int GETNRM = 52;

    /**
     * getNextRoom reply int type, int room
     */
    public static final int GETNRMREP = 52;
}
