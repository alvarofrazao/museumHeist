package serverSide.sharedRegions;

import serverSide.entities.museumClientProxy;
import serverSide.infrastructure.Message;
import serverSide.infrastructure.MessageException;
import serverSide.infrastructure.MessageType;

public class MuseumInterface {

  /**
   * Reference to the Museum.
   */

  private final Museum museum;

  /**
   * Instantiation of an interface to the .
   *
   * @param reference to the Museum
   */

  public MuseumInterface(Museum museum) {
    this.museum = museum;
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

    if ((inMessage.getMsgType() != MessageType.ROLLCAN) && (inMessage.getMsgType() != MessageType.GETDIST) && (inMessage.getMsgType() != MessageType.SHUTDOWN)) {
      throw new MessageException("Invalid message type!", inMessage);
    } else {
      if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6)) {
        throw new MessageException("Invalid thief id!", inMessage);
      } else if ((inMessage.getoThRoom() < 0) || (inMessage.getoThRoom() >= 5)) {
        throw new MessageException("Invalid room id!", inMessage);
      }
    }

    museumClientProxy curThread = (museumClientProxy)Thread.currentThread();

    /* processing */

    switch (inMessage.getMsgType()) {
      case MessageType.ROLLCAN:
        curThread.setId(inMessage.getThId());
        curThread.setAp(inMessage.getoThAP());
        if(museum.rollACanvas(inMessage.getoThRoom())){
          outMessage = new Message(MessageType.ROLLCANREP,true);
        }else{
          outMessage = new Message(MessageType.ROLLCANREP,false);
        }
        break;
      case MessageType.GETDIST:
        int rDist = museum.getRoomDistance(inMessage.getoThRoom());
        outMessage = new Message(MessageType.GETDISTREP, rDist);
        break;
      case MessageType.SHUTDOWN:
        museum.shutdown();
        outMessage = new Message(MessageType.SHUTDONE);
      default:
        throw new MessageException("Invalid message type!", inMessage);
    }

    return (outMessage);
  }
}
