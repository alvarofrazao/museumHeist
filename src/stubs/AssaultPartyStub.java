package src.stubs;

import genclass.GenericIO;
import src.entities.mThief;
import src.entities.oThief;
import src.infrastructure.ClientCom;
import src.infrastructure.Message;
import src.infrastructure.MessageType;

public class AssaultPartyStub {
    
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
    public AssaultPartyStub(String serverHostname, int serverPortNum){
        this.serverHostName = serverHostname;
        this.serverPortNum = serverPortNum;
    }

    public void setupParty(){

        ClientCom com;

        Message outMessage, inMessage;
        mThief curThread = (mThief) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.PREPAP,curThread.getID());
        com.writeObject(outMessage);

        inMessage = (Message)com.readObject();

        if(inMessage.getMsgType() != MessageType.PREPAPREP){
            GenericIO.writelnString("Thread " + curThread.getID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getThId() != curThread.getID()){
            GenericIO.writelnString ("Thread " + curThread.getID () + ": Invalid barber id!");
           GenericIO.writelnString (inMessage.toString ());
           System.exit (1);
        }

        com.close();
    }

    public int addThief(){

        ClientCom com;

        Message outMessage, inMessage;
        
        oThief curThread = (oThief) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.ADDTH,curThread.getThiefID(),curThread.getCurAP());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.ADDTHREP){
            GenericIO.writelnString("Thread " + curThread.getThiefID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
        curThread.setPartyPos(inMessage.getRI1());
        return inMessage.getRI2();
    }

    public void crawlIn(int dist){

        ClientCom com;

        Message outMessage, inMessage;
        
        oThief curThread = (oThief) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.CRIN,curThread.getThiefID(),dist,curThread.getCurAP(),curThread.getPartyPos());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.ACK){
            GenericIO.writelnString("Thread " + curThread.getThiefID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    public void crawlOut(int dist){

        ClientCom com;

        Message outMessage, inMessage;
        
        oThief curThread = (oThief) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.CROUT,curThread.getThiefID(),dist,curThread.getCurAP(),curThread.getPartyPos());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.AINREP){
            GenericIO.writelnString("Thread " + curThread.getThiefID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    public void reverseDirection(){

        ClientCom com;

        Message outMessage, inMessage;
        
        oThief curThread = (oThief) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.REVDIR,curThread.getThiefID(),curThread.getPartyPos(),curThread.getCurRoom());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.ACK){
            GenericIO.writelnString("Thread " + curThread.getThiefID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    public void signalDeparture(){

        ClientCom com;

        Message outMessage, inMessage;
        mThief curThread = (mThief) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.SIGNDEP,curThread.getID());
        com.writeObject(outMessage);

        inMessage = (Message)com.readObject();

        if(inMessage.getMsgType() != MessageType.ACK){
            GenericIO.writelnString("Thread " + curThread.getID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }
}
