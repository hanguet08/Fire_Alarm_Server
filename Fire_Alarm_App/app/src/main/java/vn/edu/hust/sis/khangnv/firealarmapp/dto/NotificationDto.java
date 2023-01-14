package vn.edu.hust.sis.khangnv.firealarmapp.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.AbstractDto;

public class NotificationDto extends AbstractDto implements Serializable {
    @SerializedName("_id")
    private String id;
    private String device;
    private String statusSeen;
    private String content;
    private String userId;
    private String dateTime;

    public String getId() {
        return id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getStatusSeen() {
        return statusSeen;
    }

    public void setStatusSeen(String statusSeen) {
        this.statusSeen = statusSeen;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }


    public NotificationDto(String id, String device, String statusSeen, String content, String userId, String dateTime) {
        this.id = id;
        this.device = device;
        this.statusSeen = statusSeen;
        this.content = content;
        this.userId = userId;
        this.dateTime = dateTime;
    }

    public NotificationDto(String device, String statusSeen, String content) {
        this.device = device;
        this.statusSeen = statusSeen;
        this.content = content;
    }

    public NotificationDto(String statusSeen, String content) {
        this.statusSeen = statusSeen;
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
