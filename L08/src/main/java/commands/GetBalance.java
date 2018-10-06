package commands;

import atm.api.ATM;

public class GetBalance implements Command {
    @Override
    public long execute(ATM terminal) {
        return terminal.getBalance();
    }
}
