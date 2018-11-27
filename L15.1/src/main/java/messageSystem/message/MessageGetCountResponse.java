package messageSystem.message;


import front.FrontendService;
import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.Message;
import messageSystem.MessageSystemContext;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MessageGetCountResponse extends Message {
    private static final Logger logger = LogManager.getLogManager().getLogger(MessageGetCountResponse.class.getName());

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
            logger.info("Зарос андресован не к Фронту!");
        }
    }

    public void exec(FrontendService frontendService) {
        frontendService.returnNumberOfUsers(result, socketId);
    }
}
