package src.infrastructure;

public class MessageType {

    /**
     * Start Operations request
     */
    public static final int STARTOP = 1;
    
    /**
     * Start Operations reply
     */
    public static final int STARTOPREP= 2;
    

    /**
     * Appraise Situation request
     */
    public static final int APSIT= 3;

    /**
     * Appraise Situation reply
     */
    public static final int APSITREP= 4;

    /**
     * Prepare Assault PArty request
     */
    public static final int PREPAP= 5;

    /**
     * Prepare Assault PArty reply
     */
    public static final int PREPAPREP = 6;

    /**
     * Setup Party request
     */
    public static final int SETP= 7;

    /**
     * Setup Party reply 
     */
    public static final int SETPREP = 8;

    /**
     * Send Assault party request
     */
    public static final int SNDPTY = 9;
    
    /**
     * Send Assault party reply
     */
    public static final int SNDPTYREP = 10;
    
    /**
     * Signal departure request
     */
    public static final int SIGNDEP= 11;
    
    /**
     * Signal departure reply
     */
    public static final int SIGNDEPREP = 12;
    
    /**
     * Take a rest request
     */
    public static final int TKREST= 13;
    
    /**
     * Take a rest reply
     */
    public static final int TKRESTREP = 14;
    
    /**
     * Collect a canvas request
     */
    public static final int COLCANV = 15;
    
    /**
     * Collect a canvas reply
     */
    public static final int COLCANVREP = 16;
    
    /**
     * Sum up results request
     */
    public static final int SUMRES = 17;
    
    /**
     * Sum up results reply
     */
    public static final int SUMRESREP = 18;
    
    /**
     * Am I needed request
     */
    public static final int AIN = 19;

    /**
     * Am I needed reply
     */
    public static final int AINREP = 20;
    
    /**
     * Prepare excursion request
     */
    public static final int PREPEX = 21;
    
    /**
     * Prepare excursion reply
     */
    public static final int PREPEXREP = 22;
    
    /**
     * addThief request
     */
    public static final int ADDTH = 23;
    
    /**
     * addThief reply
     */
    public static final int ADDTHREP = 24;
    
    /**
     * getRoomDistance request
     */
    public static final int GETDIST = 25;
    
    /**
     * getRoomDistance reply
     */
    public static final int GETDISTREP= 26;
    
    /**
     * crawlIn request
     */
    public static final int CRIN = 27;
    
    /**
     * crawlIn reply
     */
    public static final int CRINREP = 28;
    
    /**
     * rollACanvas request
     */
    public static final int ROLLCAN = 29;
    
    /**
     * rollACanvas reply
     */
    public static final int ROLLCANREP = 30;

    /**
     * reverseDirection request
     */
    public static final int REVDIR = 31;

    /**
     * reverseDirection reply
     */
    public static final int REVDIRREP = 32;

    /**
     * crawlOut request
     */
    public static final int CROUT = 33;

    /**
     * crawlOut reply
     */
    public static final int CROUTREP = 34;

    /**
     * handACanvas request
     */
    public static final int HNDCAN = 35;

    /**
     * handACanvas reply
     */
    public static final int HNDCANREP = 36;

    /**
     * Logfile initialization request
     */
    public static final int LOGINIT = 37;

    /**
     * Logfile initialization reply
     */
    public static final int LOGINITREP = 38;

    /**
     * setOrdinaryThiefState request
     */
    public static final int SETOTSTT = 39;

    /**
     * setOrdinaryThiefState reply
     */
    public static final int SETOTSTTREP = 40;

    /**
     * setOrdinaryThiefPartyState request
     */
    public static final int SETOTPSTT= 41;

    /**
     * setOrdinaryThiefPartyState reply
     */
    public static final int SETOTPSTTREP = 42;

    /**
     * setOrdinaryThiefMD request
     */
    public static final int SETTHMD = 43;

    /**
     * setOrdinaryThiefMD reply
     */
    public static final int SETTHMDREP = 44;

    /**
     * setMasterThiefState request
     */
    public static final int SETMTHSTT= 45;

    /**
     * setMasterThiefState reply
     */
    public static final int SETMTHSTTREP = 46;

    /**
     * setAssaultPartyRoom request
     */
    public static final int ADDTHAP = 47;

    /**
     * addThiefToAssaultParty reply
     */
    public static final int ADDTHAPREP = 48;

    /**
     * removeThiefFromAssaultParty request
     */
    public static final int REMTHAP = 49;

    /**
     * removeThiefFromAssaultParty reply
     */
    public static final int REMTHAPREP = 50;

    /**
     * setThiefPosition request
     */
    public static final int SETHTPOS= 51;

    /**
     * setThiefPosition reply
     */
    public static final int SETHTPOSREP = 52;

    /**
     * setThiefCanvas request
     */
    public static final int SETTHCAN= 53;

    /**
     * setThiefCanvas reply
     */
    public static final int SETTHCANREP = 54;

    /**
     * setNumPaintingsInRoom request
     */
    public static final int SETPNTSRM = 55;

    /**
     * setNumPaintingsInRoom reply
     */
    public static final int SETPNTSRMREP = 56;

    /**
     * setRoomDistance request
     */
    public static final int SETRMDIS = 57;

    /**
     * setRoomDistance reply
     */
    public static final int SETRMDISREP = 58;

    /**
     * setRoomDistanceAndPaintings request
     */
    public static final int SETRDISTPNTS = 59;

    /**
     * setRoomDistanceAndPaintings reply
     */
    public static final int SETRDISTPNTSREP= 60;

    /**
     * finalResult request
     */
    public static final int FINRES = 61;

    /**
     * finalResult reply
     */
    public static final int FINRESREP = 62;
}
