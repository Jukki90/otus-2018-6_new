package ws;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WebSocketsHolderImpl implements WebSocketsHolder{
    private final Map<String, WSocket> webSockets = new ConcurrentHashMap<>();

    @Override
    public Set<WSocket> getWebSockets() {
        return webSockets.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    @Override
    public Optional<WSocket> getWebSocket(String socketId) {
        return webSockets.entrySet()
                .stream()
                .filter(x -> Objects.equals(x.getKey(), socketId))
                .map(Map.Entry::getValue)
                .findFirst();
    }


    @Override
    public void add(String key, WSocket socket) {
        webSockets.put(key, socket);
    }

    @Override
    public void remove(WSocket socket) {
        webSockets.remove(socket.getSocketId());
    }
}
