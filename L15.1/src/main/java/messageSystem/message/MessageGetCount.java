package messageSystem.message;

import dbservice.DBService;
import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.Message;
import messageSystem.MessageSystemContext;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MessageGetCount extends Message {

    private static final Logger logger = LogManager.getLogManager().getLogger(MessageGetCount.class.getName());
    private final MessageSystemContext context;
    private final String socketId;

    public MessageGetCount(Address from, Address to, MessageSystemContext context, String socketId) {
        super(from, to);
        this.context = context;
        this.socketId = socketId;
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
        String result = String.valueOf(dbService.count());
        dbService.getMS().sendMessage(new MessageGetCountResponse(getTo(), getFrom(), result, context, socketId));
    }
}
