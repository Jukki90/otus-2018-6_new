package main;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import servlet.AdminServlet;

/**
 * mysql> CREATE USER 'user'@'localhost' IDENTIFIED BY 'qwerty';
 * mysql> GRANT ALL PRIVILEGES ON * . * TO 'user'@'localhost';
 * mysql> select user, host from mysql.user;
 * mysql> create database hw10;
 * mysql> SET GLOBAL time_zone = '+3:00';
 */

public class Main {

    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "public_html";

    public static void main(String[] args) throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(AdminServlet.class, "/admin");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }
}