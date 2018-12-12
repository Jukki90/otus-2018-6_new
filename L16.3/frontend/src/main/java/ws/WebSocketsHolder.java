package ws;


import java.util.Optional;
import java.util.Set;

public interface WebSocketsHolder {
    Set<WSocket> getWebSockets();

    Optional<WSocket> getWebSocket(String socketId);

    void add(String key, WSocket socket);

    void remove(WSocket socket);
}
