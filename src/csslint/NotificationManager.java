package csslint;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

public class NotificationManager {
    public static void showError (String message) {
        Notification notification = new Notification(
                Bundle.message("notification.group"),
                Bundle.message("notification.title"),
                message,
                NotificationType.ERROR
        );
        Notifications.Bus.notify(notification);
    }
}
