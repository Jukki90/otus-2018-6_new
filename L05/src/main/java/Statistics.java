public class Statistics {
    private long youngPerPeriod;
    private long oldPerPeriod;
    private long youngSummary;
    private long oldSummary;
    private long numberOfAssemblies;
    private static Statistics instance = new Statistics();
    private final long startTime;
    private int numberOfCycle;

    public static synchronized Statistics getInstance() {
        if (instance == null) {
            instance = new Statistics();
        }
        return instance;
    }

    private Statistics() {
        youngPerPeriod = 0;
        oldPerPeriod = 0;
        youngSummary = 0;
        oldSummary = 0;
        numberOfAssemblies = 0;
        startTime = System.currentTimeMillis();
        numberOfCycle = 1;
    }


    public void reset() {
        youngPerPeriod = 0;
        oldPerPeriod = 0;
    }

    public void incNumberOfAssemblies() {
        numberOfAssemblies++;
        numberOfAssemblies++;
    }

    public void addYoung(long value) {
        this.youngSummary += value;
        this.youngPerPeriod += value;
    }


    public void addOld(long value) {
        this.oldPerPeriod += value;
        this.oldSummary += value;
    }


    public void printResult() {
        System.out.println("Цикл " + numberOfCycle);
        numberOfCycle++;
        System.out.println("Young gen per last minute = " + youngPerPeriod + "ms");
        long avgYoung = youngSummary * 60000 / (System.currentTimeMillis() - startTime);
        System.out.println("Young gen per minute avg = " + avgYoung + "ms");
        System.out.println("Young gen summary = " + youngSummary + " ms");
        System.out.println("Old gen per last minute = " + oldPerPeriod + " ms");
        long avgOld = oldSummary * 60000 / (System.currentTimeMillis() - startTime);
        System.out.println("Old gen per minute avg = " + avgOld + "ms");
        System.out.println("Old per minute avg = " + oldSummary + " ms");
        System.out.println("Кол-во сборок GC = " + numberOfAssemblies);
    }
}
