package dbserver;

import base.UserDataSet;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dbservice.CacheServiceImpl;
import dbservice.DBService;
import messageSystem.Address;
import messageSystem.message.MsgCache;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NonBlockingEchoSocketMsgServer {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(NonBlockingEchoSocketMsgServer.class);
    private static final int THREADS_NUMBER = 1;
    private static final int PORT = 5050;
    private static final int ECHO_DELAY = 100;
    private static final int CAPACITY = 256;
    private static final String MESSAGES_SEPARATOR = "\n\n";

    private final ExecutorService executor;
    private final Map<String, ChannelMessages> channelMessages;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    Gson gson = new Gson();
    ObjectMapper objectMapper = new ObjectMapper();


    DBService dbService;

    public NonBlockingEchoSocketMsgServer() {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        channelMessages = new ConcurrentHashMap<>();
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("DBSpringBeans.xml"); //get Spring context

        dbService = ctx.getBean("cacheService", CacheServiceImpl.class);

    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void start() throws Exception {
        executor.submit(this::echo);

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", PORT));

            serverSocketChannel.configureBlocking(false); //non blocking mode
            int ops = SelectionKey.OP_ACCEPT;
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, ops, null);

            logger.info("Started on port: " + PORT);

            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    try {
                        if (key.isAcceptable()) {
                            SocketChannel channel = serverSocketChannel.accept(); //non blocking accept
                            String remoteAddress = channel.getRemoteAddress().toString();
                            System.out.println("Connection Accepted: " + remoteAddress);

                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);

                            channelMessages.put(remoteAddress, new ChannelMessages(channel));

                        } else if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();

                            ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
                            int read = channel.read(buffer);
                            if (read != -1) {
                                String result = new String(buffer.array()).trim();
                                System.out.println("Message received: " + result + " from: " + channel.getRemoteAddress());
                                channelMessages.get(channel.getRemoteAddress().toString()).messages.add(result);
                            } else {
                                key.cancel();
                                String remoteAddress = channel.getRemoteAddress().toString();
                                channelMessages.remove(remoteAddress);
                                System.out.println("Connection closed, key canceled");
                            }
                        }
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    } finally {
                        iterator.remove();
                    }
                }
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private Object echo() throws InterruptedException {
        while (true) {
            for (Map.Entry<String, ChannelMessages> entry : channelMessages.entrySet()) {
                ChannelMessages channelMessages = entry.getValue();
                if (channelMessages.channel.isConnected()) {
                    channelMessages.messages.forEach(message -> {
                        try {
                            System.out.println("Echoing message to: " + entry.getKey());
                            ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);

                            MsgCache result = getDBResult(message);
                            buffer.put(gson.toJson(result).getBytes());
                            buffer.put(MESSAGES_SEPARATOR.getBytes());
                            buffer.flip();
                            while (buffer.hasRemaining()) {
                                channelMessages.channel.write(buffer);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    channelMessages.messages.clear();
                }
            }
            Thread.sleep(ECHO_DELAY);
        }
    }

    private MsgCache getDBResult(String message) {
        logger.info("Пытаемся обработать запрос");
        try {
            JsonNode jsonNodeWrapper = objectMapper.readTree(message);
            JsonNode jsonNode = jsonNodeWrapper.get("value");
            String method = jsonNode.get("METHOD").asText();
            Map<String, Object> resMap = new HashMap<>();
            switch (method) {
                case "GET_USER_ID":
                    long id = jsonNode.get("ID").asLong();
                    UserDataSet user = dbService.load(id, UserDataSet.class);
                    if(user!=null) {
                        resMap.put("RESULT", user.getName());
                    }
                    else{
                        resMap.put("RESULT", "Пользователь не найден!");
                    }
                    break;
                case "SAVE":
                    String userStr = jsonNode.get("USER").toString();
                    UserDataSet usr = objectMapper.readValue(userStr, UserDataSet.class);
                    dbService.save(usr);
                    resMap.put("RESULT", "SUCCESS");
                    break;
                case "COUNT":
                    resMap.put("RESULT", dbService.count());
                    break;

                default:
                    logger.error("Метод не реализован!");
            }
            Address from = new Address("dbAddress");
            Address to = new Address("Frontend");
            String wsId = jsonNodeWrapper.get("webSocketId").asText();
            MsgCache result = new MsgCache(from, to, resMap, wsId);
            System.out.println("Send result: " + resMap.toString());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean getRunning() {
        return true;
    }


    public void setRunning(boolean running) {
        if (!running) {
            executor.shutdown();
            logger.info("Bye.");
        }
    }

    private class ChannelMessages {
        private final SocketChannel channel;
        private final List<String> messages = new ArrayList<>();

        private ChannelMessages(SocketChannel channel) {
            this.channel = channel;
        }
    }
}
