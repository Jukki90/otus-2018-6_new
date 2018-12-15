package dbserver;


import dbservice.CacheServiceImpl;
import dbservice.DBService;
import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.channel.MsgChannel;
import messageSystem.message.Msg;
import messageSystem.message.MsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DBServer implements Addressee {
    private static final Logger logger = LoggerFactory.getLogger(DBServer.class);
    private static final int DELAY = 500;
    private static final String HOST = "localhost";
    private static final int SOCKET_PORT = 5050;
    private static final int PORT = 5050;
    private Address address;
    //private int num;
    private static final int THREADS_NUMBER = 4;
    private volatile boolean isRegistered = false;
    private DBService cache;
    private final ExecutorService executor;
    private final ConcurrentMap<MsgChannel, Address> registeredChannels;


    public DBServer(int num) {
       // this.num = num;
        this.address = new Address("DBServer");
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        this.registeredChannels = new ConcurrentHashMap<>();
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "DBSpringBeans.xml"); //get Spring context
        cache = ctx.getBean("cacheService", CacheServiceImpl.class);
    }


    public void start() throws Exception {
       // executor.submit(this::getMessage);
    }

    /*

    @SuppressWarnings("InfiniteLoopStatement")
    private void getMessage() {
        while (true) {
            for (Map.Entry<MsgChannel, Address> entry : registeredChannels.entrySet()) {
                MsgChannel channel = entry.getKey();

                Msg message = channel.poll();
                if (message != null) {

                    Address to = message.getTo();
                    Address from = message.getFrom();
                    logger.info("Received the message from: " + from + " to:   " + to + " message:   " + message);

                    if (message.getType() == MsgType.REGISTER) {
                        registeredChannels.put(channel, from);
                        logger.info("Registered address:    " + from.getId());
                        channel.send(message);
                    } else if (message.getType() == MsgType.REQUEST) {
                        if (toChannel != null) {
                            if (registeredChannels.containsKey(toChannel)) {

                                logger.info("Sending cache answer message: " + from + " to:   " + to + " message:   " + message);
                                logger.info("MsgServer :    Current CACHE status: " + message.getValue());
                                getChannel(to).send(message);
                            }
                        } else {
                            logger.info("Receiver for message:  " + message + " wasn't registered yet");
                            logger.info("Need to register:  " + to.getId() + " first");
                            logger.info("Try to send MsgRegister to:  MsgServerService");
                        }
                        logger.info("Address FROM:  " + getChannel(from));
                        logger.info("Address TO:  " + getChannel(to));
                    }
                }
            }
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    
    @Override
    public Address getAddress() {
        return address;
    }
}
