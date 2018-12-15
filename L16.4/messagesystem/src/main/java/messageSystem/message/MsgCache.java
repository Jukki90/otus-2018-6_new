package messageSystem.message;


import messageSystem.Address;


import java.util.Map;
import java.util.logging.Logger;

public class MsgCache extends Msg {
    private static final Logger LOGGER = Logger.getLogger(MsgCache.class.getName());

    public MsgCache(Address from, Address to, Map<String, Object> value) {
        super(MsgType.REQUEST, MsgCache.class, from, to, value);
    }
}
