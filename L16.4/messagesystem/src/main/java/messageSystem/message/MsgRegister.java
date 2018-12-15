package messageSystem.message;

import messageSystem.Address;


import java.util.logging.Logger;

public class MsgRegister extends Msg {
    private static final Logger LOGGER = Logger.getLogger(MsgRegister.class.getName());

    public MsgRegister(Address from, Address to) {
        super(MsgType.REGISTER, MsgRegister.class, from, to, null);
    }
}
