package src.stubs;

import src.entities.*;
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
        
        oThief curThread = (oThief) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.AIN,curThread.getThiefID(),curThread.isFirstCycle(),curThread.getCurSit());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.AINREP){
            GenericIO.writelnString("Thread " + curThread.getThiefID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
        curThread.setFirstCycle(inMessage.getoThFC());
        return inMessage.getRB();
    }

    /**
     * 
     * @return
     */
    public int prepareAssaultParty(){

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
        return outMessage.getoThAP();
    }
    
    /**
     * 
     */
    public void takeARest(){

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

        outMessage = new Message(MessageType.TKREST,curThread.getID());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.ACK){
            GenericIO.writelnString("Thread " + curThread.getID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * 
     */
    public void collectACanvas(){

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

        outMessage = new Message(MessageType.COLCANV,curThread.getID());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.ACK){
            GenericIO.writelnString("Thread " + curThread.getID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * 
     */
    public void handACanvas(){

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
        outMessage = new Message(MessageType.HNDCAN,curThread.getThiefID(),curThread.hasPainting(),curThread.getCurRoom());
        com.writeObject(outMessage);

        inMessage= (Message) com.readObject();
        if(inMessage.getMsgType() != MessageType.ACK){
            GenericIO.writelnString("Thread " + curThread.getThiefID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * 
     * @return
     */
    public int appraiseSit(){

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

        outMessage = new Message(MessageType.APSIT);
        com.writeObject(outMessage);

        inMessage= (Message) com.readObject();
        if(inMessage.getMsgType() != MessageType.APSITREP){
            GenericIO.writelnString("Thread " + curThread.getID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
        return inMessage.getRI1();
    }

    /**
     * 
     */
    public void startOperations(){

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

        outMessage = new Message(MessageType.STARTOP,curThread.getID());
        com.writeObject(outMessage);

        inMessage= (Message) com.readObject();
        if(inMessage.getMsgType() != MessageType.ACK){
            GenericIO.writelnString("Thread " + curThread.getID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * 
     */
    public void sumResults(){

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

        outMessage = new Message(MessageType.SUMRES,curThread.getID());
        com.writeObject(outMessage);

        inMessage= (Message) com.readObject();
        if(inMessage.getMsgType() != MessageType.ACK){
            GenericIO.writelnString("Thread " + curThread.getID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }
 
}
