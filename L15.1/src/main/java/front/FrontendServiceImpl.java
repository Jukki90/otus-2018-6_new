package front;

import base.UserDataSet;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageSystem;
import messageSystem.MessageSystemContext;
import messageSystem.message.MessageFindUser;
import messageSystem.message.MessageGetCount;
import messageSystem.message.MessageSaveUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ws.WSocket;
import ws.WebSocketsHolder;

import java.util.Optional;


@Service
public class FrontendServiceImpl implements FrontendService {
    private static final Logger logger = LoggerFactory.getLogger(FrontendServiceImpl.class);

    public void setAddress(Address address) {
        this.address = address;
    }

    @Qualifier("frontAddress")
    private Address address;

    private WebSocketsHolder webSockets;

    private final MessageSystemContext context;

    public FrontendServiceImpl(MessageSystemContext context, Address address, WebSocketsHolder webSockets) {
        this.context = context;
        this.address = address;
        this.webSockets = webSockets;
    }

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
        logger.info("address from " + getAddress().getId());
        logger.info("address to " + context.getDbAddress().getId());
        Message message = new MessageGetCount(getAddress(), context.getDbAddress(), context, socketId);
        context.getMessageSystem().sendMessage(message);
    }

    @Override
    public void getUserById(String socketId, long id) {
        logger.info("address from " + getAddress().getId());
        logger.info("address to " + context.getDbAddress().getId());
        Message message = new MessageFindUser(getAddress(), context.getDbAddress(), context, socketId, id);
        context.getMessageSystem().sendMessage(message);
        logger.info("count finished - frontendServiceImpl");
    }

    @Override
    public void returnNumberOfUsers(String result, String socketId) {
        returnStringResult(result, socketId);
    }

    @Override
    public void returnResultAfterSaving(String result, String socketId) {
        returnStringResult(result, socketId);
    }

    @Override
    public void returnUserById(String result, String socketId) {
        returnStringResult(result, socketId);
    }

    private void returnStringResult(String result, String socketId) {
        logger.info("Возвращаем результат {} в socket {}", result, socketId);
        Optional<WSocket> socket = webSockets.getWebSocket(socketId);
        if (!socket.isPresent()) {
            RuntimeException ex = new RuntimeException("Cannot get WebSocket by socketId=" + socketId);
            logger.info("Сокет не нашелся (для ответа)!");
            ex.printStackTrace();
            throw ex;
        } else {
            socket.get().sendMessage(result);
        }
    }

    @Override
    public void save(String socketId, UserDataSet user) {
        logger.info("address from " + getAddress().getId());
        logger.info("address to " + context.getDbAddress().getId());
        Message message = new MessageSaveUser(getAddress(), context.getDbAddress(), context, socketId, user);
        context.getMessageSystem().sendMessage(message);
    }

    public WebSocketsHolder getWebSocketsHolder() {
        return webSockets;
    }

}
