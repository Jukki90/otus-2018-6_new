package atm.api;

import exceptions.CellIsEmptyException;
import exceptions.CellOverFilledException;
import money.Currency;
import money.interfaces.Banknote;

public interface Cell {
    void putBanknote(Banknote banknote) throws CellOverFilledException;

    Banknote getBanknote() throws CellIsEmptyException;

    int getTotalCapacity();

    int getCurrentCapacity();

    int getNominal();

    Currency getCurrency();
}
