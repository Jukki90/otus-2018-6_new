package atm.impl;

import atm.api.ATM;
import atm.api.Cell;
import exceptions.CellIsEmptyException;
import exceptions.CellOverFilledException;
import money.Currency;
import money.impl.RoubleBanknote;
import money.interfaces.Banknote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RoubleATM implements ATM {
    private static Logger logger = LoggerFactory.getLogger(RoubleATM.class);
    private List<ConcreteCell> roubleCells = new ArrayList<ConcreteCell>();


    public RoubleATM() {
        for (RoubleBanknote type : RoubleBanknote.values()) {
            roubleCells.add(new ConcreteCell(type.getNominal(), type.getCurrency()));
        }
    }


    public List<Banknote> returnCash(long summa, Currency currency) throws CellIsEmptyException {
        if (currency.equals(Currency.RUB)) {
            return returnRoubleCash(summa);
        } else {
            throw new IllegalArgumentException("Банкомат не выдает валюту");
        }
    }

    private List<Banknote> returnRoubleCash(long summa) throws CellIsEmptyException {
        if (summa > getRoubleBalance()) {
            throw new CellIsEmptyException("В банкомате недостаточно средств!");
        }
        Collections.sort(roubleCells, cellsComparator);
        List<Banknote> result = new ArrayList<>();
        long remainder = 0;
        for (int i = 0; i < roubleCells.size(); i++) {
            ConcreteCell cell = roubleCells.get(i);
            int nominal = cell.getNominal();
            long number = summa / nominal;
            for (int j = 0; j < number; j++) {
                result.add(cell.getBanknote());
            }
            remainder = summa % nominal;
            summa = remainder;
        }
        if (remainder > 0) {
            throw new CellIsEmptyException("Нельзя выдать сумму имеющимися купюрами!");
        }
        return result;
    }


    public void putCash(List<? extends Banknote> money) throws CellOverFilledException {
        for (int j = 0; j < money.size(); j++) {
            Banknote banknote = money.get(j);
            if (banknote instanceof RoubleBanknote) {
                putRoubleBanknote((RoubleBanknote) banknote);
            } else {
                throw new IllegalArgumentException("Банкомат принимает только рублевые купюры");
            }
        }
    }


    private void putRoubleBanknote(RoubleBanknote banknote) throws CellOverFilledException {
        for (int i = 0; i < roubleCells.size(); i++) {
            if (roubleCells.get(i).getNominal() == banknote.getNominal()) {
                roubleCells.get(i).putBanknote(banknote);
            }
        }
    }


    public long getBalance() {
        return getRoubleBalance();
    }

    private long getRoubleBalance() {
        long summa = 0;
        for (int i = 0; i < roubleCells.size(); i++) {
            ConcreteCell item = roubleCells.get(i);
            summa += item.getCurrentCapacity() * item.getNominal();
        }
        return summa;
    }

    private Comparator cellsComparator = new Comparator<Cell>() {
        @Override
        public int compare(Cell o1, Cell o2) {
            int res = o2.getNominal() - o1.getNominal();
            return res / Math.abs(res);
        }
    };

}
