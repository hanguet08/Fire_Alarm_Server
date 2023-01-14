package vn.edu.hust.sis.khangnv.firealarmapp.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.AbstractDto;

public class RoomDto extends AbstractDto implements Serializable {
    @SerializedName("_id")
    private String id;
    private String roomName;
    private String position;
    private int area;
    private String owner;
    private String houseId;

    public String getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getHouseId() {
        return houseId;
    }

    public RoomDto(String id, String roomName, String position, int area, String owner, String houseId) {
        this.id = id;
        this.roomName = roomName;
        this.position = position;
        this.area = area;
        this.owner = owner;
        this.houseId = houseId;
    }

    public RoomDto(String roomName, String position, int area, String owner, String houseId) {
        this.roomName = roomName;
        this.position = position;
        this.area = area;
        this.owner = owner;
        this.houseId = houseId;
    }

    public RoomDto(String name, String position, int area, String owner) {
        this.roomName = name;
        this.position = position;
        this.area = area;
        this.owner = owner;
    }
}
