import java.util.ArrayList;

public class Benchmark implements BenchmarkMBean {
    private volatile int size = 0;
    private Statistics statistics = Statistics.getInstance();

    @SuppressWarnings("InfiniteLoopStatement")
    void run() {
        System.out.println("Starting the loop");
        ArrayList<String> arr = new ArrayList();
        long period = 60_000;
        long timer = System.currentTimeMillis();
        while (true) {
            for (int i = 0; i < size; i++) {
                arr.add(new String());
            }

            for (int i = 0; i < size / 2; i++) {
                arr.remove(arr.size() - 1);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if ((System.currentTimeMillis() - timer) > period) {
                statistics.printResult();
                statistics.reset();
;
                timer = System.currentTimeMillis();
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

}
