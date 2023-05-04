package serverSide.sharedRegions;

import infrastructure.Message;
import infrastructure.MessageException;
import infrastructure.MessageType;


import serverSide.entities.*;

public class AssaultPartyInterface {

   /**
    * Reference to the AssaultParty.
    */

   private final AssaultParty aParty;

   /**
    * Instantiation of an interface to the AssaultParty.
    *
    * @param aParty reference to the AssaultParty
    */

   public AssaultPartyInterface(AssaultParty aParty) {
      this.aParty = aParty;
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
         case MessageType.ADDTH:
            if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
               throw new MessageException("Invalid thief id!", inMessage);
            break;
         case MessageType.SETP:
            if (inMessage.getThId() != 6)
               throw new MessageException("Invalid master thief id!", inMessage);
            break;
         case MessageType.CRIN:
            if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
               throw new MessageException("Invalid master thief id!", inMessage);
            else if ((inMessage.getroomDist() < 15) || (inMessage.getThState() >= 31))
               throw new MessageException("Invalid room distance value!", inMessage);
            else if ((inMessage.getoThAP() < 0) || (inMessage.getoThAP() >= 2))
               throw new MessageException("Invalid assault party id!", inMessage);
            else if ((inMessage.getoThPartyPos() < 0) || (inMessage.getoThPartyPos() >= 3))
            throw new MessageException("Invalid party position!", inMessage);
            break;
         case MessageType.CROUT:
            if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
               throw new MessageException("Invalid master thief id!", inMessage);
            else if ((inMessage.getroomDist() < 15) || (inMessage.getThState() >= 31))
               throw new MessageException("Invalid room distance value!", inMessage);
            else if ((inMessage.getoThAP() < 0) || (inMessage.getoThAP() >= 2))
               throw new MessageException("Invalid assault party id!", inMessage);
            else if ((inMessage.getoThPartyPos() < 0) || (inMessage.getoThPartyPos() >= 3))
               throw new MessageException("Invalid party position!", inMessage);
            break;
         case MessageType.REVDIR:
            if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6))
               throw new MessageException("Invalid master thief id!", inMessage);
            else if ((inMessage.getoThPartyPos() < 0) || (inMessage.getoThPartyPos() >= 3))
               throw new MessageException("Invalid party position!", inMessage);
            break;
         case MessageType.SIGNDEP:
            // doesn't check for anything
            break;
         case MessageType.SHUTDOWN:
            break;
         default:
            throw new MessageException("Invalid message type!", inMessage);
      }

      /* processing */
      aPClientProxy curThread = (aPClientProxy) Thread.currentThread();
      switch (inMessage.getMsgType()) {
         case MessageType.ADDTH:
            curThread.setId(inMessage.getThId());
            int room = aParty.addThief();
            outMessage = new Message(MessageType.ADDTHREP,curThread.getThId(),curThread.getPpos(),room);
            break;
         case MessageType.SETP:
            aParty.setupParty(inMessage.getoThRoom());
            outMessage = new Message(MessageType.SETPREP);
            break;
         case MessageType.CRIN:
            curThread.setId(inMessage.getThId());
            curThread.setAp(inMessage.getoThAP());
            curThread.setPpos(inMessage.getoThPartyPos());
            aParty.crawlIn(inMessage.getroomDist());
            outMessage = new Message(MessageType.CRINREP);
            break;
         case MessageType.CROUT:
            curThread.setId(inMessage.getThId());
            curThread.setAp(inMessage.getoThAP());
            curThread.setPpos(inMessage.getoThPartyPos());
            aParty.crawlOut(inMessage.getroomDist());
            outMessage = new Message(MessageType.CROUTREP);
            break;
         case MessageType.REVDIR:
            curThread.setId(inMessage.getThId());
            curThread.setPpos(inMessage.getoThPartyPos());
            aParty.reverseDirection();
            outMessage = new Message(MessageType.REVDIREP);
            break;
         case MessageType.SIGNDEP:
            aParty.signalDeparture();
            outMessage = new Message(MessageType.SIGNDEPREP);
            break;
         case MessageType.SHUTDOWN:
            aParty.shutdown();
            outMessage = new Message(MessageType.SHUTDONE);
            break;
         default:
            throw new MessageException("Invalid message type!", inMessage);
      }

      return (outMessage);
   }
}
