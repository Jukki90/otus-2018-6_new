import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

public class MyNotificationListener implements NotificationListener {
    private Statistics statistics = Statistics.getInstance();

    @Override
    public void handleNotification(Notification notification, Object handback) {
        {
            if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                GarbageCollectionNotificationInfo gcInfo = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                StringBuilder builder = new StringBuilder();
                String gctype = gcInfo.getGcAction();
                if ("end of minor GC".equals(gctype)) {
                    builder.append("Young Gen GC: ");
                    statistics.addYoung(gcInfo.getGcInfo().getDuration());
                } else if ("end of major GC".equals(gctype)) {
                    builder.append("Old Gen GC: ");
                    statistics.addOld(gcInfo.getGcInfo().getDuration());
                }
                statistics.incNumberOfAssemblies();
                builder.append(":").append(" start ").append(gcInfo.getGcInfo().getStartTime()).append(", duration: ").append(gcInfo.getGcInfo().getDuration());
                System.out.println(builder.toString());
            }
        }
    }

}
