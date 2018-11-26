package messageSystem.message;

import front.FrontendService;
import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.Message;
import messageSystem.MessageSystemContext;

public class MessageFindUserResponse extends Message {

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
            //todo error!
        }
    }

    public void exec(FrontendService frontendService) {
//        LOG.info("Start MessageGetUserByIdAnswer.exec()");
        frontendService.returnUserById(result, socketId);
    }
}
