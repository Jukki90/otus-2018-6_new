package exceptions;

public class CellIsEmptyException extends Exception {
    public CellIsEmptyException(String message) {
        super(message);
    }

    public CellIsEmptyException() {
        super("Ячейка пуста!");
    }
}
