import messageSystem.MsgServer;
import runner.ProcessRunnerImpl;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MsgServerMain {
    private static final Logger LOGGER = Logger.getLogger(MsgServerMain.class.getName());

    private static final int CLIENT_START_DELAY_SEC = 2;
    private static MsgServer msgServer;
    //private int initialFrontPort = 49094;
    private int initialFrontPort = 5050;

    public static void main(String[] args) {
        int servicesToStart = getArgument(args);

        new MsgServerMain().start(servicesToStart);
    }

    public static Integer getArgument(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-count")) {
                return Integer.valueOf(args[i + 1]);
            }
        }
        throw new RuntimeException();
    }

    private void start(int servicesToStart) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(servicesToStart * 2);

        for (int i = 0; i < servicesToStart; i++) {
            startClient(executorService, "java -jar dbservice/target/dbservice.jar -num " + i);
            startClient(executorService, "java -jar frontend/target/frontend.jar -num " + i + " -port " + String.valueOf(initialFrontPort));
            //startClient(executorService, "java -jar frontend/target/frontend.war -num " + i + " -port " + String.valueOf(initialFrontPort));
            initialFrontPort++;
        }

        msgServer = new MsgServer();
        try {
            msgServer.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void startClient(ScheduledExecutorService executorService, String command) {
        executorService.schedule(() -> {
            try {
                LOGGER.info("STARTING:  " + command);
                new ProcessRunnerImpl().start(command);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }, CLIENT_START_DELAY_SEC, TimeUnit.SECONDS);
    }
}
