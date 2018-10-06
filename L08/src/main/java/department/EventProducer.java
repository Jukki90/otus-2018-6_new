package department;

import department.interfaces.ATMListener;

import java.util.ArrayList;
import java.util.List;

public class EventProducer {
    public EventProducer() {

    }

    private final List<ATMListener> listeners = new ArrayList<>();

    public void addListener(ATMListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ATMListener listener) {
        listeners.remove(listener);
    }

    public void event() {
        listeners.forEach(listener -> listener.restoreState());
    }


}
