package serverSide.entities;

/***
 * Definition of the state variables for the Master Thief Entity
 */

public class mStates {

    public static final int PLANNING_THE_HEIST = 0;
    public static final int DECIDING_WHAT_TO_DO = 1;
    public static final int ASSEMBLING_A_GROUP = 2;
    public static final int WAITING_FOR_GROUP_ARRIVAL = 3;
    public static final int PRESENTING_THE_REPORT = 4;

    private mStates() {

    }
}
