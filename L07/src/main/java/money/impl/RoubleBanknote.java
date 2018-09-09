package money.impl;

import money.Currency;
import money.interfaces.Banknote;


public enum RoubleBanknote implements Banknote {
    TEN(10),
    FIFTY(50),
    ONE_HUNDRED(100),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000),
    TWO_THOUSANDS(2000),
    FIVE_THOUSANDS(5000);

    private int nominal;

    public int getNominal() {
        return nominal;
    }

    @Override
    public Currency getCurrency() {
        return Currency.RUB;
    }

    RoubleBanknote(int nominal) {
        this.nominal = nominal;
    }
}
