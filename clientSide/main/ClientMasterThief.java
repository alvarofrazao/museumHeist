package clientSide.main;

import genclass.GenericIO;
import clientSide.entities.*;
import clientSide.stubs.AssaultPartyStub;
import clientSide.stubs.ConcentrationSiteStub;
import clientSide.stubs.ControlCollectionSiteStub;
import clientSide.stubs.MuseumStub;
import infrastructure.*;

public class ClientMasterThief {
    
    
    public static void main(String[] args){

        AssaultPartyStub[] aPStubs = new AssaultPartyStub[2];
        aPStubs[0] = new AssaultPartyStub(ExecParameters.aP1HostName, ExecParameters.aP1PortNum);
        aPStubs[1] = new AssaultPartyStub(ExecParameters.aP2HostName, ExecParameters.aP2PortNum);
        MuseumStub museumStub = new MuseumStub(ExecParameters.museumHostName, ExecParameters.museumPortNum);
        ConcentrationSiteStub csStub =  new ConcentrationSiteStub(ExecParameters.ccsHostName,ExecParameters.ccsPortNum);
        ControlCollectionSiteStub cclStub = new ControlCollectionSiteStub(ExecParameters.cclHostName, ExecParameters.cclPortNum);

        mClient mThief = new mClient(aPStubs, cclStub, csStub, 6);

        mThief.start();
        GenericIO.writelnString ();

        try {
            mThief.join();
        } catch (Exception e) {}

        aPStubs[0].shutdown();
        aPStubs[1].shutdown();
        csStub.shutdown();
        museumStub.shutdown();
        cclStub.shutdown();
        GenericIO.writelnString("mClient successfully terminated");
    }
}
