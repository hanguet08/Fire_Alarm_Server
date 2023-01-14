package vn.edu.hust.sis.khangnv.firealarmapp.utils;

public enum DeviceType {
    FLAME_SENSOR("FLAME_SENSOR", 1),
    MQ2_SENSOR("MQ2_SENSOR", 2),
    DHT11_SENSOR("DHT11_SENSOR", 3);

    private String stringValue;
    private int intValue;

    public String getStringValue() {
        return stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    private DeviceType(String stringValue, int intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    static public boolean isValidDeviceType(int typeValue) {
        DeviceType[] deviceTypes = DeviceType.values();
        for (DeviceType deviceType : deviceTypes)
            if (deviceType.intValue == typeValue)
                return true;
        return false;
    }
}
