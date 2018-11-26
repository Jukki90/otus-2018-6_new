package messageSystem;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author tully
 */

@Component
public final class MessageSystem {
    private static final Logger LOG = LogManager.getLogManager().getLogger(MessageSystem.class.getName());
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
//        LOG.info(String.format("Added Addressee %s with address %s", addressee, address));

        addresseeMap.put(address, addressee);
        messagesMap.put(address, new ConcurrentLinkedQueue<>());

        startForAddressee(addressee);
    }

    public void sendMessage(Message message) {
        Address from = message.getFrom();
        Address to = message.getTo();
      //  LOG.info("Send message " + message + " from " + from + " to " + to);
        messagesMap.get(to).add(message);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void startForAddressee(Addressee addressee) {
        new Thread(() -> {
            Address address = addressee.getAddress();
           // LOG.info("Start Thread for Address " + address.getId().toString());
            while (true) {
                ConcurrentLinkedQueue<Message> queue = messagesMap.get(address);
                while (!queue.isEmpty()) {
                    Message message = queue.poll();
                    //LOG.info("Start process Message " + message.toString());
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