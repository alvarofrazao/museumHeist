package clientSide.stubs;

import genclass.GenericIO;
import clientSide.entities.mClient;
import clientSide.entities.oClient;
import infrastructure.ClientCom;
import infrastructure.Message;
import infrastructure.MessageType;

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

    public void setupParty(int room){

        ClientCom com;

        Message outMessage, inMessage;
        mClient curThread = (mClient) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.SETP,room);
        com.writeObject(outMessage);

        inMessage = (Message)com.readObject();

        if(inMessage.getMsgType() != MessageType.SETPREP){
            GenericIO.writelnString("Thread " + curThread.getID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    public int addThief(){

        ClientCom com;

        Message outMessage, inMessage;
        
        oClient curThread = (oClient) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.ADDTH,curThread.getThiefID());
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
        
        oClient curThread = (oClient) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.CRIN,curThread.getThiefID(),dist,curThread.getPartyPos());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.CRINREP){
            GenericIO.writelnString("Thread " + curThread.getThiefID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    public void crawlOut(int dist){

        ClientCom com;

        Message outMessage, inMessage;
        
        oClient curThread = (oClient) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.CROUT,curThread.getThiefID(),dist,curThread.getPartyPos());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.CROUTREP){
            GenericIO.writelnString("Thread " + curThread.getThiefID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    public void reverseDirection(){

        ClientCom com;

        Message outMessage, inMessage;
        
        oClient curThread = (oClient) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.REVDIR,curThread.getThiefID(),curThread.getPartyPos());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.REVDIREP){
            GenericIO.writelnString("Thread " + curThread.getThiefID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    public void signalDeparture(){

        ClientCom com;

        Message outMessage, inMessage;
        mClient curThread = (mClient) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.SIGNDEP);
        com.writeObject(outMessage);

        inMessage = (Message)com.readObject();

        if(inMessage.getMsgType() != MessageType.SIGNDEPREP){
            GenericIO.writelnString("Thread " + curThread.getID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    public void shutdown(){
        ClientCom com;

        Message outMessage, inMessage;
        mClient curThread = (mClient) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.SHUTDOWN);
        com.writeObject(outMessage);

        inMessage = (Message)com.readObject();

        if(inMessage.getMsgType() != MessageType.SHUTDONE){
            GenericIO.writelnString("Thread " + curThread.getID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }
}
