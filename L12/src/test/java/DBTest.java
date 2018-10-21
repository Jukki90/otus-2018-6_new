import base.AddressDataSet;
import base.PhoneDataSet;
import base.UserDataSet;
import dbservice.DBService;
import dbservice.DBServiceHibernateImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class DBTest {
    DBService dbService;

    @Before
    public void setup() {
        dbService = new DBServiceHibernateImpl();
    }

    @Test
    public void creationTest() {

        List<PhoneDataSet> phones = new ArrayList<>();
        phones.add((new PhoneDataSet("+78885556644")));
        phones.add((new PhoneDataSet("+78885556645")));
        AddressDataSet address = new AddressDataSet("Санкт-Петербупг, ул. Лунных кошек, д.1");
        UserDataSet user = new UserDataSet("Кот Матроскин 3", 7, address, phones);
        dbService.save(user);
        UserDataSet res = dbService.load(1, UserDataSet.class);
        System.out.println(res);
        assertEquals(user.getName(), res.getName());
        assertEquals(user.getAge(), res.getAge());
        assertEquals(user.getAddress().getStreet(), res.getAddress().getStreet());
        assertTrue(user.getPhones().containsAll(res.getPhones()));
        assertTrue(res.getPhones().containsAll(user.getPhones()));
    }

    @After
    public void tearDown() throws Exception {
        dbService.close();
    }
}
