package messageSystem.channel;

import messageSystem.message.Msg;


import java.io.IOException;

public interface MsgChannel {
    void send(Msg msg);

    Msg poll();

    Msg take() throws InterruptedException;

    void close() throws IOException;
}
