import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import servlet.TemplateProcessor;

import java.util.logging.Logger;

public class FrontendMain {
    private static final Logger LOGGER = Logger.getLogger(FrontendMain.class.getName());
    //private final static String PUBLIC_HTML = "frontend/src/main/resources/tml/";
    //private final static String PUBLIC_HTML = "frontend/public_html";
    private final static String PUBLIC_HTML = "public_html";
    private static final String HOST = "localhost";
    private static final int PORT = 5050;

    public static void main(String[] args) throws Exception {
        int port = getParameter(args, "-port");
        int num = getParameter(args, "-num");
        LOGGER.info("Старт FrontendMain");

/*
        Socket socket = new Socket(HOST, port);
        SocketClientChannel client = new SocketClientChannel(socket);
        client.init();

        FrontendServer frontendServer = new FrontendServer(num, client);
        frontendServer.start();

*/
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(".");
        ResourceHandler resourceHandler = new ResourceHandler();
        //resourceHandler.setResourceBase(PUBLIC_HTML);
        //resourceHandler.setResourceBase(PUBLIC_HTML);
        resourceHandler.setResourceBase(FrontendMain.class.getClassLoader().getResource(PUBLIC_HTML).toExternalForm());

        //  context.setContextPath(".");
        TemplateProcessor templateProcessor = new TemplateProcessor();
        //  context.addServlet(new ServletHolder(new AdminServlet(templateProcessor, client, frontendServer)), "/");
        // context.addServlet(WebSocketChatServlet.class, "/ws");
        //  context.addServlet(AdminServlet.class, "/");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }

    public static Integer getParameter(String[] args, String pattern) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(pattern)) {
                return Integer.valueOf(args[i + 1]);
            }
        }
        throw new RuntimeException();
    }
}
