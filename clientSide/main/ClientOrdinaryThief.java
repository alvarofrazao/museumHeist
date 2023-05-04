package clientSide.main;

import clientSide.stubs.AssaultPartyStub;
import clientSide.stubs.ConcentrationSiteStub;
import clientSide.stubs.ControlCollectionSiteStub;
import clientSide.stubs.MuseumStub;
import infrastructure.ExecParameters;
import genclass.GenericIO;
import clientSide.entities.oClient;

public class ClientOrdinaryThief {
    
    

    public static void main(String[] args){
        /* String aPServerHostName = ExecParameters.aPHostName;
        int aPPortNum = ExecParameters.aPPortNum;
        String ccsServerHostName = ExecParameters.ccsHostName;
        int ccsPortNum = ExecParameters.ccsPortNum;
        String cclerverHostName = ExecParameters.cclHostName;
        int cclPortNum = ExecParameters.cclPortNum;
        String museumServerHostName = ExecParameters.museumHostName;
        int museumPortNum = ExecParameters.museumPortNum; */
    
        AssaultPartyStub[] aPStubs = new AssaultPartyStub[2];
        aPStubs[0] = new AssaultPartyStub(ExecParameters.aP1HostName, ExecParameters.aP1PortNum);
        aPStubs[1] = new AssaultPartyStub(ExecParameters.aP2HostName, ExecParameters.aP2PortNum);
        ConcentrationSiteStub csStub =  new ConcentrationSiteStub(ExecParameters.ccsHostName,ExecParameters.ccsPortNum);
        ControlCollectionSiteStub cclStub = new ControlCollectionSiteStub(ExecParameters.cclHostName, ExecParameters.cclPortNum);
        MuseumStub museumStub =  new MuseumStub(ExecParameters.museumHostName, ExecParameters.museumPortNum);

        oClient[] oThieves = new oClient[6];

        for(int i = 0; i < 6; i++){
            oThieves[i] = new oClient(i, aPStubs, cclStub, csStub, museumStub, 6, 2);
        }
        
        for(int i = 0; i < 6; i++){
            oThieves[i].start();
        }

        GenericIO.writelnString ();
        for(int i = 0; i < 6; i++){
            try{
                oThieves[i].join();
            }catch(InterruptedException e){}
            GenericIO.writelnString("oThief client " + (i+1) + " has terminated successfully");
        }
        GenericIO.writelnString ();
        GenericIO.writelnString("All clients successfully terminated");
    }
}
