package front;

import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageSystem;
import messageSystem.MessageSystemContext;
import messageSystem.message.MessageGetCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ws.WSocket;
import ws.WebSocketsHolder;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class FrontendServiceImpl implements FrontendService {
    private static final Logger logger = Logger.getLogger(FrontendServiceImpl.class.getName());

    public void setAddress(Address address) {
        this.address = address;
    }

    //@Autowired
    @Qualifier("frontAddress")
    private Address address;

    //@Autowired
    // private WSocket socket;

    private WebSocketsHolder webSockets;

    //@Autowired
    private final MessageSystemContext context;

   /* public FrontendServiceImpl(MessageSystemContext context) {
        this.context = context;
    }*/

    public FrontendServiceImpl(MessageSystemContext context, Address address, WebSocketsHolder webSockets) {
        this.context = context;
        this.address = address;
        this.webSockets = webSockets;
    }


    //@PostConstruct
    @Override
    public void init() {
        logger.info("Start init method for frontend service");
        context.getMessageSystem().addAddressee(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }

    @Override
    public void count(String socketId) {
        logger.info("address from "+ getAddress().getId());
        logger.info("address to "+ context.getDbAddress().getId());
        Message message = new MessageGetCount(getAddress(), context.getDbAddress(),context,socketId);
        context.getMessageSystem().sendMessage(message);
        logger.info("count finished - frontendServiceImpl");
    }

    @Override
    public void returnNumberOfUsers(String result, String socketId) {
        //socket.sendMessage(result);
        logger.info("Возвращаем результат в socket "+ result);
        //
        for (WSocket socket : webSockets.getWebSockets()) {
            try {
                socket.sendMessage(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }//
        Optional<WSocket> socket = webSockets.getWebSocket(socketId);
        if (!socket.isPresent()) {
            RuntimeException ex = new RuntimeException("Cannot get WebSocket by socketId=" + socketId);
            logger.info("Сокет не нашелся (для ответа)!");
            ex.printStackTrace();
            throw ex;
        }
    }

    public WebSocketsHolder getWebSocketsHolder() {
        return webSockets;
    }

}
