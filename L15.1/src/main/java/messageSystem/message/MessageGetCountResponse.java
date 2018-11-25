package messageSystem.message;


import front.FrontendService;
import front.FrontendServiceImpl;
import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.Message;
import messageSystem.MessageSystemContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MessageGetCountResponse extends Message {
    private static final Logger LOG = LogManager.getLogManager().getLogger(MessageGetCountResponse.class.getName());
    //@Autowired
    //FrontendServiceImpl frontendService;
    private String result;
    private final MessageSystemContext context;
    private final String socketId;

    public MessageGetCountResponse(Address from, Address to, String result, MessageSystemContext context, String socketId) {
        super(from, to);
        this.result = result;
        this.context = context;
        this.socketId = socketId;
    }
/*
    @Override
    public void exec(Addressee addressee) {
        frontendService.returnNumberOfUsers(result);
    }
*/

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof FrontendService) {
            exec((FrontendService) addressee);
        } else {
            //todo error!
        }
    }

    public void exec(FrontendService frontendService) {
//        LOG.info("Start MessageGetUserByIdAnswer.exec()");
        frontendService.returnNumberOfUsers(result, socketId);
    }
}
