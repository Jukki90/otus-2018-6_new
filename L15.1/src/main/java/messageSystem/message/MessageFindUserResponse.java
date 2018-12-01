package messageSystem.message;

import front.FrontendService;
import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.Message;
import messageSystem.MessageSystemContext;
import org.slf4j.LoggerFactory;

public class MessageFindUserResponse extends Message {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MessageFindUserResponse.class);

    private String result;
    private final MessageSystemContext context;
    private final String socketId;

    public MessageFindUserResponse(Address from, Address to, String result, MessageSystemContext context, String socketId) {
        super(from, to);
        this.result = result;
        this.context = context;
        this.socketId = socketId;
    }


    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof FrontendService) {
            exec((FrontendService) addressee);
        } else {
            logger.error("Зарос андресован не к Фронту!");
        }
    }

    public void exec(FrontendService frontendService) {
        frontendService.returnUserById(result, socketId);
    }
}
