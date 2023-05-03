package serverSide.main;

import java.net.SocketTimeoutException;

import genclass.GenericIO;
import serverSide.entities.ccsClientProxy;
import serverSide.infrastructure.ExecParameters;
import serverSide.infrastructure.ServerCom;
import serverSide.sharedRegions.ConcentrationSite;
import serverSide.sharedRegions.ConcentrationSiteInterface;
import serverSide.stubs.GeneralReposStub;

public class ServerConcentrationSite {
    
    /**
     * 
     */
    public static boolean waitConnection;

    public static void main(String[] args){

        ServerCom scon, sconi;
        GeneralReposStub gReposStub = new GeneralReposStub(ExecParameters.gReposHostName, ExecParameters.gReposPortNum);
        int portNumb = ExecParameters.ccsPortNum;
        ConcentrationSite cs = new ConcentrationSite(gReposStub);
        ConcentrationSiteInterface csInter = new ConcentrationSiteInterface(cs);

        scon = new ServerCom(portNumb);
        scon.start();
        GenericIO.writelnString ("Service is established!");
        GenericIO.writelnString ("Server is listening for service requests.");

        ccsClientProxy clientProxy;

        waitConnection = true;
        while(waitConnection){
            try{ 
                sconi = scon.accept ();                                    
                clientProxy = new ccsClientProxy (sconi, csInter);    
                clientProxy.start ();                                        
            }
            catch (SocketTimeoutException e) {}
        }
        scon.end ();
        GenericIO.writelnString ("Server was shutdown.");
    } 
} 
