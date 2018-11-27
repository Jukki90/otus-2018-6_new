package messageSystem.message;

import base.UserDataSet;
import dbservice.DBService;
import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.Message;
import messageSystem.MessageSystemContext;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MessageSaveUser extends Message {
    private static final Logger logger = LogManager.getLogManager().getLogger(MessageSaveUser.class.getName());
    private final MessageSystemContext context;
    private final String socketId;
    private final UserDataSet userData;


    public MessageSaveUser(Address from, Address to, MessageSystemContext context, String socketId, UserDataSet userData) {
        super(from, to);
        this.context = context;
        this.socketId = socketId;
        this.userData = userData;
    }


    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBService) {
            exec((DBService) addressee);
        } else {
            logger.info("Зарос андресован не к ДБ сервису!");
        }
    }

    public void exec(DBService dbService) {

        dbService.save(userData);
        dbService.getMS().sendMessage(new MessageSaveUserResponse(getTo(), getFrom(), "success", context, socketId));
    }
}
