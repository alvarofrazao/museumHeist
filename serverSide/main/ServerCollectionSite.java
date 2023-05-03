package serverSide.main;

import serverSide.sharedRegions.*;
import serverSide.entities.*;
import serverSide.stubs.GeneralReposStub;
import serverSide.infrastructure.*;

import java.net.SocketTimeoutException;

import genclass.GenericIO;

public class ServerCollectionSite {
    
    /**
     * 
     */
    public static boolean waitConnection;

    public static void main(String[] args){

        ServerCom scon, sconi;
        GeneralReposStub gReposStub = new GeneralReposStub(ExecParameters.gReposHostName, ExecParameters.gReposPortNum);
        int portNumb = ExecParameters.cclPortNum;
        ControlCollectionSite ccl = new ControlCollectionSite(gReposStub,5,6);
        ControlCollectionSiteInterface cclInter = new ControlCollectionSiteInterface(ccl);

        scon = new ServerCom(portNumb);
        scon.start();
        GenericIO.writelnString ("Service is established!");
        GenericIO.writelnString ("Server is listening for service requests.");

        cclClientProxy clientProxy;

        waitConnection = true;
        while(waitConnection){
            try{ 
                sconi = scon.accept ();                                    // enter listening procedure
                clientProxy = new cclClientProxy (sconi, cclInter);    // start a service provider agent to address
                clientProxy.start ();                                         //   the request of service
            }
            catch (SocketTimeoutException e) {}
        }
        scon.end ();
        GenericIO.writelnString ("Server was shutdown.");
    }
}
