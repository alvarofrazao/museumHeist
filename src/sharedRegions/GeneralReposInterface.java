package src.sharedRegions;

public class GeneralReposInterface {

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
       { case MessageType.ADDTH:  if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
                                       throw new MessageException ("Invalid thief id!", inMessage);
                                       else if ((inMessage.getThState() < oStates.CONCENTRATION_SITE) || (inMessage.getThState() > oStates.COLLECTION_SITE))
                                               throw new MessageException ("Invalid thief state!", inMessage);
                                    break;
         case MessageType.SETP:    if ((inMessage.getThId() < 6) || (inMessage.getThId() >= 7))
                                       throw new MessageException ("Invalid master thief id!", inMessage);
                                    break;
         case MessageType.CRIN: if ((inMessage.getThId() < 6) || (inMessage.getThId() >= 7))
                                       throw new MessageException ("Invalid master thief id!", inMessage);
                                       else if ((inMessage.getThState () < oStates.CONCENTRATION_SITE) || (inMessage.getThState () > oStates.COLLECTION_SITE))
                                               throw new MessageException ("Invalid master thief state!", inMessage);
                                    break;
         case MessageType.CROUT:   if ((inMessage.getThId () < 0) || (inMessage.getThId () >= 6))
                                       throw new MessageException ("Invalid thief id!", inMessage);
                                       else if ((inMessage.getThState() < oStates.CONCENTRATION_SITE) || (inMessage.getThState() > oStates.COLLECTION_SITE))
                                               throw new MessageException ("Invalid thief state!", inMessage);
                                    break;
         case MessageType.REVDIR:    if ((inMessage.getThId () < 0) || (inMessage.getThId () >= 6))
                                       throw new MessageException ("Invalid master thief id!", inMessage);
                                    break;
         case MessageType.SIGNDEP:  
                                    if ((inMessage.getThId() < 6) || (inMessage.getThId() >= 7))
                                        throw new MessageException ("Invalid master thief id!", inMessage);
                                    break;
         default:                   throw new MessageException ("Invalid message type!", inMessage);
       }
 
      /* processing */
 
      switch (inMessage.getMsgType ())
      { case MessageType.ADDTH:  
                                   break;
        case MessageType.SETP:    
                                   break;
        case MessageType.CRIN: 
                                   break;
        case MessageType.CROUT:  
                                   break;
        case MessageType.REVDIR:    
                                   break;
        case MessageType.SIGNDEP:  
                                   break;
        default:                   throw new MessageException ("Invalid message type!", inMessage);
      }
 
      return (outMessage);
}
