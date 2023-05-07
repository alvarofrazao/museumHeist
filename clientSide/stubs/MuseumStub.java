package clientSide.stubs;

import genclass.GenericIO;
import clientSide.entities.oClient;
import infrastructure.ClientCom;
import infrastructure.Message;
import infrastructure.MessageType;

public class MuseumStub {
    
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
    public MuseumStub(String serverHostname, int serverPortNum){
        this.serverHostName = serverHostname;
        this.serverPortNum = serverPortNum;
    }

    public int getRoomDistance(int roomID){
        
        ClientCom com;

        Message outMessage, inMessage;
        

        com = new ClientCom(serverHostName, serverPortNum);

        while(!com.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }
            catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.GETDIST,roomID);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.GETDISTREP){
            GenericIO.writelnString("Thread " + Thread.currentThread().getId() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
        return inMessage.getRI1();
    }

    public boolean rollACanvas(int roomID){
        
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

        outMessage = new Message(MessageType.ROLLCAN,curThread.getThiefID(),curThread.getCurAP(), roomID);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.ROLLCANREP){
            GenericIO.writelnString("Thread " + curThread.getThiefID() + ": Invalid message type");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
        return inMessage.getRB();
    }

    public void shutdown(){
        ClientCom com;

        Message outMessage, inMessage;

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

        com.close();
    }
}
