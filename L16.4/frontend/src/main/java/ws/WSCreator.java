package ws;

import front.FrontendService;
import messageSystem.channel.SocketMsgWorker;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class WSCreator implements WebSocketCreator {
    private static Logger logger = LoggerFactory.getLogger(WSCreator.class);
    private Set<WSocket> users;
    private SocketMsgWorker client;


    public WSCreator() {
        this.users = ConcurrentHashMap.newKeySet();
        logger.info("WSCreator created");
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("FrontSpringBeans.xml"); //get Spring context

        FrontendService front = ctx.getBean("frontendService", FrontendService.class);
        WSocket socket = new WSocket(front);
        return socket;
    }
}
