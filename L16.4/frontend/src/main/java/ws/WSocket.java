package ws;


import base.UserDataSet;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import front.FrontendService;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jsondata.CommandEnum;
import jsondata.JsonDataImpl;

import java.io.IOException;
import java.lang.reflect.Type;


@WebSocket
public class WSocket {
    private static Logger logger = LoggerFactory.getLogger(WSocket.class);

    private Session session;
    private String socketId;
    private FrontendService frontendService;


    public WSocket(FrontendService frontend) {
        this.frontendService = frontend;
    }

    @OnWebSocketConnect
    public void open(Session session) {
        logger.info("Start onOpen()");

        this.session = session;
        this.socketId = String.valueOf(session.hashCode());
        frontendService.getWebSocketsHolder().add(socketId, this);
        logger.info("End onOpen(). socketId=" + socketId);
    }


    @OnWebSocketClose
    public void closedConnection(int statusCode, String reason) {
        logger.info("close");
        frontendService.getWebSocketsHolder().remove(this);
    }

    @OnWebSocketError
    public void error(Session session, Throwable t) {
        logger.error("ws error", t);
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
        logger.info("Получено сообщение" + msg);
        this.session = session;
        Gson gson = new Gson();
        JsonDataImpl<UserDataSet> data = null;
        try {
            Type userType = new TypeToken<JsonDataImpl<UserDataSet>>() {
            }.getType();
            data = gson.fromJson(msg, userType);
        } catch (JsonSyntaxException ex) {
            logger.error("Возникла ошибка при парсинге запроса", ex);
            ex.printStackTrace();
        }
        CommandEnum method = data.getCommand();
        logger.info("Выполняется метод: " + method);
        switch (method) {
            case GET_USER_BY_ID:
                UserDataSet userData = (UserDataSet) data.getUserData();
                long userId = userData.getId();
                this.frontendService.getUserById(socketId, userId);
                break;
            case GET_COUNT:
                this.frontendService.count(socketId);
                break;

            case CREATE_USER:
                UserDataSet user = data.getUserData();
                this.frontendService.save(socketId, user);
                break;
            default:
                logger.error("Не обрабатывается метод с таким праметром");
        }

        logger.info("onMessage finished");
    }

    public void sendMessage(String cacheParams) {
        try {
            if (session.isOpen()) {
                session.getRemote().sendString(cacheParams);
            } else {
                logger.info("Сессия закрыта!!!!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Возникла ошибка ", e);
        }
    }

    public String getSocketId() {
        return socketId;
    }
}
