package service;

import model.Notification;
import java.util.*;

public class NotificationService {
    private static final List<Notification> NOTIFICATIONS = new ArrayList<Notification>();
    private static boolean loaded = false;

    public NotificationService() {
        loadIfNeeded();
    }

    private static synchronized void loadIfNeeded() {
        if (loaded) return;
        NOTIFICATIONS.clear();
        NOTIFICATIONS.addAll(DataStore.loadNotifications());
        loaded = true;
    }

    private static synchronized void persist() {
        DataStore.saveNotifications(NOTIFICATIONS);
    }

    public synchronized void addNotification(int targetId, String message) {
        NOTIFICATIONS.add(new Notification(targetId, message));
        persist();
    }

    public synchronized List<Notification> getAllNotifications() {
        return new ArrayList<Notification>(NOTIFICATIONS);
    }

    public synchronized List<Notification> getNotificationsForStudent(int studentId) {
        List<Notification> list = new ArrayList<Notification>();
        for (Notification n : NOTIFICATIONS) {
            if (n.getTargetId() == -1 || n.getTargetId() == studentId) {
                list.add(n);
            }
        }
        return list;
    }

    public synchronized void removeNotificationsForStudent(int studentId) {
        Iterator<Notification> it = NOTIFICATIONS.iterator();
        while (it.hasNext()) {
            if (it.next().getTargetId() == studentId) it.remove();
        }
        persist();
    }

    public synchronized void clearAll() {
        NOTIFICATIONS.clear();
        persist();
    }

    public synchronized void refreshFromDisk() {
        loaded = false;
        loadIfNeeded();
    }
}
