package atm.api;

import exceptions.CellIsEmptyException;
import money.Currency;
import money.interfaces.Banknote;

public interface Cell extends Cloneable {
    void putBanknote(Banknote banknote);

    Banknote getBanknote() throws CellIsEmptyException;

    int getTotalCapacity();

    int getCurrentCapacity();

    int getNominal();

    Currency getCurrency();

    void fill();

    Cell clone() throws CloneNotSupportedException;
}
