package messageSystem;

import messageSystem.channel.MsgChannel;
import messageSystem.channel.SocketClientChannel;
import messageSystem.message.Msg;
import messageSystem.message.MsgType;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class MsgServer {
    private static final Logger LOGGER = Logger.getLogger(MsgServer.class.getName());

    private static final int THREADS_NUMBER = 4;
    private static final int PORT = 5050;
    private static final int DELAY = 100;

    private final ExecutorService executor;
    private final ConcurrentMap<MsgChannel, Address> registeredChannels;

    public MsgServer() {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        this.registeredChannels = new ConcurrentHashMap<>();
    }

    public void start() throws Exception {
        executor.submit(this::getMessage);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            LOGGER.info("Server started on port: " + serverSocket.getLocalPort());
            while (!executor.isShutdown()) {
                Socket client = serverSocket.accept();
                SocketClientChannel channel = new SocketClientChannel(client);
                channel.init();
                LOGGER.info("New channel:   " + channel);
                channel.addShutdownRegistration(() -> registeredChannels.remove(channel));
                registeredChannels.put(channel, new Address("EMPTY"));
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void getMessage() {
        while (true) {
            for (Map.Entry<MsgChannel, Address> entry : registeredChannels.entrySet()) {
                MsgChannel channel = entry.getKey();

                Msg message = channel.poll();
                if (message != null) {
                    MsgChannel toChannel = getChannel(message.getTo());
                    Address to = message.getTo();
                    Address from = message.getFrom();
                    LOGGER.info("Received the message from: " + from + " to:   " + to + " message:   " + message);

                    if (message.getType() == MsgType.REGISTER) {
                        registeredChannels.put(channel, from);
                        LOGGER.info("Registered address:    " + from.getId());
                        channel.send(message);
                    } else if (message.getType() == MsgType.REQUEST) {
                        if (toChannel != null) {
                            if (registeredChannels.containsKey(toChannel)) {

                                LOGGER.info("Sending cache answer message: " + from + " to:   " + to + " message:   " + message);
                                LOGGER.info("MsgServer :    Current CACHE status: " + message.getValue());
                                getChannel(to).send(message);
                            }
                        } else {
                            LOGGER.info("Receiver for message:  " + message + " wasn't registered yet");
                            LOGGER.info("Need to register:  " + to.getId() + " first");
                            LOGGER.info("Try to send MsgRegister to:  MsgServerService");
                        }
                        LOGGER.info("Address FROM:  " + getChannel(from));
                        LOGGER.info("Address TO:  " + getChannel(to));
                    }
                }
            }
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private MsgChannel getChannel(Address address) {
        if (Objects.isNull(address)) {
            throw new NullPointerException();
        }
        for (Map.Entry<MsgChannel, Address> msgChannelAddressEntry : registeredChannels.entrySet()) {
            if (msgChannelAddressEntry.getValue().equals(address)) {
                return msgChannelAddressEntry.getKey();
            }
        }
        return null;
    }
}
