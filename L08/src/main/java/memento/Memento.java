package memento;

public class Memento {
    private final State state;

    public Memento(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}