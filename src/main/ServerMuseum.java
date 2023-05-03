package src.main;

import java.net.SocketTimeoutException;

import genclass.GenericIO;
import src.entities.museumClientProxy;
import src.infrastructure.ExecParameters;
import src.infrastructure.ServerCom;
import src.sharedRegions.Museum;
import src.sharedRegions.MuseumInterface;
import src.stubs.GeneralReposStub;

public class ServerMuseum {
    
    /**
     * 
     */
    public static boolean waitConnection;

    public static void main(){

        ServerCom scon, sconi;
        GeneralReposStub gReposStub = new GeneralReposStub(ExecParameters.gReposHostName, ExecParameters.gReposPortNum);
        int portNumb = ExecParameters.ccsPortNum;
        Museum museum = new Museum(5, 30, 15, 16, 8,gReposStub);
        MuseumInterface mInter = new MuseumInterface(museum);

        scon = new ServerCom(portNumb);
        scon.start();
        GenericIO.writelnString ("Service is established!");
        GenericIO.writelnString ("Server is listening for service requests.");

        museumClientProxy clientProxy;

        waitConnection = true;
        while(waitConnection){
            try{ 
                sconi = scon.accept ();                                    
                clientProxy = new museumClientProxy (sconi, mInter);    
                clientProxy.start ();                                        
            }
            catch (SocketTimeoutException e) {}
        }
        scon.end ();
        GenericIO.writelnString ("Server was shutdown.");
    } 
}