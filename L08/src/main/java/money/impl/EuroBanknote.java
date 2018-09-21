package money.impl;


import money.Currency;
import money.interfaces.Banknote;

public enum EuroBanknote implements Banknote {
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    ONE_HUNDRED(100),
    TWO_HUNDREDS(200),
    FIVE_HUNDRED(500);

    private int nominal;

    @Override
    public int getNominal() {
        return this.nominal;
    }

    EuroBanknote(int nominal) {
        this.nominal = nominal;
    }

    public Currency getCurrency() {
        return Currency.EUR;
    }

}
