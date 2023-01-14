package vn.edu.hust.sis.khangnv.firealarmapp.utils;

public enum DeviceStatus {
    ON("ON"),
    OFF("OFF");

    private final String stringValue;

    public String getStringValue() {
        return stringValue;
    }

    DeviceStatus(String stringValue) {
        this.stringValue = stringValue;
    }

    static public boolean isValidStatus(String status) {
        DeviceStatus[] deviceStatuses = DeviceStatus.values();
        for (DeviceStatus deviceStatus : deviceStatuses)
            if (deviceStatus.stringValue.equals(status))
                return true;
        return false;
    }
}
