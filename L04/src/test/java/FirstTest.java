import annotations.After;
import annotations.Before;
import annotations.Test;

public class FirstTest {

    @Before
    public void before01Method() {
        System.out.println("FirstTest:Before test 01");
    }

    @Before
    public static void before02Method() {
        System.out.println("FirstTest:Before test 02");
    }

    @Test
    public void test01() {
        System.out.println("FirstTest: Test 01 started");
    }

    @Test
    public static void test02() {
        System.out.println("FirstTest: Test 02 started");
    }


    @After
    public void after01Method() {
        System.out.println("FirstTest: After test 01");
    }

    @After
    public static void after02Method() {
        System.out.println("FirstTest: After test 02");
    }

    public FirstTest(String name) {
        System.out.println("--" + name + "--");
    }
}
