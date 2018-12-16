package messageSystem.workers;

import messageSystem.message.Msg;

import java.io.Closeable;


public interface MsgWorker extends Closeable {
    void send(Msg msg);

    Msg poll();


    Msg take() throws InterruptedException;

    void close();
}
