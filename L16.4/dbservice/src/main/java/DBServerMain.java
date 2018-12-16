
import dbserver.NonBlockingEchoSocketMsgServer;

import java.util.logging.Logger;

public class DBServerMain {
    private static final Logger LOGGER = Logger.getLogger(DBServerMain.class.getName());

    public static void main(String[] args) throws Exception {

        NonBlockingEchoSocketMsgServer msgServer = new NonBlockingEchoSocketMsgServer();
        msgServer.start();
    }
}
