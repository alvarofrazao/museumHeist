package src.sharedRegions;

import src.entities.mStates;
import src.entities.oStates;
import src.infrastructure.Message;
import src.infrastructure.MessageException;
import src.infrastructure.MessageType;

public class ConcentrationSiteInterface {

   /**
    * Reference to the ConcentrationSite.
    */

   private final ConcentrationSite cc;

   /**
    * Instantiation of an interface to the ConcentrationSite.
    *
    * @param reference to the
    */

   public ConcentrationSiteInterface(ConcentrationSite cc) {
      this.cc = cc;
   }

   /**
    * Processing of the incoming messages.
    *
    * Validation, execution of the corresponding method and generation of the
    * outgoing message.
    *
    * @param inMessage service request
    * @return service reply
    * @throws MessageException if the incoming message is not valid
    */

   public Message processAndReply(Message inMessage) throws MessageException {
      Message outMessage = null; // outgoing message

      /* validation of the incoming message */

      switch (inMessage.getMsgType()) {
         case MessageType.SNDPTY:
            if ((inMessage.getThId() < 6) || (inMessage.getThId() >= 7))
               throw new MessageException("Invalid master thief id!", inMessage);
            else if ((inMessage.getThState() < mStates.PLANNING_THE_HEIST)
                  || (inMessage.getThState() > mStates.PRESENTING_THE_REPORT))
               throw new MessageException("Invalid master thief state!", inMessage);
            break;
         case MessageType.PREPEX:
            if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
               throw new MessageException("Invalid thief id!", inMessage);
            break;

         default:
            throw new MessageException("Invalid message type!", inMessage);
      }

      /* processing */

      switch (inMessage.getMsgType()) {
         case MessageType.SNDPTY:
            break;
         case MessageType.PREPEX:
            break;

         default:
            throw new MessageException("Invalid message type!", inMessage);
      }

      return (outMessage);
   }
}