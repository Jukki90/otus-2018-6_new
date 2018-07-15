import java.lang.management.ManagementFactory;
import java.util.function.Supplier;

public class MemoryMeasurer {


    public <T> Object measure(Supplier<T> supplier, int size) throws InterruptedException {
        System.out.println("pid" + ManagementFactory.getRuntimeMXBean().getName());
        System.out.println("--- start the loop for " + supplier.get().getClass().getSimpleName());
        long mem = getMem();
        System.out.println("Mem: " + mem);
        Object[] array = new Object[size];

        long mem2 = getMem();
        System.out.println("Mem2: " + mem2);
        System.out.println("Ref size: " + (mem2 - mem) / array.length);

        for (int i = 0; i < size; i++) {
            array[i] = supplier.get();
        }

        long mem3 = getMem();
        System.out.println("Element size " + (mem3 - mem2) / array.length);
        array = null;

        System.out.println("Array is ready for GC");
        Thread.sleep(1000);
        return array;
    }


    private long getMem() throws InterruptedException {
        System.gc();
        Thread.sleep(10);
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
