package memento;

import atm.api.Cell;

import java.util.ArrayList;
import java.util.List;

public class State {
    private List<Cell> arr;

    public List<Cell> getCells() {
        return arr;
    }

    public State(List<Cell> cells) throws CloneNotSupportedException {
        List<Cell> list = cells;
        arr = createListClone(list);
        int i = 0;
    }


    public State(State state) throws CloneNotSupportedException {
        List<Cell> cells = state.getCells();
        arr = createListClone(cells);
    }

    private List<Cell> createListClone(List<Cell> cells) throws CloneNotSupportedException {
        ArrayList<Cell> qwe = new ArrayList<>();
        for (int i = 0; i < cells.size(); i++) {
            qwe.add((Cell) cells.get(i).clone());
        }
        return qwe;
    }

    @Override
    public String toString() {
        return "State{" +
                "array=" + (arr == null ? null : arr) +
                '}';
    }
}
