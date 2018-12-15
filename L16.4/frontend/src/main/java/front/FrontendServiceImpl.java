package front;

import base.UserDataSet;
import channel.ManagedMsgSocketWorker;
import messageSystem.Address;
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
    // private final SocketClient socketClient;
    private static final int DELAY = 500;
    private volatile boolean isRegistered = false;
    // private SocketClientChannel msClient;

    public void setAddress(Address address) {
        this.address = address;
    }

    @Qualifier("frontAddress")
    private Address address;

    private WebSocketsHolder webSockets;
    private ManagedMsgSocketWorker msClient;


    //private final MessageSystemContext context;

    public FrontendServiceImpl( Address address, WebSocketsHolder webSockets,ManagedMsgSocketWorker msClient) {
        //this.context = context;
        this.address = address;
        this.webSockets = webSockets;
        this.msClient =msClient;
        /*
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "FrontSpringBeans.xml"); //get Spring context
*/
       // msClient = ctx.getBean("msClient", ManagedMsgSocketWorker.class);
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  socketClient = new SocketClient();

/*
        try {
            socketClient.initPool("localhost", 5050);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //  init();// ??????
    }

    public void start() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                while (true) {
                    Object msg = msClient.take();
                    System.out.println("Message received: " + msg.toString());
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

    /*
    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }
*/
    @Override
    public void count(String socketId) {
        logger.info("address from " + getAddress().getId());
/*
        try {
            socketClient.doRequest("localhost", 5050, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        //  logger.info("address to " + context.getDbAddress().getId());
        // Message message = new MessageGetCount(getAddress(), context.getDbAddress(), context, socketId);
        //   context.getMessageSystem().sendMessage(message);
    }

    @Override
    public void getUserById(String socketId, long id) {
        logger.info("address from " + getAddress().getId());
        Map<String, Object> reqParam = new HashMap<>();
        reqParam.put("ID", id);




        /*
        executorService.submit(() -> {
            try {
                // while (!isRegistered) {
                logger.info("Sending registration message:  not registered yet");
                msClient.send(new MsgCache(address, new Address("MsgServerService"), reqParam));
                Thread.sleep(100);
                //}
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.info(e.getMessage());
            }
        });
*/

     /*   executorService.submit(() -> {
            try {
                while (!isRegistered) {*/


        MsgCache request = new MsgCache(address, new Address("dbAddress"), reqParam);
        logger.info("Send message to db service:  " + request.getType() + "   from:   " + request.getFrom().getId() + " to:   " + request.getTo().getId() + " value: " + request.getValue());
        //msClient.send(new MsgRegister(address, new Address("MsgServerService")));
        msClient.send(request);
                    /*
                    Thread.sleep(DELAY);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });*/

                    /*
        executorService.submit(() -> {
            try {
                while (true) {
                    logger.info("Attempt to take resp");
                    Msg receivedMsg = msClient.take();
                    logger.info("Received message:  " + receivedMsg.getType() + "   from:   " + receivedMsg.getFrom() + " to:   " + receivedMsg.getTo() + ", value: " + receivedMsg.getValue());
                    if ((!isRegistered)) {
                        logger.info("Receiving registration message answer:  not registered yet");
                        logger.info("Received message:  " + receivedMsg.getType() + "   from:   " + receivedMsg.getFrom() + " to:   " + receivedMsg.getTo() + ", value: " + receivedMsg.getValue());
                        logger.info("Registered on MsgServer successfully {}", receivedMsg);
                        isRegistered = true;
                    }

                    Thread.sleep(DELAY);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
*/

        //msClient.send(new MsgCache( new Address("frontAddress"), new Address("dbAddress"), reqParam));
/*
        try {
            socketClient.doRequest("localhost", 5050, String.valueOf(id));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        // logger.info("address to " + context.getDbAddress().getId());
        // Message message = new MessageFindUser(getAddress(), context.getDbAddress(), context, socketId, id);
        // context.getMessageSystem().sendMessage(message);
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
        // logger.info("address to " + context.getDbAddress().getId());
        //Message message = new MessageSaveUser(getAddress(), context.getDbAddress(), context, socketId, user);
        // context.getMessageSystem().sendMessage(message);
    }

    public WebSocketsHolder getWebSocketsHolder() {
        return webSockets;
    }

}
