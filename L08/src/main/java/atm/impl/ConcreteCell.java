package atm.impl;

import atm.api.Cell;
import exceptions.CellIsEmptyException;
import money.Currency;
import money.impl.RoubleBanknote;
import money.interfaces.Banknote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConcreteCell implements Cell, Cloneable {
    private static final int CAPACITY = 1000;
    private int nominal;
    private Currency currency;
    private List<Banknote> list;

    public ConcreteCell(int nominal, Currency currency) {
        list = new ArrayList<>();
        this.nominal = nominal;
        this.currency = currency;
    }

    public void fill() {
        list = new ArrayList<>();
        switch (this.currency) {
            case RUB:
                for (int i = 0; i < CAPACITY; i++) {
                    list.add(RoubleBanknote.getByNominal(nominal));
                }
                break;
            default:
                throw new IllegalArgumentException("Filling atm by this type of currency is not implemented");
        }
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

    @Override
    public String toString() {
        return Arrays.asList(this.list).toString();
    }

    @Override
    public ConcreteCell clone() throws CloneNotSupportedException {
        ConcreteCell clonedObj = (ConcreteCell) super.clone();
        clonedObj.list = new ArrayList<>();
        for (int i = 0; i < this.list.size(); i++) {
            clonedObj.list.add(this.list.get(i));
        }

        return clonedObj;
    }
}
