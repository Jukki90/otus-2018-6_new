package front;

import messageSystem.Address;
import messageSystem.Addressee;
import messageSystem.MessageSystem;
import ws.WebSocketsHolder;

public interface FrontendService extends Addressee {
    void init();

    void count(String socketId);

    void returnNumberOfUsers(String result,String socketId);
    //void addUser(int id, String name);

    WebSocketsHolder getWebSocketsHolder();
}
