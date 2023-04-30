package src.sharedRegions;

import src.infrastructure.Message;
import src.infrastructure.MessageException;
import src.infrastructure.MessageType;
import src.entities.*;

public class ControlCollectionSiteInterface {

    /**
     * Reference to the ControlCollectionSite.
     */

    private final ControlCollectionSite ccl;

    /**
     * Instantiation of an interface to the ControlCollectionSite.
     *
     * @param reference to the
     */

    public ControlCollectionSiteInterface(ControlCollectionSite ccl) {
        this.ccl = ccl;
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
 
       switch (inMessage.getMsgType ())
       { case MessageType.AIN:  if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
                                       throw new MessageException ("Invalid thief id!", inMessage);
                                       else if ((inMessage.getThState() < oStates.CONCENTRATION_SITE) || (inMessage.getThState() > oStates.COLLECTION_SITE))
                                               throw new MessageException ("Invalid thief state!", inMessage);
                                    break;
         case MessageType.PREPAP:    if ((inMessage.getThId() < 6) || (inMessage.getThId() >= 7))
                                       throw new MessageException ("Invalid master thief id!", inMessage);
                                    break;
         case MessageType.TKREST: if ((inMessage.getThId() < 6) || (inMessage.getThId() >= 7))
                                       throw new MessageException ("Invalid master thief id!", inMessage);
                                    break;
         case MessageType.COLCANV:   if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
                                       throw new MessageException ("Invalid thief id!", inMessage);
                                    break;
         case MessageType.HNDCAN:    if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
                                       throw new MessageException ("Invalid thief id!", inMessage);
                                       else if ((inMessage.getoThRoom() < 0) || (inMessage.getoThRoom() >= 5)){

                                       }
                                    break;
         case MessageType.APSIT:  
                                    if ((inMessage.getThId() < 6) || (inMessage.getThId() >= 7))
                                        throw new MessageException ("Invalid master thief id!", inMessage);
                                    break;
         case MessageType.STARTOP:
                                    if ((inMessage.getThId() < 6) || (inMessage.getThId() >= 7))
                                        throw new MessageException ("Invalid master thief id!", inMessage);
                                    break;
         case MessageType.SUMRES:
                                    if ((inMessage.getThId() < 6) || (inMessage.getThId() >= 7)){
                                        throw new MessageException ("Invalid master thief id!", inMessage);}
                                    break;  
         default:                   throw new MessageException ("Invalid message type!", inMessage);
       }
 
      /* processing */
 
      switch (inMessage.getMsgType ())
      { case MessageType.AIN:  
            ((cclClientProxy)Thread.currentThread()).setThId(inMessage.getThId());
            ((cclClientProxy)Thread.currentThread()).setFC(inMessage.getoThFC());
            ((cclClientProxy)Thread.currentThread()).setPS(inMessage.getoThSit());
            if(ccl.amINeeded()){
                outMessage = new Message(MessageType.AINREP, true, ((cclClientProxy)Thread.currentThread()).getFC());
            }else{
                outMessage = new Message(MessageType.AINREP, false, ((cclClientProxy)Thread.currentThread()).getFC());
            }
            break;
        case MessageType.PREPAP:
            ((cclClientProxy)Thread.currentThread()).setThId(inMessage.getThId());   
            int nextParty = ccl.prepareAssaultParty();
            outMessage = new Message(MessageType.PREPAPREP,inMessage.getThId(),nextParty);
            break;
        case MessageType.TKREST: 
            ((cclClientProxy)Thread.currentThread()).setThId(inMessage.getThId());
            ccl.takeARest(); 
            outMessage = new Message(MessageType.TKRESTREP);
            break;
        case MessageType.COLCANV:  
            ((cclClientProxy)Thread.currentThread()).setThId(inMessage.getThId());
            ccl.collectACanvas();
            outMessage = new Message(MessageType.COLCANREP);
            break;
        case MessageType.HNDCAN: 
            ((cclClientProxy)Thread.currentThread()).setThId(inMessage.getThId());
            ((cclClientProxy)Thread.currentThread()).setThAP(inMessage.getoThAP());
            ((cclClientProxy)Thread.currentThread()).setRoom(inMessage.getoThRoom());
            ((cclClientProxy)Thread.currentThread()).setHC(inMessage.getoThCanvas());
            ccl.handACanvas();
            outMessage = new Message(MessageType.HNDCANREP);
            break;
        case MessageType.APSIT:
            int result = ccl.appraiseSit();
            outMessage = new Message(MessageType.APSITREP,result);
            break;
        case MessageType.STARTOP: 
            ccl.startOperations();
            outMessage = new Message(MessageType.STARTOPREP);
            break;
        case MessageType.SUMRES:  
            ccl.sumUpResults();
            outMessage = new Message(MessageType.SUMRESREP);
            break;
        default: throw new MessageException ("Invalid message type!", inMessage);
      }
 
      return (outMessage);
    }
}
