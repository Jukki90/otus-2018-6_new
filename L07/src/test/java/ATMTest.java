import atm.api.ATM;
import atm.impl.RoubleATM;
import exceptions.CellIsEmptyException;
import money.Currency;
import money.impl.EuroBanknote;
import money.impl.RoubleBanknote;
import money.interfaces.Banknote;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ATMTest {
    private static Logger logger = LoggerFactory.getLogger(ATMTest.class);
    private ATM atm;

    @Before
    public void init() {
        atm = new RoubleATM();
    }

    @Test
    public void addMoneyTest() {
        logger.info("Тест на пополнение несколькими купюрами:");
        List<RoubleBanknote> money = new ArrayList<>();
        money.add(RoubleBanknote.TEN);
        money.add(RoubleBanknote.FIFTY);
        atm.putCash(money);
        long balance = atm.getBalance();
        logger.info("Баланс равен = {}", balance);
        assertEquals("Баланс после пополнения отличен от ожидаемого! ", 60L, balance);
    }

    @Test
    public void getMoneyTest() throws CellIsEmptyException {
        logger.info("Тест на выдачу нескольких купюр:");
        fillATM();
        List<Banknote> money = atm.returnCash(110, Currency.RUB);
        assertTrue(money.contains(RoubleBanknote.ONE_HUNDRED));
        assertTrue(money.contains(RoubleBanknote.TEN));

        logger.info("Выданные купюры: {}", money.toString());
    }


    @Test(expected = CellIsEmptyException.class)
    public void getMoney02Test() throws CellIsEmptyException {
        logger.info("Тест на невозможность получения суммы имеющимися купюрами:");
        fillATM();
        atm.returnCash(112, Currency.RUB);
    }

    @Test(expected = CellIsEmptyException.class)
    public void notEnoughMoneyTest() throws CellIsEmptyException {
        logger.info("Тест на получение купюр при недостаточности средств в банкомате:");
        atm.returnCash(10, Currency.RUB);
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectCurrencyTest() {
        logger.info("Тест на невозможность положить другие валюты:");
        ArrayList<Banknote> cache = new ArrayList<>();
        cache.add(EuroBanknote.FIVE);
        atm.putCash(cache);
    }

    @Test
    public void getBalanceTest() {
        logger.info("Тест на получение баланса:");
        long balance = atm.getBalance();
        logger.info("До загрузки купюр в банкомат баланс = {}", balance);
        assertEquals(0L, balance);
        //atm.fillCells();
        ArrayList<Banknote> atmBalance = new ArrayList<>();
        atmBalance.add(RoubleBanknote.FIFTY);
        atm.putCash(atmBalance);
        balance = atm.getBalance();
        logger.info("После загрузки купюр в банкомат баланс = {}", balance);
        assertEquals(50L, balance);
    }

    private void fillATM() {
        ArrayList<Banknote> atmBalance = new ArrayList<>();
        atmBalance.add(RoubleBanknote.FIVE_THOUSANDS);
        atmBalance.add(RoubleBanknote.TWO_THOUSANDS);
        atmBalance.add(RoubleBanknote.ONE_THOUSAND);
        atmBalance.add(RoubleBanknote.FIVE_HUNDRED);
        atmBalance.add(RoubleBanknote.ONE_HUNDRED);
        atmBalance.add(RoubleBanknote.FIFTY);
        atmBalance.add(RoubleBanknote.TEN);
        atm.putCash(atmBalance);
    }

}
