import atm.api.ATM;
import atm.impl.RoubleATM;
import department.Department;
import money.impl.RoubleBanknote;
import money.interfaces.Banknote;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class DepartmentTest {
    private static Logger logger = LoggerFactory.getLogger(DepartmentTest.class);

    @Test
    public void balanceTest(){
        List<ATM> atmList = new ArrayList<>();
        ATM atm1 = null;
        try {
            atm1 = new RoubleATM();

        atm1.fill();
        atmList.add(atm1);
        atmList.add(atm1);
        Department department = new Department(atmList);
        long depBalance = department.getSummaryBalance();
        logger.info("Баланс ATM в отделении = {}", depBalance);
        assertTrue("Не сошелся баланс при подсчете разными способами",atm1.getBalance()*2 == depBalance);
        } catch (CloneNotSupportedException e) {
            Assert.fail("Возникла ошибка при попытке клонировать текущее состояние ячеек");
        }
        }

    @Test
    public void restoreStateTest() {
        List<ATM> atmList = new ArrayList<>();
        try {
            ATM atm1 = new RoubleATM();
            atmList.add(atm1);

            Department department = new Department(atmList);

            List<Banknote> cash = new ArrayList<>();
            cash.add(RoubleBanknote.FIFTY);
            department.getAtms().get(0).putCash(cash);

            assertTrue("Баланс не увеличился на ожидаемую сумму после пополнения", 50 == department.getSummaryBalance());

            department.restoreState();
            logger.info("Баланс после сброса состояния = {}", department.getSummaryBalance());
            assertTrue("Баланс не сбросился!", 0 == department.getSummaryBalance());
        } catch (CloneNotSupportedException e) {
            Assert.fail("Возникла ошибка при попытке клонировать текущее состояние ячеек");
        }
    }

        @Test
        public void restoreState02Test(){
            List<ATM> atmList = new ArrayList<>();
            try {
                logger.info("Выставляем ненулевой изначальный баланс в банкоматах");
                List<Banknote> preconditionCash = new ArrayList<>();
                preconditionCash.add(RoubleBanknote.TEN);
                ATM atm1 = new RoubleATM(preconditionCash);
                atmList.add(atm1);
                atmList.add(new RoubleATM());

                Department department = new Department(atmList);

                List<Banknote> cash = new ArrayList<>();
                cash.add(RoubleBanknote.ONE_HUNDRED);
                department.getAtms().get(0).putCash(cash);
                department.getAtms().get(0).putCash(cash);
                department.getAtms().get(1).putCash(cash);
                assertTrue("Пополнение на ожидаемую сумму не произошло",310==department.getSummaryBalance());
                department.restoreState();
                logger.info("Баланс после сброса состояния = {}", department.getSummaryBalance());
                assertTrue("Баланс не сбросился!", 10 == department.getSummaryBalance());
            } catch (CloneNotSupportedException e) {
                Assert.fail("Возникла ошибка при попытке клонировать текущее состояние ячеек");
            }

    }
}
