package messageSystem.channel;



import com.fasterxml.jackson.databind.ObjectMapper;
import messageSystem.message.Msg;
import messageSystem.workers.MsgWorker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by tully.
 */
public class SocketMsgWorker implements MsgWorker {
    private static final Logger logger = Logger.getLogger(SocketMsgWorker.class.getName());
    private static final int WORKERS_COUNT = 2;
    private static final int QUEUE_CAPACITY = 10;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final BlockingQueue<Msg> output = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    private final BlockingQueue<Msg> input = new LinkedBlockingQueue<>(QUEUE_CAPACITY);

    protected final Socket socket;
    private final ExecutorService executor;
    private final List<Runnable> shutdownRegistrations;

    public SocketMsgWorker(Socket socket) {
        this.socket = socket;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
        this.shutdownRegistrations = new ArrayList<>();
    }

    @Override
    public void send(Msg msg) {
        output.add(msg);
    }

    @Override
    public Msg poll() {
        return input.poll();
    }


    @Override
    public Msg take() throws InterruptedException {
        return input.take();
    }

    @Override
    public void close() {
        System.out.println("Worker closed");
        shutdownRegistrations.forEach(Runnable::run);
        shutdownRegistrations.clear();

        executor.shutdown();
    }

    public void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    public void addShutdownRegistration(Runnable runnable) {
        this.shutdownRegistrations.add(runnable);
    }


    private void sendMessage() {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            while (socket.isConnected()) {
                final Msg msg = output.take(); //blocks
                final String json = MAPPER.writeValueAsString(msg);
                System.out.println("Sending message: " + json);
                writer.println(json);
                writer.println();//line with json + an empty line
            }
        } catch (InterruptedException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }


    private void receiveMessage() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) { //blocks
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) { //empty line is the end of the message
                    final String json = stringBuilder.toString();
                    System.out.println("Receiving message: " + json);
                    final Msg msg = MAPPER.readValue(json, Msg.class);
                    input.add(msg);
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }
    }

}
