package commands;

import atm.api.ATM;

@FunctionalInterface
public interface Command {
    long execute(ATM terminal);
}
