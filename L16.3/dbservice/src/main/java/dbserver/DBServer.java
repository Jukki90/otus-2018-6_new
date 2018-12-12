package dbserver;



import cache.CacheEngine;
import cache.CacheEngineImpl;
import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.channel.SocketClientChannel;
import messageSystem.message.Msg;
import messageSystem.message.MsgCache;
import messageSystem.message.MsgRegister;
import messageSystem.message.MsgType;
import worker.Worker;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBServer implements Addressee {
    private static final Logger LOGGER = Logger.getLogger(DBServer.class.getName());
    private static final int DELAY = 500;
    private static final String HOST = "localhost";
    private static final int SOCKET_PORT = 5050;
    private Address address;
    private int num;
    private volatile boolean isRegistered = false;

    public DBServer(int num) {
        this.num = num;
        this.address = new Address("DBServer" + this.num);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void start() throws Exception {

        CacheEngine cacheEngine = new CacheEngineImpl(1000,10000,10000);
        SocketClientChannel client = new SocketClientChannel(new Socket(HOST, SOCKET_PORT));
        client.init();

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            try {
                while (!isRegistered) {
                    LOGGER.info("Sending registration message:  not registered yet");
                    client.send(new MsgRegister(address, new Address("MsgServerService")));
                    Thread.sleep(DELAY);
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        });

        executorService.submit(() -> {
            try {
                while (true) {
                    Msg receivedMsg = client.take();
                    LOGGER.info("Received message:  " + receivedMsg.getType() + "   from:   " + receivedMsg.getFrom() + " to:   " + receivedMsg.getTo());
                    if ((!isRegistered) && (receivedMsg.getType() == MsgType.REGISTER)) {
                        LOGGER.info("Receiving registration message answer:  not registered yet");
                        LOGGER.info("Registered on MsgServer successfully");
                        isRegistered = true;
                    }
                    if (receivedMsg.getType() == MsgType.REQUEST) {
                        LOGGER.info("RECEIVED Cache message");

                        // TO_DO: проброс параметров!!!!
                        Map<String, Object> cacheMap = new HashMap<>();
                        cacheMap.put("result",cacheEngine.get(0).getValue());
                        Msg msg = new MsgCache(receivedMsg.getTo(), receivedMsg.getFrom(), cacheMap);
                        client.send(msg);
                    }
                    Thread.sleep(DELAY);
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        });

        Worker worker = new Worker();

        executorService.submit(worker::run);
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
