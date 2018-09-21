package memento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Originator {
    private static Logger logger = LoggerFactory.getLogger(Originator.class);
    private List<Memento> stack = new ArrayList<>();

    public void saveState(State state) {
        stack.add(new Memento(state));
    }


    public State restoreStartingState() {
        return stack.get(0).getState();
    }

}
