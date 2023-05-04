package serverSide.main;

import java.net.SocketTimeoutException;

import genclass.GenericIO;
import serverSide.entities.aPClientProxy;
import infrastructure.ExecParameters;
import infrastructure.ServerCom;
import serverSide.sharedRegions.AssaultParty;
import serverSide.sharedRegions.AssaultPartyInterface;
import serverSide.stubs.GeneralReposStub;

public class ServerAssaultParty {
    
    public static boolean waitConnection;

    public static void main(String[] args){
        AssaultParty aP;
        int id = -1;
        AssaultPartyInterface apInter;
        GeneralReposStub gReposStub;
        ServerCom scon,sconi;
        int portNum = -1;

        if(args.length != 1){
            GenericIO.writelnString ("Wrong number of parameters!");
            System.exit (1);
        }

        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString ("args[0] is not a number!");
            System.exit (1);
        }

        if((id < 0) || (id >= 2)){
            GenericIO.writelnString ("args[0] is not a valid AssaultParty ID");
            System.exit(1);
        }

        if(id == 0){
            portNum = ExecParameters.aP1PortNum;
        }
        else if(id == 1){
            portNum = ExecParameters.aP2PortNum;
        }
        else{
            GenericIO.writelnString ("Failed to correctly assign port number");
            System.exit(1);
        }

        /*Service started correctly */
        gReposStub = new GeneralReposStub(ExecParameters.gReposHostName, ExecParameters.gReposPortNum);
        
        aP = new AssaultParty(id, 3,3,3, gReposStub);
        apInter = new AssaultPartyInterface(aP);
        scon = new ServerCom(portNum);
        scon.start();

        GenericIO.writelnString ("Service is established!");
        GenericIO.writelnString ("Server is listening for service requests.");

        aPClientProxy clientProxy;

        waitConnection = true;
        while(waitConnection){
            try{
                sconi = scon.accept();
                clientProxy = new aPClientProxy(sconi, apInter);
                clientProxy.start();
            }catch(SocketTimeoutException e){}
        }
        scon.end();
        GenericIO.writelnString ("Server was shutdown.");
    }
}
