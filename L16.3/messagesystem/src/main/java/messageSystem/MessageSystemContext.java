package messageSystem;

import org.springframework.stereotype.Component;

/**
 * Created by tully.
 */
@Component
public class MessageSystemContext {

    private final MessageSystem messageSystem;
    private Address frontAddress;
    private Address dbAddress;

    public MessageSystemContext(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }


    public MessageSystem getMessageSystem() {
        return messageSystem;
    }


    public Address getFrontAddress() {
        return frontAddress;
    }

    public void setFrontAddress(Address frontAddress) {
        this.frontAddress = frontAddress;
    }

    public Address getDbAddress() {
        return dbAddress;
    }

    public void setDbAddress(Address dbAddress) {
        this.dbAddress = dbAddress;
    }
}
