package src.main;

import src.sharedRegions.*;
import src.entities.*;
import src.stubs.GeneralReposStub;
import src.infrastructure.*;
import genclass.GenericIO;

public class ServerCollectionSite {
    
    /**
     * 
     */
    public static boolean waitConnection;


    /**
     * Main method
     * @param args runtime arguments
     *      args[0] - port nunber for listening to service requests
     */
    public static void main(String[] args){

        ControlCollectionSite ccl;
        ControlCollectionSiteInterface cclInter;
        ServerCom scon, sconi;
        int portNumb = -1;
    }
}
