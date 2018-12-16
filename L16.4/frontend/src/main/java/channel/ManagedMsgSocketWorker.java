package channel;

import com.google.gson.Gson;
import messageSystem.channel.SocketMsgWorker;

import java.io.IOException;
import java.net.Socket;


public class ManagedMsgSocketWorker extends SocketMsgWorker {

    private final Socket socket;

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

}
