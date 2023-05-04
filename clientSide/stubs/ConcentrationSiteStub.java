package clientSide.stubs;

import genclass.GenericIO;
import clientSide.entities.mClient;
import clientSide.entities.oClient;
import infrastructure.ClientCom;
import infrastructure.Message;
import infrastructure.MessageType;

public class ConcentrationSiteStub {
    
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
    public ConcentrationSiteStub(String serverHostname, int serverPortNum){
        this.serverHostName = serverHostname;
        this.serverPortNum = serverPortNum;
    }

    public void sendAssaultParty(){
        
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

        outMessage = new Message(MessageType.SNDPTY, curThread.getID());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.SNDPTYREP){
            GenericIO.writelnString("Thread " + curThread.getID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    public int prepareExcursion(){
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

        outMessage = new Message(MessageType.PREPEX,curThread.getThiefID());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.PREPEXREP){
            GenericIO.writelnString("Thread " + curThread.getThiefID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
        return inMessage.getRI1();
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
