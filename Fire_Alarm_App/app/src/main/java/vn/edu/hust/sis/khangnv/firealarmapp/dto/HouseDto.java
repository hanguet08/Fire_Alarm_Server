package vn.edu.hust.sis.khangnv.firealarmapp.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.AbstractDto;

public class HouseDto extends AbstractDto implements Serializable {
    @SerializedName("_id")
    private String id;
    private String houseName;
    private int floor;
    private int members;
    private String address;
    private String userId;
    private int area;

    public HouseDto(String _id, String houseName, String address, int floor, int members, int area, String userId) {
        this.id = _id;
        this.houseName = houseName;
        this.floor = floor;
        this.members = members;
        this.address = address;
        this.userId = userId;
        this.area = area;
    }

    public HouseDto(String houseName, String address, int floor, int members, int area) {
        this.houseName = houseName;
        this.floor = floor;
        this.members = members;
        this.address = address;
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }
}
