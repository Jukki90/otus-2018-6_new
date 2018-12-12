import dbserver.DBServer;
import util.Utils;

import java.util.logging.Logger;

public class DBServerMain {
    private static final Logger LOGGER = Logger.getLogger(DBServerMain.class.getName());

    public static void main(String[] args) throws Exception {
        int num = Utils.getParameter(args, "-num");
        DBServer dbServer = new DBServer(num);
        dbServer.start();
    }
}
