package src.sharedRegions;

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
            break;
         case MessageType.PREPEX:
            if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
               throw new MessageException("Invalid thief id!", inMessage);
            break;
         case MessageType.SHUTDOWN:
            break;

         default:
            throw new MessageException("Invalid message type!", inMessage);
      }

      /* processing */

      switch (inMessage.getMsgType()) {
         case MessageType.SNDPTY:
            cc.sendAssaultParty();
            outMessage = new Message(MessageType.SNDPTYREP);
            break;
         case MessageType.PREPEX:
            int nextParty = cc.prepareExcursion();
            outMessage = new Message(MessageType.PREPEXREP,nextParty);
            break;
         case MessageType.SHUTDOWN:
            cc.shutdown();
            break;
         default:
            throw new MessageException("Invalid message type!", inMessage);
      }

      return (outMessage);
   }
}