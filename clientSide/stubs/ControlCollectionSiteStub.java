package clientSide.stubs;

import clientSide.entities.*;
import infrastructure.*;
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
        
        oClient curThread = (oClient) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.AIN,curThread.getThiefID(),curThread.isFirstCycle(),curThread.getCurAP());
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
        mClient curThread = (mClient) Thread.currentThread();

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
            GenericIO.writelnString ("Thread " + curThread.getID () + ": Invalid Thief id!");
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
        mClient curThread = (mClient) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.TKREST, curThread.getID());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.TKRESTREP){
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
        mClient curThread = (mClient) Thread.currentThread();

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

        if(inMessage.getMsgType() != MessageType.COLCANREP){
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
        oClient curThread = (oClient) Thread.currentThread();

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }
        outMessage = new Message(MessageType.HNDCAN,curThread.getThiefID(),curThread.getCurAP(),curThread.getCurRoom(),curThread.hasPainting());
        com.writeObject(outMessage);

        inMessage= (Message) com.readObject();
        if(inMessage.getMsgType() != MessageType.HNDCANREP){
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
            GenericIO.writelnString("Thread " + ((mClient) Thread.currentThread()).getID() + ": Invalid message type");
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

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.STARTOP);
        com.writeObject(outMessage);

        inMessage= (Message) com.readObject();
        if(inMessage.getMsgType() != MessageType.STARTOPREP){
            GenericIO.writelnString("Thread " + ((mClient) Thread.currentThread()).getID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * 
     */
    public void sumUpResults(){

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

        outMessage = new Message(MessageType.SUMRES);
        com.writeObject(outMessage);

        inMessage= (Message) com.readObject();
        if(inMessage.getMsgType() != MessageType.SUMRESREP){
            GenericIO.writelnString("Thread " + ((mClient) Thread.currentThread()).getID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    public int getNextRoom(){

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

        outMessage = new Message(MessageType.GETNRM,curThread.getID());
        com.writeObject(outMessage);

        inMessage= (Message) com.readObject();
        if(inMessage.getMsgType() != MessageType.GETNRMREP){
            GenericIO.writelnString("Thread " + curThread.getID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        return inMessage.getoThRoom();
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
