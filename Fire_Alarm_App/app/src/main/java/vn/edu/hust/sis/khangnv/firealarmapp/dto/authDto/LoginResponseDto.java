package vn.edu.hust.sis.khangnv.firealarmapp.dto.authDto;

public class LoginResponseDto {
    private String userId;
    private String accessToken;
    private String refreshToken;
    private String fcmToken;

    public LoginResponseDto(String accessToken, String refreshToken, String userId, String fcmToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.fcmToken = fcmToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
