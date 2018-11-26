package front;

import messageSystem.Addressee;
import ws.WebSocketsHolder;

public interface FrontendService extends Addressee {
    void init();

    void count(String socketId);

    void returnNumberOfUsers(String result, String socketId);
    //void addUser(int id, String name);

    void getUserById(String socketId, long id);

    void returnUserById(String result, String socketId);

    WebSocketsHolder getWebSocketsHolder();
}
