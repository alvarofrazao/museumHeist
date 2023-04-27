package src.stubs;

import src.entities.oThief;
import src.infrastructure.*;
import genclass.GenericIO;

public class ControlCollectionSiteStub {

    /**
     * Name of the machine where the ControlCollection server is housed
     */
    private String serverHostName;

    /**
     * Port Number for listening to service requests
     */
    private int serverPortNum;


    /**
     * Collection Site stub instantiation
     * @param serverHostname
     * @param serverPortNum
     */
    public ControlCollectionSiteStub(String serverHostname, int serverPortNum){
        this.serverHostName = serverHostname;
        this.serverPortNum = serverPortNum;
    }
    
    /**
     * 
     * @return
     */
    public boolean amINeeded(){

        ClientCom com;

        Message outMessage, inMessage;

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.AIN,((oThief)Thread.currentThread()).getThiefID(),((oThief)Thread.currentThread()).getThiefState());
    }

    /**
     * 
     * @return
     */
    public int prepareAssaultParty(){

        ClientCom com;

        Message outMessage, inMessage;

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }
    }
    
    /**
     * 
     */
    public void takeARest(){

        ClientCom com;

        Message outMessage, inMessage;

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }
    }

    /**
     * 
     */
    public void collectACanvas(){

        ClientCom com;

        Message outMessage, inMessage;

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }
    }

    /**
     * 
     */
    public void handACanvas(){

        ClientCom com;

        Message outMessage, inMessage;

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }
    }

    /**
     * 
     * @return
     */
    public int appraiseSit(){

        ClientCom com;

        Message outMessage, inMessage;

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }
    }

    /**
     * 
     */
    public void startOperations(){

        ClientCom com;

        Message outMessage, inMessage;

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }
    }

    /**
     * 
     */
    public void sumResults(){

        ClientCom com;

        Message outMessage, inMessage;

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }
    }
    
}
