package messageSystem.message;

import dbservice.DBService;
import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.Message;
import messageSystem.MessageSystemContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageGetCount extends Message {
    private static Logger logger = LoggerFactory.getLogger(MessageGetCount.class);
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
            logger.error("Зарос андресован не к ДБ сервису!");
        }
    }

    public void exec(DBService dbService) {
        String result = String.valueOf(dbService.count());
        dbService.getMS().sendMessage(new MessageGetCountResponse(getTo(), getFrom(), result, context, socketId));
    }
}
