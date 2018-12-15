package messageSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author tully
 */

@Component
public final class MessageSystem {
    private static Logger logger = LoggerFactory.getLogger(MessageSystem.class);
    private static final int DEFAULT_STEP_TIME = 10;
    private final int stepTime;

    private final Map<Address, ConcurrentLinkedQueue<Message>> messagesMap = new HashMap<>();
    private final Map<Address, Addressee> addresseeMap = new HashMap<>();

    public MessageSystem() {
        this(DEFAULT_STEP_TIME);
    }

    public MessageSystem(int stepTime) {
        this.stepTime = stepTime;
    }

    public void addAddressee(Addressee addressee) {
        Address address = addressee.getAddress();
        addresseeMap.put(address, addressee);
        messagesMap.put(address, new ConcurrentLinkedQueue<>());

        startForAddressee(addressee);
    }

    public void sendMessage(Message message) {
        Address from = message.getFrom();
        Address to = message.getTo();
        messagesMap.get(to).add(message);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void startForAddressee(Addressee addressee) {
        new Thread(() -> {
            Address address = addressee.getAddress();
            while (true) {
                ConcurrentLinkedQueue<Message> queue = messagesMap.get(address);
                while (!queue.isEmpty()) {
                    Message message = queue.poll();
                    message.exec(addressee);
                }
                pause();
            }
        }).start();
    }

    private void pause() {
        try {
            Thread.sleep(stepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
