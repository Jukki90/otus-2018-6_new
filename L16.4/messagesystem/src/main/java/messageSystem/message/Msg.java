package messageSystem.message;


import messageSystem.Address;

import java.util.Map;

public abstract class Msg {
    public static final String CLASS_NAME_VARIABLE = "className";
    private final Address from;
    private final String className;
    private Address to;
    private Map<String, Object> value;
    private MsgType type;



    private String webSocketId;


    protected Msg(MsgType type, Class<?> clazz, Address from, Address to, Map<String, Object> value, String socketId) {
        this.className = clazz.getName();
        this.to = to;
        this.from = from;
        this.value = value;
        this.type = type;
        this.webSocketId = socketId;
    }

    public Address getTo() {
        return to;
    }

    public Address getFrom() {
        return from;
    }

    public Map<String, Object> getValue() {
        return value;
    }

    public void setValue(Map<String, Object> value) {
        this.value = value;
    }

    public MsgType getType() {
        return type;
    }

    public String getWebSocketId() {
        return webSocketId;
    }

}
