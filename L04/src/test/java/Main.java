import core.TestRunner;

public class Main {
    public static void main(String[] args) {
        TestRunner.run(FirstTest.class, "First test");
        TestRunner.run(SecondTest.class);
    }
}
