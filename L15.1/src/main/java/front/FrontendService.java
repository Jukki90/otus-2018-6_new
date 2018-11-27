package front;

import base.UserDataSet;
import messageSystem.Addressee;
import ws.WebSocketsHolder;

public interface FrontendService extends Addressee {
    void init();

    void count(String socketId);

    void returnNumberOfUsers(String result, String socketId);

    void getUserById(String socketId, long id);

    void returnUserById(String result, String socketId);

    void save(String socketId, UserDataSet user);

    WebSocketsHolder getWebSocketsHolder();

}
