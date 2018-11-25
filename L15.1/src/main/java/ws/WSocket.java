package ws;


import front.FrontendService;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

//@ServerEndpoint(value = "/ws", configurator = SpringConfigurator.class)
//@Controller
@WebSocket
public class WSocket {
    private static final Logger logger = Logger.getLogger(WSocket.class.getName());

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
    public void closedConnection(int statusCode,String reason) {
        logger.info("close");
        frontendService.getWebSocketsHolder().remove(this);
    }

    //@OnError
    @OnWebSocketError
    public void error(Session session, Throwable t) {
        logger.info("ws error");
    }


    //@OnMessage
    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
        logger.info("Получено сообщение"+msg);
        this.session = session;
        this.frontendService.count(socketId);
        logger.info("onMessage finished");
    }

    public void sendMessage(String cacheParams) {
        try {
            this.session.getRemote().sendString(cacheParams);

            //for (Session sess : session.getOpenSessions()) {
                if (session.isOpen())
                    session.getRemote().sendString(cacheParams);
            //}
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    public String getSocketId() {
        return socketId;
    }
}
