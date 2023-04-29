package src.sharedRegions;

import src.infrastructure.Message;
import src.infrastructure.MessageException;
import src.infrastructure.MessageType;

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

    if ((inMessage.getMsgType() != MessageType.ROLLCAN) || (inMessage.getMsgType() != MessageType.GETDIST)) {
      throw new MessageException("Invalid message type!", inMessage);
    } else {
      if ((inMessage.getThId() < 0) || (inMessage.getThId() >= 6)) {
        throw new MessageException("Invalid thief id!", inMessage);
      } else if ((inMessage.getoThRoom() < 0) || (inMessage.getoThRoom() >= 5)) {
        throw new MessageException("Invalid room id!", inMessage);
      }
    }

    /* processing */

    switch (inMessage.getMsgType()) {
      case MessageType.ROLLCAN:
        break;
      case MessageType.GETDIST:
        break;
      default:
        throw new MessageException("Invalid message type!", inMessage);
    }

    return (outMessage);
  }
}
