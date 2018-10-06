package atm.api;

import department.interfaces.ATMListener;
import exceptions.CellIsEmptyException;
import money.Currency;
import money.interfaces.Banknote;

import java.util.List;

public interface ATM extends ATMListener {
    List<Banknote> returnCash(long summa, Currency currency) throws CellIsEmptyException, CloneNotSupportedException;

    void putCash(List<? extends Banknote> money) throws CloneNotSupportedException;

    long getBalance();

    void fill() throws CloneNotSupportedException;

}
