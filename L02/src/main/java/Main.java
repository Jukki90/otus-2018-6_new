import java.util.ArrayList;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int size = 20_000_000;
        MemoryMeasurer memoryMeasurer = new MemoryMeasurer();
        //while (true) {
        memoryMeasurer.measure(() -> new String(""), size);
        memoryMeasurer.measure(() -> new String(new char[]{}), size);
        memoryMeasurer.measure(() -> new String(new byte[0]), size);
        memoryMeasurer.measure(() -> new String[]{}, size);
        memoryMeasurer.measure(() -> new int[0], size);
        memoryMeasurer.measure(() -> new int[20], size);
        memoryMeasurer.measure((Supplier<ArrayList>) ArrayList::new, size);
        memoryMeasurer.measure(() -> new MyClass(), size);

        // }
    }
}
