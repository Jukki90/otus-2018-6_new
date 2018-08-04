public class Statistics {
    private long youngPerPeriod;
    private long oldPerPeriod;
    private long youngSummary;
    private long oldSummary;
    private long startTime;
    private static Statistics instance = new Statistics();

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
        startTime = System.currentTimeMillis();
    }


    public void reset() {
        youngPerPeriod = 0;
        oldPerPeriod = 0;
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
        System.out.println("--------------------------------------------------------------");
        System.out.println("Young gen per minute = " + youngPerPeriod + "ms");
        System.out.println("Old gen per minute = " + oldPerPeriod + "ms");
        System.out.println("Young gen summary = " + youngSummary + "ms");
        System.out.println("Old gen summary = " + oldSummary + "ms");
    }
}
