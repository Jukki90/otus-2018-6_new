package messageSystem.message;


import front.FrontendService;
import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.Message;
import messageSystem.MessageSystemContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MessageGetCountResponse extends Message {
    private static Logger logger = LoggerFactory.getLogger(MessageGetCount.class);

    private String result;
    private final MessageSystemContext context;
    private final String socketId;

    public MessageGetCountResponse(Address from, Address to, String result, MessageSystemContext context, String socketId) {
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
        frontendService.returnNumberOfUsers(result, socketId);
    }
}
