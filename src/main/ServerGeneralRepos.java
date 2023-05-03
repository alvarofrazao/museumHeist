package src.main;

import java.net.SocketTimeoutException;

import genclass.GenericIO;
import src.entities.GeneralReposClientProxy;
import src.infrastructure.ExecParameters;
import src.infrastructure.ServerCom;
import src.sharedRegions.GeneralRepos;
import src.sharedRegions.GeneralReposInterface;

public class ServerGeneralRepos {
    
    public static boolean waitConnection;

    public static void main(){

        ServerCom scon, sconi;
        int portNumb = ExecParameters.gReposPortNum;
        GeneralRepos gRepos = new GeneralRepos();
        GeneralReposInterface gReposInter = new GeneralReposInterface(gRepos);

        scon = new ServerCom(portNumb);
        scon.start();
        GenericIO.writelnString ("Service is established!");
        GenericIO.writelnString ("Server is listening for service requests.");

        GeneralReposClientProxy clientProxy;

        waitConnection = true;
        while(waitConnection){
            try{ 
                sconi = scon.accept ();                                    // enter listening procedure
                clientProxy = new GeneralReposClientProxy (sconi, gReposInter);    // start a service provider agent to address
                clientProxy.start ();                                         //   the request of service
            }
            catch (SocketTimeoutException e) {}
        }
        scon.end ();
        GenericIO.writelnString ("Server was shutdown.");
    }
}
