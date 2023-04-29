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
                                       else if ((inMessage.getoThRoom() < 0) || (inMessage.getoThRoom() >= 5))
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
                                   break;
        case MessageType.PREPAP:    
                                   break;
        case MessageType.TKREST: 
                                   break;
        case MessageType.COLCANV:  
                                   break;
        case MessageType.HNDCAN:    
                                   break;
        case MessageType.APSIT:  
                                   break;
        case MessageType.STARTOP:  
                                   break;
        case MessageType.SUMRES:  
                                   break;
        default:                   throw new MessageException ("Invalid message type!", inMessage);
      }
 
      return (outMessage);
    }
}
