import javax.management.MBeanServer;
import javax.management.NotificationEmitter;
import javax.management.ObjectName;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
/*
-agentlib:jdwp=transport=dt_socket,address=14000,server=y,suspend=n
-Xms1g
-Xmx1g
-XX:MaxMetaspaceSize=512m
-verbose:gc
-Xlog:gc*:file=gc_pid_%p.log
-Dcom.sun.management.jmxremote.port=15000
-Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=.
-XX:+UseG1GC
-Xdebug
 */

public class Main {
    private static final int SIZE = 10000;

    public static void main(String[] args) {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            emitter.addNotificationListener(new MyNotificationListener(), null, null);
        }
        try {
            ObjectName name = new ObjectName("ru.otus:type=Benchmark");
            Benchmark mbean = new Benchmark();
            mbs.registerMBean(mbean, name);
            mbean.setSize(SIZE);
            mbean.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}