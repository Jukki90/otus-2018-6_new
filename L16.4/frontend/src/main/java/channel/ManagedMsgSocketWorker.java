package channel;

import com.google.gson.Gson;
import messageSystem.channel.SocketMsgWorker;
import messageSystem.message.Msg;
import messageSystem.message.MsgCache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;


public class ManagedMsgSocketWorker extends SocketMsgWorker {

    private final Socket socket;
    Gson gson = new Gson();

    public ManagedMsgSocketWorker(String host, int port) throws IOException {
        this(new Socket(host, port));
    }

    private ManagedMsgSocketWorker(Socket socket) throws IOException {
        super(socket);
        this.socket = socket;
    }

    @Override
    public void close() {
        super.close();
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

/*
    @Override
    private void receiveMessage() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) { //blocks
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) { //empty line is the end of the message
                    final String json = stringBuilder.toString();
                    System.out.println("Receiving message: " + json);
                    Msg msg = gson.fromJson(json, MsgCache.class); //MAPPER.readValue(json, Msg.class);
                    input.add(msg);
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }

    }*/
}
