package atm.api;

import exceptions.CellIsEmptyException;
import money.Currency;
import money.interfaces.Banknote;

public interface Cell {
    void putBanknote(Banknote banknote);

    Banknote getBanknote() throws CellIsEmptyException;

    int getTotalCapacity();

    int getCurrentCapacity();

    int getNominal();

    Currency getCurrency();
}
