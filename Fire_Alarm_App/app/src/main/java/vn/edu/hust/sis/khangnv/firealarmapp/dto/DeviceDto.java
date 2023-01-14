package vn.edu.hust.sis.khangnv.firealarmapp.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.AbstractDto;

public class DeviceDto extends AbstractDto implements Serializable {
    @SerializedName("_id")
    private String id;
    private String deviceName;
    private String status;
    private String position;
    private String roomId;
    private int deviceType;


    // constructor get device
    public DeviceDto(String id, String deviceName, String status, String position, String roomId, int deviceType) {
        this.id = id;
        this.deviceName = deviceName;
        this.status = status;
        this.position = position;
        this.roomId = roomId;
        this.deviceType = deviceType;
    }

    // constructor create device
    public DeviceDto(String deviceName, String position, String status, String roomId, int deviceType) {
        this.deviceName = deviceName;
        this.status = status;
        this.position = position;
        this.roomId = roomId;
        this.deviceType = deviceType;
    }

    public String getId() {
        return id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRoomId() {
        return roomId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}
