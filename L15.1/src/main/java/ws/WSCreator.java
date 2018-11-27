package ws;

import front.FrontendService;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author v.chibrikov
 */
@Component
public class WSCreator implements WebSocketCreator {
    private final static Logger log = Logger.getLogger(WSCreator.class.getName());
    private Set<WSocket> users;


    public WSCreator() {
        this.users = ConcurrentHashMap.newKeySet();
        log.info("WSCreator created");
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        FrontendService front = ApplicationContextProvider.getApplicationContext().getBean("frontendService", FrontendService.class);
        WSocket socket = new WSocket(front);
        log.info("WSocket created, socketId=" + socket.getSocketId());
        return socket;
    }
}
