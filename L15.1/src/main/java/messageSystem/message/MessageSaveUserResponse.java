package messageSystem.message;

import front.FrontendService;
import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.Message;
import messageSystem.MessageSystemContext;
import org.slf4j.LoggerFactory;

public class MessageSaveUserResponse extends Message {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MessageSaveUserResponse.class);
    private String result;
    private final MessageSystemContext context;
    private final String socketId;

    public MessageSaveUserResponse(Address from, Address to, String result, MessageSystemContext context, String socketId) {
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
            logger.error("Зарос адресован не к Фронту!");
        }
    }

    public void exec(FrontendService frontendService) {
        frontendService.returnResultAfterSaving(result, socketId);
    }
}
