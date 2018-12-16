package front;

import base.UserDataSet;
import channel.ManagedMsgSocketWorker;
import messageSystem.Address;
import messageSystem.message.Msg;
import messageSystem.message.MsgCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ws.WSocket;
import ws.WebSocketsHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class FrontendServiceImpl implements FrontendService {
    private static final Logger logger = LoggerFactory.getLogger(FrontendServiceImpl.class);
    private static final int DELAY = 500;
    public static final String METHOD = "METHOD";
    private volatile boolean isRegistered = false;


    public void setAddress(Address address) {
        this.address = address;
    }

    @Qualifier("frontAddress")
    private Address address;

    private WebSocketsHolder webSockets;
    private ManagedMsgSocketWorker msClient;


    public FrontendServiceImpl(Address address, WebSocketsHolder webSockets, ManagedMsgSocketWorker msClient) {
        this.address = address;
        this.webSockets = webSockets;
        this.msClient = msClient;
        try {
            logger.info("trying to start executors");
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                while (true) {
                    Msg msg = msClient.take();
                    System.out.println("Message received: " + msg.toString());
                    returnStringResult(msg.getValue().toString(), msg.getWebSocketId());
                }
            } catch (InterruptedException e) {
                logger.info(e.getMessage());
            }
        });
    }

    @Override
    public void init() {
        logger.info("Start init method for frontend service");
    }


    @Override
    public Address getAddress() {
        return address;
    }


    @Override
    public void count(String socketId) {
        logger.info("address from " + getAddress().getId());
        Map<String, Object> reqParam = new HashMap<>();
        reqParam.put(METHOD, "COUNT");
        MsgCache request = new MsgCache(address, new Address("dbAddress"), reqParam, socketId);
        logger.info("Send message to db service:  " + request.getType() + "   from:   " + request.getFrom().getId() + " to:   " + request.getTo().getId() + " value: " + request.getValue());
        msClient.send(request);

    }

    @Override
    public void getUserById(String socketId, long id) {
        logger.info("address from " + getAddress().getId());
        Map<String, Object> reqParam = new HashMap<>();
        reqParam.put(METHOD, "GET_USER_ID");
        reqParam.put("ID", id);
        MsgCache request = new MsgCache(address, new Address("dbAddress"), reqParam, socketId);
        logger.info("Send message to db service:  " + request.getType() + "   from:   " + request.getFrom().getId() + " to:   " + request.getTo().getId() + " value: " + request.getValue());
        msClient.send(request);
    }


    @Override
    public void save(String socketId, UserDataSet user) {
        logger.info("address from " + getAddress().getId());
        Map<String, Object> reqParam = new HashMap<>();
        reqParam.put(METHOD, "SAVE");
        reqParam.put("USER", user);
        MsgCache request = new MsgCache(address, new Address("dbAddress"), reqParam, socketId);
        logger.info("Send message to db service:  " + request.getType() + "   from:   " + request.getFrom().getId() + " to:   " + request.getTo().getId() + " value: " + request.getValue());
        msClient.send(request);
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


    public WebSocketsHolder getWebSocketsHolder() {
        return webSockets;
    }

}
