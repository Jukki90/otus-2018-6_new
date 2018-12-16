package messageSystem;

import messageSystem.channel.MsgChannel;
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

}
