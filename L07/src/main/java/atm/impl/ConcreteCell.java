package atm.impl;

import atm.api.Cell;
import exceptions.CellIsEmptyException;
import money.Currency;
import money.interfaces.Banknote;

import java.util.ArrayList;
import java.util.List;

public class ConcreteCell implements Cell {
    private static final int CAPACITY = 1000;
    private int nominal;
    private Currency currency;
    private List<Banknote> list;

    public ConcreteCell(int nominal, Currency currency) {
        list = new ArrayList<>();
        this.nominal = nominal;
        this.currency = currency;
    }


    public void putBanknote(Banknote banknote) {

        if (list.size() == CAPACITY) {
            throw new IndexOutOfBoundsException("Невозможно поместить больше купюр в ячейку!");
        }
        if (banknote.getNominal() != nominal) {
            throw new IllegalArgumentException("Номинал купюры отличен от ожидаемого!");
        }
        if (banknote.getCurrency() != currency) {
            throw new IllegalArgumentException("Валюта купюры отлична от ожидаемой!");
        }
        list.add(banknote);
    }

    @Override
    public Banknote getBanknote() throws CellIsEmptyException {
        if (list.size() == 0) {
            throw new CellIsEmptyException();
        }
        Banknote banknote = list.get(list.size() - 1);
        list.remove(list.size() - 1);
        return banknote;
    }


    @Override
    public int getTotalCapacity() {
        return CAPACITY;
    }

    @Override
    public int getCurrentCapacity() {
        return list.size();
    }

    @Override
    public int getNominal() {
        return nominal;
    }

    @Override
    public Currency getCurrency() {
        return this.currency;
    }
}
