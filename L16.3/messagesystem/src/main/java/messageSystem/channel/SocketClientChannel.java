package messageSystem.channel;

import com.google.gson.Gson;
import messageSystem.message.Msg;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class SocketClientChannel implements MsgChannel {
    private static final Logger LOGGER = Logger.getLogger(SocketClientChannel.class.getName());
    private static final int WORKERS_COUNT = 2;

    private final BlockingQueue<Msg> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<Msg> input = new LinkedBlockingQueue<>();

    private final ExecutorService executor;

    private final Socket client;
    private final List<Runnable> shutdownRegistrations;

    public SocketClientChannel(Socket client) {
        this.client = client;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
        this.shutdownRegistrations = new CopyOnWriteArrayList<>();
    }

    public void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    public void addShutdownRegistration(Runnable runnable) {
        this.shutdownRegistrations.add(runnable);
    }

    private void receiveMessage()  {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty() && !stringBuilder.toString().isEmpty()) {
                    String json = stringBuilder.toString();
                    Msg msg = getMsgFromJSON(json);
                    input.add(msg);
                    stringBuilder = new StringBuilder();
                }
            }


        } catch (IOException | org.json.simple.parser.ParseException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Msg getMsgFromJSON(String json) throws  ClassNotFoundException, org.json.simple.parser.ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        String className = (String) jsonObject.get(Msg.CLASS_NAME_VARIABLE);
        Class<?> msgClass = Class.forName(className);
        return (Msg) new Gson().fromJson(json, msgClass);
    }

    private void sendMessage() {
        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            while (client.isConnected()) {
                Msg msg = output.take();
                String json = new Gson().toJson(msg);
                out.println(json);
                out.println();
            }
        } catch (InterruptedException | IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
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
    public void close() throws IOException {
        shutdownRegistrations.forEach(Runnable::run);
        shutdownRegistrations.clear();

        executor.shutdown();
    }
}
