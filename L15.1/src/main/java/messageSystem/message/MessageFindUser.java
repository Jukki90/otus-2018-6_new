package messageSystem.message;

import base.UserDataSet;
import dbservice.DBService;
import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.Message;
import messageSystem.MessageSystemContext;
import org.slf4j.LoggerFactory;

public class MessageFindUser extends Message {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(Message.class);
    private final MessageSystemContext context;
    private final String socketId;
    private final long userId;


    public MessageFindUser(Address from, Address to, MessageSystemContext context, String socketId, long userId) {
        super(from, to);
        this.context = context;
        this.socketId = socketId;
        this.userId = userId;
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBService) {
            exec((DBService) addressee);
        } else {
            logger.error("Зарос андресован не к ДБ сервису!");
        }
    }


    public void exec(DBService dbService) {
        UserDataSet userDataSet = dbService.load(userId, UserDataSet.class);
        String result =null;
        if(userDataSet!=null){
            result=userDataSet.getName();
        }else{
            result = "Пользователь не найден!";
        }
        dbService.getMS().sendMessage(new MessageFindUserResponse(getTo(), getFrom(),result , context, socketId));
    }
}
