import annotations.After;
import annotations.Before;
import annotations.Test;

public class SecondTest {

    public SecondTest() {
        System.out.println("-- Second test --");
    }

    @Before
    public void beforeMethod() {
        System.out.println("SecondTest: Before test");
    }

    @Test
    public void test() {
        System.out.println("SecondTest: Test started");
    }


    @After
    public void afterMethod() {
        System.out.println("SecondTest: After test");
    }

}
