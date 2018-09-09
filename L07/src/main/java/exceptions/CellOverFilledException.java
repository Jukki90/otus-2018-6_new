package exceptions;

public class CellOverFilledException extends Exception {
    public CellOverFilledException(String message) {
        super(message);
    }

    public CellOverFilledException() {
        super("Ячейка банкомата переполнена!");
    }
}
