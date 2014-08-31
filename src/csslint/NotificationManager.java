package csslint;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

public class NotificationManager {
    public static void showError (String message) {
        show(message, NotificationType.ERROR);
    }

    public static void showWarning (String message) {
        show(message, NotificationType.WARNING);
    }

    private static void show (String message, NotificationType type) {
        Notification notification = new Notification(
                Bundle.message("notification.group"),
                Bundle.message("notification.title"),
                message,
                type
        );
        Notifications.Bus.notify(notification);
    }
}
