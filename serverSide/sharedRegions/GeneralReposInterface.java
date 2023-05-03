package serverSide.sharedRegions;

import serverSide.entities.mStates;
import serverSide.entities.oStates;
import serverSide.infrastructure.Message;
import serverSide.infrastructure.MessageException;
import serverSide.infrastructure.MessageType;

public class GeneralReposInterface {

  private final GeneralRepos gr;



  public GeneralReposInterface(GeneralRepos gr){
    this.gr = gr;
  }
  /**
    *  Processing of the incoming messages.
    *
    *  Validation, execution of the corresponding method and generation of the outgoing message.
    *
    *    @param inMessage service request
    *    @return service reply
    *    @throws MessageException if the incoming message is not valid
    */
 
    public Message processAndReply (Message inMessage) throws MessageException
    {
       Message outMessage = null;                                     // outgoing message
 
      /* validation of the incoming message */
 
       switch (inMessage.getMsgType ()){ 
        case MessageType.LOGINIT:  
          if (inMessage.getLogName() == null)
              throw new MessageException ("Logfile name not present!", inMessage);
                                      
          break;
        case MessageType.SETOTSTT:    
          if ((inMessage.getThId() < 6) || (inMessage.getThId() >= 7))
            throw new MessageException ("Invalid ordinary thief id!", inMessage);
          else if ((inMessage.getThState() < oStates.CONCENTRATION_SITE) || (inMessage.getThState() > oStates.COLLECTION_SITE))
            throw new MessageException ("Invalid ordinary thief state!", inMessage);
          break;
        case MessageType.SETOTPSTT: 
          if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
            throw new MessageException ("Invalid ordinary thief id!", inMessage);
          else if ((inMessage.getoThSit() != 'W') && (inMessage.getThState () != 'P'))
            throw new MessageException ("Invalid ordinary thief party state!", inMessage);
          break;
        case MessageType.SETTHMD:   
          if ((inMessage.getThId () < 0) || (inMessage.getThId () >= 6))
            throw new MessageException ("Invalid ordinary thief id!", inMessage);
          else if ((inMessage.getoThMaxDist() < 2) || (inMessage.getoThMaxDist() > 6))
            throw new MessageException ("Invalid value for MD!", inMessage);
          break;
        case MessageType.SETMTHSTT:    
          if ((inMessage.getThId () < 0) || (inMessage.getThId () >= 6))
            throw new MessageException ("Invalid master thief id!", inMessage);
          else if ((inMessage.getThState() < mStates.PLANNING_THE_HEIST) || (inMessage.getThState() > mStates.PRESENTING_THE_REPORT))
            throw new MessageException ("Invalid master thief state!", inMessage);
          break;
        case MessageType.ADDTHAP:  
          if ((inMessage.getThId() < 6) || (inMessage.getThId() >= 7))
            throw new MessageException ("Invalid master thief id!", inMessage);
          else if ((inMessage.getoThAP() < 0) || (inMessage.getoThAP() > 1))
            throw new MessageException ("Invalid assault party id!", inMessage);
          else if ((inMessage.getoThPartyPos() < 0) || (inMessage.getoThPartyPos() > 2))
            throw new MessageException ("Invalid assault party position!", inMessage);
          break;
        case MessageType.REMTHAP:
            if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
               throw new MessageException("Invalid master thief id!", inMessage);
            else if ((inMessage.getoThAP() < 0) || (inMessage.getoThAP() >= 2))
               throw new MessageException("Invalid assault party id!", inMessage);
          break;
        case MessageType.SETTHPOS:
          if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
            throw new MessageException("Invalid master thief id!", inMessage);
          else if ((inMessage.getoThAP() < 0) || (inMessage.getoThAP() >= 2))
            throw new MessageException("Invalid assault party id!", inMessage);
          break;
        case MessageType.SETTHCAN:
          if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
            throw new MessageException("Invalid master thief id!", inMessage);
          else if ((inMessage.getoThAP() < 0) || (inMessage.getoThAP() >= 2))
            throw new MessageException("Invalid assault party id!", inMessage);
          break;
        case MessageType.SETRDISTPNTS:
          if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
            throw new MessageException("Invalid room id!", inMessage);
          break;
        case MessageType.SETPNTSRM:
          if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
            throw new MessageException("Invalid room id!", inMessage);
          break;
        case MessageType.SETAPRM:
          if ((inMessage.getoThAP() < 0)|| (inMessage.getoThAP() >= 2))
            throw new MessageException("Invalid assault party id!", inMessage);
          else if ((inMessage.getoThRoom() < 0) || (inMessage.getoThRoom() >= 5))
            throw new MessageException("Invalid room id!", inMessage);
        case MessageType.FINRES:
          //checks nothing
          break;
        case MessageType.SHUTDOWN:
          break;
        default: throw new MessageException ("Invalid message type!", inMessage);

      }
 
      /* processing */
 
      switch (inMessage.getMsgType ()){
        case MessageType.LOGINIT:
          //gr.logInit(inMessage.getLogName()); <- corrigir a funçao
          break;
        case MessageType.SETOTSTT:
          gr.setOrdinaryThiefState(inMessage.getThId(), inMessage.getThState());
          outMessage = new Message(MessageType.ACK);
          break;
        case MessageType.SETOTPSTT:
          gr.setOrdinaryThiefPartyState(inMessage.getThId(), inMessage.getoThSit());
          outMessage = new Message(MessageType.ACK);
          break;
        case MessageType.SETTHMD:
          gr.setOrdinaryThiefMD(inMessage.getThId(),inMessage.getoThMaxDist());
          outMessage = new Message(MessageType.ACK);
          break;
        case MessageType.SETMTHSTT:   
          gr.setMasterThiefState(inMessage.getThState());
          outMessage = new Message(MessageType.ACK);
          break;
        case MessageType.ADDTHAP:
          gr.addThiefToAssaultParty(inMessage.getThId(),inMessage.getoThAP(),inMessage.getoThPartyPos());
          outMessage = new Message(MessageType.ACK);
          break;
        case MessageType.REMTHAP:
          gr.removeThiefFromAssaultParty(inMessage.getThId(),inMessage.getoThAP());
          outMessage = new Message(MessageType.ACK);
          break;
        case MessageType.SETTHPOS:  
          gr.setThiefPosition(inMessage.getThId(),inMessage.getoThAP(),inMessage.getoThPartyPos());
          outMessage = new Message(MessageType.ACK);
          break;
        case MessageType.SETTHCAN:  
          gr.setThiefCanvas(inMessage.getThId(),inMessage.getoThAP(),inMessage.getRI1());
          outMessage = new Message(MessageType.ACK);
          break;
        case MessageType.SETRDISTPNTS:
          gr.setRoomDistanceAndPaintings(inMessage.getoThRoom(),inMessage.getroomDist(),inMessage.getRI1());
          outMessage = new Message(MessageType.ACK);
          break;
        case MessageType.SETPNTSRM:
          gr.setNumPaintingsInRoom(inMessage.getoThRoom(),inMessage.getRI1());
          outMessage = new Message(MessageType.ACK); 
          break;
        case MessageType.SETAPRM:
          gr.setAssaultPartyRoom(inMessage.getoThAP(),inMessage.getoThRoom());
          outMessage = new Message(MessageType.ACK);
          break;
        case MessageType.FINRES:
          gr.finalResult(inMessage.getRI1());
          outMessage = new Message(MessageType.ACK);
          break;
        case MessageType.SHUTDOWN:
          gr.shutdown();
          outMessage = new Message(MessageType.SHUTDONE);
        default: throw new MessageException ("Invalid message type!", inMessage);
      }
 
      return (outMessage);
  }
}
