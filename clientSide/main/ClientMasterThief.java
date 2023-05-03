package clientSide.main;

import genclass.GenericIO;
import clientSide.entities.*;
import clientSide.stubs.AssaultPartyStub;
import clientSide.stubs.ConcentrationSiteStub;
import clientSide.stubs.ControlCollectionSiteStub;
import clientSide.infrastructure.*;

public class ClientMasterThief {
    
    
    public static void main(String[] args){

        AssaultPartyStub[] aPStubs = new AssaultPartyStub[2];
        aPStubs[0] = new AssaultPartyStub(ExecParameters.aP1HostName, ExecParameters.aP1PortNum);
        aPStubs[1] = new AssaultPartyStub(ExecParameters.aP2HostName, ExecParameters.aP2PortNum);
        ConcentrationSiteStub csStub =  new ConcentrationSiteStub(ExecParameters.ccsHostName,ExecParameters.ccsPortNum);
        ControlCollectionSiteStub cclStub = new ControlCollectionSiteStub(ExecParameters.cclHostName, ExecParameters.cclPortNum);

        mClient mThief = new mClient(aPStubs, cclStub, csStub, 6);

        mThief.start();
        GenericIO.writelnString ();

        try {
            mThief.join();
        } catch (Exception e) {}

        GenericIO.writelnString("mClient successfully terminated");
    }
}
