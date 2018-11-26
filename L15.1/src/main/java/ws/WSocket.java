package ws;


import com.google.gson.Gson;
import front.FrontendService;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import ws.jsondata.CommandInfo;

import java.io.IOException;
import java.util.logging.Logger;

//@ServerEndpoint(value = "/ws", configurator = SpringConfigurator.class)
//@Controller
@WebSocket
public class WSocket {
    private static final Logger logger = Logger.getLogger(WSocket.class.getName());
    public static final String GET_USER_BY_ID = "GET_USER_BY_ID";
    public static final String GET_COUNT = "GET_COUNT";
    public static final String CREATE_USER = "CREATE_USER";

    private Session session;
    private String socketId;
    //@Autowired
    FrontendService frontendService;

    /*
        public FrontendService getFrontendService() {
            return frontendService;
        }

        public void setFrontendService(FrontendService frontendService) {
            this.frontendService = frontendService;
        }
    */
    public WSocket(FrontendService frontend) {
        this.frontendService = frontend;
    }


    //@OnOpen
    @OnWebSocketConnect
    public void open(Session session) {
        logger.info("Start onOpen()");

        this.session = session;
        this.socketId = String.valueOf(session.hashCode());
        frontendService.getWebSocketsHolder().add(socketId, this);

        logger.info("End onOpen(). socketId=" + socketId);
    }

    //@OnClose
    @OnWebSocketClose
    public void closedConnection(int statusCode, String reason) {
        logger.info("close");
        frontendService.getWebSocketsHolder().remove(this);
    }

    //@OnError
    @OnWebSocketError
    public void error(Session session, Throwable t) {
        logger.info("ws error");
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
        logger.info("Получено сообщение" + msg);
        this.session = session;
        Gson g = new Gson();

        CommandInfo commandInfo = g.fromJson(msg, CommandInfo.class);
        String method = commandInfo.getMethod();

        switch (method) {
            case GET_USER_BY_ID:
                long userId = commandInfo.getUserId();
                this.frontendService.getUserById(socketId, userId);
                break;
            case GET_COUNT:
                this.frontendService.count(socketId);
                break;

            case CREATE_USER:
                break;
            default:
                logger.info("Не обрабатывается метод с таким праметром");

        }

        // this.frontendService.count(socketId);
        logger.info("onMessage finished");
    }

    public void sendMessage(String cacheParams) {
        try {
            if (session.isOpen())
                session.getRemote().sendString(cacheParams);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    public String getSocketId() {
        return socketId;
    }
}
