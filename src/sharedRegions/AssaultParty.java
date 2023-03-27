package src.sharedRegions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import src.entities.oStates;
import src.entities.oThief;

public class AssaultParty {

    /**
     * Main monitor object
     */
    private ReentrantLock lock;

    /**
     * Condition variable used before the ingoing movement
     */
    private Condition cond;

    /**
     * Condition variable used before and during the outgoing momevement
     */
    private Condition reverseCond;

    /**
     * Condition variable used for the party formation phase
     */
    private Condition setupCond;
    
    /**
     * Reference to the Museum shared region
     */
    private Museum museum;


    /**
     * Reference to the generalRepository shared region
     */
    private final GeneralRepos repos;

    /**
     * Index of the room currently assigned to the party
     */
    private int currentRoomID;

    /**
     * Array storing the current distance of each thief to the Collection Site
     */
    private int[] thiefDist;

    /**
     * Number of thieves currently in the party. 
     */
    private int currentThiefNum;

    /**
     * Number of thieves that have arrived at the movement target (either Museum room or Collection Site)
     */
    private int hasArrived;

    /**
     * Maximum separation between two consecutive thieves
     */
    private int S;

    /**
     * Index of the party in the party array
     */
    private int id;

    /**
     * Party status flag. True means the thieves are currently between the CRAWLING_INWARDS and COLLECTION_SITE states
     */
    private boolean isRunning;

    /***
     * Instantiation of AssaultParty object
     * @param id Party id in the AssaultParty array: either 0 or 1
     * @param partySize Number of thieves in a single party
     * @param S Maximum distance between to consecutive thieves
     * @param museum Reference to the Museum shared memory region 
     * @param repos Reference to the GeneralRepository shared memory region
     */
    public AssaultParty(int id, int partySize, int thiefMax, int S, Museum museum, GeneralRepos repos) {
        this.id = id;
        this.lock = new ReentrantLock();
        this.cond = lock.newCondition();
        this.reverseCond = lock.newCondition();
        this.setupCond = lock.newCondition();
        this.thiefDist = new int[partySize];
        this.museum = museum;
        this.repos = repos;
        this.currentThiefNum = 0;
        this.hasArrived = 0;
        this.S = S;
        this.isRunning = false;
    }


    
    public boolean getStatus() {
        return isRunning;
    }

    public int getRoomID() {
        return currentRoomID;
    }


    /***
     * Setups up AssaultParty variables for party formation
     * @param roomID index of the room assigned to the party
     */
    public void setupParty(int roomID) {
        lock.lock();
        currentThiefNum = 0;
        hasArrived = 0;
        isRunning = false;
        currentRoomID = roomID;
        while(currentThiefNum < 3){
            setupCond.signalAll();
            lock.unlock();
            lock.lock();
        }
        lock.unlock();
    }


    /***
     * Assigns thief to party, and blocks waiting for departure signal.
     * @return Room index that was assigned to the party
     * @throws InterruptedException
     */
    public int addThief() throws InterruptedException {
        lock.lock();
        setupCond.await();
        oThief curThread = (oThief) Thread.currentThread();
        curThread.setPartyPos(currentThiefNum);
        repos.addThiefToAssaultParty(curThread.getThiefID(), this.id,currentThiefNum);
        thiefDist[currentThiefNum++] = 0;
        cond.await();
        repos.setOrdinaryThiefState(curThread.getThiefID(), oStates.CRAWLING_INWARDS);
        lock.unlock();
        return currentRoomID;
    }


    /***
     * Method for the ingoing movement, each thief moves until it is S units away from the one behind them, then stops and signals another thief to move,
     * repeating the process until all three arrive at the Museum Room
     * @throws InterruptedException
     */
    public void crawlIn() throws InterruptedException {
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        int move = 1;
        int curIdx = curThread.getPartyPos();
        int behindDist;
        int nextPos = 0;
        int roomDist = museum.getRoomDistance(curThread.getCurRoom());
        boolean canMove = true;

        for (; move <= roomDist; move++) {
            behindDist = -1;

            if(!canMove){
                move--;
                thiefDist[curIdx] += move;
                repos.setThiefPosition(curThread.getCurAP(), curThread.getThiefID(), thiefDist[curIdx]);
                cond.signal();
                cond.await();
                canMove = true;
                curIdx = curThread.getPartyPos();
                behindDist = thiefDist[curIdx];
                move = 1;
            }

            nextPos = move + thiefDist[curIdx];


            //Arrival detection
            if (nextPos >= roomDist) {
                thiefDist[curIdx] = roomDist;
                repos.setThiefPosition(curThread.getCurAP(), curThread.getThiefID(), roomDist);
                repos.setOrdinaryThiefState(curThread.getThiefID(), oStates.AT_A_ROOM);
                hasArrived += 1;
                cond.signal();
                lock.unlock();
                break;
            }

            //Computing the index of the thief that is behind the current Thread
            for(int i = 0;i < 3; i++){
                if(thiefDist[i] < thiefDist[curIdx]){
                    if(thiefDist[i] > behindDist){
                        behindDist = thiefDist[i];
                    }
                }
            }

            //matching distance detection
            for(int i = 0; i < 3; i++){
                if(nextPos == thiefDist[i]){
                    move++;
                    break;
                }
            }

            //Movement iteration stopping condition
            if(nextPos > (behindDist + S)){
                canMove = false;
            }
        }
        return;
    }

    /***
     * Method for the outgoing movement, each thief moves until it is S units away from the one behind them, then stops and signals another thief to move,
     * repeating the process until all three arrive at the Collection Site
     * @throws InterruptedException
     */

    public void crawlOut() throws InterruptedException{
        lock.lock();
        oThief curThread = (oThief) Thread.currentThread();
        int move = 1;
        int curIdx = curThread.getPartyPos();
        int behindDist;
        int nextPos = 0;
        int roomDist = museum.getRoomDistance(curThread.getCurRoom());
        boolean canMove = true;

        for (; move <= roomDist; move++) {
            behindDist = roomDist + 1;

            if(!canMove){
                move--;
                thiefDist[curIdx] -= move;
                repos.setThiefPosition(curThread.getCurAP(), curThread.getThiefID(), thiefDist[curIdx]);
                reverseCond.signal();
                reverseCond.await();
                canMove = true;
                curThread = (oThief) Thread.currentThread();
                curIdx = curThread.getPartyPos();
                behindDist = roomDist;
                move = 1;
            }

            nextPos = thiefDist[curIdx] - move;

            //Arrival detection
            if (nextPos <= 0) {
                thiefDist[curIdx] = 0;
                repos.setThiefPosition(curThread.getCurAP(), curThread.getThiefID(), 0);
                repos.setOrdinaryThiefState(curThread.getThiefID(), oStates.COLLECTION_SITE);
                hasArrived++;
                if(hasArrived == 3){
                    isRunning = false;
                }
                reverseCond.signal();
                lock.unlock();
                break;
            }

            //Computing the index of the thief that is behind the current Thread
            for(int i = 0;i < 3; i++){
                if(thiefDist[i] > thiefDist[curIdx]){
                    if(thiefDist[i] < behindDist){
                        behindDist = thiefDist[i];
                    }
                }
            }


            //matching distance detection
            for(int i = 0; i < 3; i++){
                if(nextPos == thiefDist[i]){
                    move++;
                    break;
                }
            }

            //Movement iteration stopping condition
            if(nextPos < (behindDist -S )){
                canMove = false;
            }
        }
        return;
    }
    
    
    /***
     * Method for preparing and initiating the outgoing movement: 
     * the last thread to call the method signals one other thread waiting on the condition to initiate the movement
     * @throws InterruptedException
     */

    public void reverseDirection() throws InterruptedException {
        lock.lock();
        hasArrived--;
        if(hasArrived > 0){
            reverseCond.await();
            repos.setOrdinaryThiefState(((oThief) Thread.currentThread()).getThiefID(), oStates.CRAWLING_OUTWARDS);
            lock.unlock();
            return;
        }else{
            reverseCond.signal();
            reverseCond.await();
            repos.setOrdinaryThiefState(((oThief) Thread.currentThread()).getThiefID(), oStates.CRAWLING_OUTWARDS);
            lock.unlock();
            return;
        }
    }


    /***
     * Method that signals the start of the party's ingoing movement: the Master signals a single thread to start moving 
     * @throws InterruptedException
     */

    public void signalDeparture() throws InterruptedException {
        lock.lock();
        isRunning = true;
        cond.signal();
        lock.unlock();
        return;
    }
}
