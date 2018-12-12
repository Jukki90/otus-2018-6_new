package front;

import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.channel.SocketClientChannel;
import messageSystem.message.Msg;
import messageSystem.message.MsgRegister;
import messageSystem.message.MsgType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrontendServer implements Addressee {
    private static final Logger LOGGER = Logger.getLogger(FrontendServer.class.getName());
    private static final int DELAY = 500;
    private final Address address;
    private final SocketClientChannel client;
    private final Address addressMessageServer = new Address("MsgServerService");
    private final Address addressDBServer;
    private Map<String, Object> cacheMap = new HashMap<>();
    private int num;
    private volatile boolean isRegistered = false;

    public FrontendServer(int num, SocketClientChannel client) {
        this.num = num;
        this.address = new Address("Frontend" + this.num);
        this.client = client;

        this.addressDBServer = new Address("DBServer" + num);
    }

    public void start() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            try {
                while (!isRegistered) {
                    LOGGER.info("Sending registration message:  not registered yet");
                    client.send(new MsgRegister(address, addressMessageServer));
                    Thread.sleep(DELAY);
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        });

        executorService.submit(this::getMessage);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void getMessage() {
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
                    LOGGER.info("Getting cache information");
                    cacheMap = receivedMsg.getValue();
                }
                Thread.sleep(DELAY);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getCacheMap() {
        return cacheMap;
    }

    public Address getFrontendAddress() {
        return address;
    }

    public Address getDBServerAddress() {
        return addressDBServer;
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
