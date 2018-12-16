import messageSystem.channel.SocketMsgWorker;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import servlet.TemplateProcessor;
import ws.WebSocketChatServlet;
import java.util.logging.Logger;

public class FrontendMain {
    private static final Logger logger = Logger.getLogger(FrontendMain.class.getName());
    private final static String PUBLIC_HTML = "public_html";
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        logger.info("Старт FrontendMain");

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(".");
        context.addServlet(WebSocketChatServlet.class, "/ws");


        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(FrontendMain.class.getClassLoader().getResource(PUBLIC_HTML).toExternalForm());

        TemplateProcessor templateProcessor = new TemplateProcessor();

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }

}
