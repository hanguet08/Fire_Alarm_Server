package vn.edu.hust.sis.khangnv.firealarmapp.utils;

public enum StatusSeenNotification {
    YES("YES"),
    NO("NO");

    private final String stringValue;

    public String getStringValue() {
        return stringValue;
    }

    StatusSeenNotification(String stringValue) {
        this.stringValue = stringValue;
    }

    static public boolean isValidStatusNotification(String statusNotification) {
        StatusSeenNotification[] statusSeenNotifications = StatusSeenNotification.values();
        for (StatusSeenNotification statusSeenNotification : statusSeenNotifications)
            if (statusSeenNotification.stringValue.equals(statusNotification))
                return true;
        return false;
    }
}
