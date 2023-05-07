package infrastructure;

public class MessageType {

    /**
     * amINeeded request: int type, int id, boolean fc, int ap ////////
     */
    public static final int AIN = 1;

    /**
     * amINeeded reply: int type, boolean fc, boolean result \\\\\\\\\\
     */
    public static final int AINREP = 2;

    /**
     * prepareAssaultParty request: int type, int id
     */
    public static final int PREPAP = 3;

    /**
     * prepareAssaultParty reply: int type, int ap-----------
     */
    public static final int PREPAPREP = 4;

    /**
     * takeARest request: int type, int id
     */
    public static final int TKREST = 5;

    /**
     * takeARestreply: int type
     */
    public static final int TKRESTREP = 6;

    /**
     * collectACanvas request: int type, int id
     */
    public static final int COLCAN = 7;

    /**
     * collectACanvas reply: int type
     */
    public static final int COLCANREP = 8;

    /**
     * HNDCAN request: int type, int id, int ap, int room, boolean hasPainting &&&&&&
     */
    public static final int HNDCAN = 9;

    /**
     * HNDCAN reply: int type
     */
    public static final int HNDCANREP = 10;

    /**
     * appraiseSit request: int type, int id
     */
    public static final int APSIT = 11;

    /**
     * appraiseSit reply: int type, int ri1--------------
     */
    public static final int APSITREP = 12;

    /**
     * startOperations request: int type
     */
    public static final int STARTOP = 13;

    /**
     * startOperations reply: int type
     */
    public static final int STARTOPREP = 14;

    /**
     * sumUpResults request: int type
     */
    public static final int SUMRES = 15;

    /**
     * sumUpResults reply: int type
     */
    public static final int SUMRESREP = 16;

    /**
     * getNextRoom request: int type
     */
    public static final int GETNRM = 17;

    /**
     * getNextRoom reply: int type, int room--------------
     */
    public static final int GETNRMREP = 18;

    /**
     * sendAssaultParty request: int type, int id
     */
    public static final int SNDPTY = 19;

    /**
     * sendAssaultParty reply: int type
     */
    public static final int SNDPTYREP = 20;

    /**
     * prepareExcursion request: int type, int id
     */
    public static final int PREPEX = 21;

    /**
     * prepareExcursion reply: int type, int ap-------------
     */
    public static final int PREPEXREP = 22;

    /**
     * setupParty request: int type, int room----------------
     */
    public static final int SETP = 23;

    /**
     * setupParty reply: int type
     */
    public static final int SETPREP = 24;

    /**
     * addThief request: int type, int id
     */
    public static final int ADDTH = 25;

    /**
     * addThief reply: int type, int ppos, int room++++++
     */
    public static final int ADDTHREP = 26;

    /**
     * crawlIn request: int type, int id, int roomDist, int ppos ?????
     */
    public static final int CRIN = 27;

    /**
     * crawlIn reply: int type
     */
    public static final int CRINREP = 28;

    /**
     * crawlOut request: int type, int id, int roomDist, int ppos ??????
     */
    public static final int CROUT = 29;

    /**
     * crawlOut reply: int type
     */
    public static final int CROUTREP = 30;

    /**
     * reverseDirection request: int type, int id, int ppos+++++++++
     */
    public static final int REVDIR = 31;

    /**
     * reverseDirection reply: int type
     */
    public static final int REVDIRREP = 32;

    /**
     * signalDeparture request: int type
     */
    public static final int SIGNDEP = 33;

    /**
     * signalDeparture reply: int type
     */
    public static final int SIGNDEPREP = 34;

    /**
     * getDistance request: int type, int id
     */
    public static final int GETDIST = 35;

    /**
     * getDistance reply: int type, int ri1(dist)---------------
     */
    public static final int GETDISTREP = 36;

    /**
     * rollCanvas request: int type, int id, int ap, int room ??????
     */
    public static final int ROLLCAN =37;

    /**
     * rollCanvas reply: int type boolean rb(hasCanvas) !!!!!
     */
    public static final int ROLLCANREP = 38;

    /**
     * request: int type
     */
    public static final int LOGINIT = 39;

    /**
     * request: int type, int id, int state++++++++++++++
     */
    public static final int SETOTSTT = 41;

    /**
     * request: int type, int id, char pstate $$$$$$
     */
    public static final int SETOTPSTT =43;

    /**
     * request: int type, int id, int md+++++++++
     */
    public static final int SETTHMD =45;

    /**
     * request: int type, int id, in state+++++++++++++
     */
    public static final int SETMTHSTT =47;

    /**
     * request: int type, int ap, int room++++++++++++++
     */
    public static final int SETAPRM =48;

    /**
     * request: int type, int id, in ap, int ppos ??????
     */
    public static final int ADDTHAP =49;

    /**
     * request: int type, int id, int ap+++++++++++++
     */
    public static final int REMTHAP =50;

    /**
     * request: int type, int id, int ap, int pos ??????
     */
    public static final int SETTHPOS =51;

    /**
     * request: int type, int id, int ap, int canvas ??????
     */
    public static final int SETTHCAN =52;

    /**
     * request: int type, int id, int dist, int ri1(num_pnts) ??????
     */
    public static final int SETRDISTPNTS=53;

    /**
     * request: int type, int id, in num_pnts+++++++++++++
     */
    public static final int SETPNTSRM =54;

    /**
     * request: int type, int total_pnts------------
     */
    public static final int FINRES =55;

    /**
     * generalRepos acknowledge
     */
    public static final int ACK = 56;


    /**
     * Shutdown Message type
     */
    public static final int SHUTDOWN = 99;

    /**
     * Shutdown reply type
     */
    public static final int SHUTDONE = 100;
}
