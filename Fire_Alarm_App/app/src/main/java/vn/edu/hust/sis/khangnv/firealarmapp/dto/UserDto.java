package vn.edu.hust.sis.khangnv.firealarmapp.dto;

import com.google.gson.annotations.SerializedName;

import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.AbstractDto;

public class UserDto extends AbstractDto {
    @SerializedName("_id")
    private String id;
    private String email;
    private String password;
    private String fcmToken;
    private String fullName;
    private String phoneNumber;
    private String address;

    // Default constructor//
    public UserDto() {}

    public UserDto(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public UserDto(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    public UserDto(String email, String password, String fullName, String fcmToken) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.fcmToken = fcmToken;
    }

    public UserDto(String email, String password, String fcmToken, String fullName, String phoneNumber, String address) {
        this.email = email;
        this.password = password;
        this.fcmToken = fcmToken;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public UserDto(String _id, String email, String password, String fcmToken, String fullName, String phoneNumber, String address) {
        this.id = _id;
        this.email = email;
        this.password = password;
        this.fcmToken = fcmToken;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
