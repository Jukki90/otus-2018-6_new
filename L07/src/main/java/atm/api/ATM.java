package atm.api;

import exceptions.CellIsEmptyException;
import money.Currency;
import money.interfaces.Banknote;

import java.util.List;

public interface ATM {
    List<Banknote> returnCash(long summa, Currency currency) throws CellIsEmptyException;

    void putCash(List<? extends Banknote> money);

    long getBalance();

}
