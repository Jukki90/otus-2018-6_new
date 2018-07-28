import annotations.After;
import annotations.Before;
import annotations.Test;
import core.TestRunner;

public class MyTest {

    @Before
    public static void beforeMethod() {
        System.out.println("Before test");
    }

    @Test
    public void test01() {
        System.out.println("Test 01 started");
    }

    @Test
    public void test02() {
        System.out.println("Test 02 started");
    }


    @After
    public void afterMethod() {
        System.out.println("After test");
    }


    public static void main(String[] args) {
        MyTest test = new MyTest();
        TestRunner.run(test);
    }
}
