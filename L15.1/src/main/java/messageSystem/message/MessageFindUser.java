package messageSystem.message;

import base.UserDataSet;
import dbservice.DBService;
import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.Message;
import messageSystem.MessageSystemContext;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MessageFindUser extends Message {
    private static final Logger logger = LogManager.getLogManager().getLogger(MessageGetCount.class.getName());
    private final MessageSystemContext context;
    private final String socketId;
    private final long userId;


    public MessageFindUser(Address from, Address to, MessageSystemContext context, String socketId, long userId){
        super(from, to);
        this.context = context;
        this.socketId = socketId;
        this.userId =userId;
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBService) {
            exec((DBService) addressee);
        }else{
            logger.info("Зарос андресован не к ДБ сервису!");
        }
    }


    public void exec(DBService dbService) {

        UserDataSet userDataSet = dbService.load(userId,UserDataSet.class);
        dbService.getMS().sendMessage(new MessageFindUserResponse(getTo(), getFrom(), userDataSet.getName(), context, socketId));
    }
}
