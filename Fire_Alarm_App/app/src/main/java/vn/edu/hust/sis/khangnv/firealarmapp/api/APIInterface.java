package vn.edu.hust.sis.khangnv.firealarmapp.api;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.DeviceDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.HouseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.NotificationDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.RoomDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.UserChangePasswordDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.ResponseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.authDto.LoginRequestDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.authDto.LoginResponseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.UserDto;

public interface APIInterface {
    // auth
    @POST("/auth/user/login")
    Call<ResponseDto<LoginResponseDto>> login(@Body LoginRequestDto loginDto);

    @POST("/auth/user/register")
    Call<ResponseDto<UserDto>> register(@Body UserDto userDto);

    @GET("/auth/user/logout")
    Call<ResponseDto<Void>> logout(@Header("Authorization") String token);

    // houses
    @GET("/api/v1/houses")
    Call<ResponseDto<List<HouseDto>>> getListHouses(@Header("Authorization") String token);

    @POST("/api/v1/houses")
    Call<ResponseDto<HouseDto>> insertHouse(@Header("Authorization") String token, @Body HouseDto houseDto);

    @PUT("/api/v1/houses/{id}")
    Call<ResponseDto<HouseDto>> updateHouse(@Header("Authorization") String token, @Body HouseDto houseDto, @Path("id") String id);

    @DELETE("/api/v1/houses/{id}")
    Call<ResponseDto<Void>> deleteHouse(@Header("Authorization") String token, @Path("id") String id);

    // rooms
    @GET("/api/v1/rooms")
    Call<ResponseDto<List<RoomDto>>> getListRooms(@Header("Authorization") String token, @Query("houseId") String houseId);

    @POST("/api/v1/rooms")
    Call<ResponseDto<RoomDto>> insertRoom(@Header("Authorization") String token, @Body RoomDto roomDto);

    @PUT("/api/v1/rooms/{id}")
    Call<ResponseDto<RoomDto>> updateRoom(@Header("Authorization") String token, @Body RoomDto roomDto, @Path("id") String id);

    @DELETE("/api/v1/rooms/{id}")
    Call<ResponseDto<Void>> deleteRoom(@Header("Authorization") String token, @Path("id") String id);

    // devices
    @GET("/api/v1/devices")
    Call<ResponseDto<List<DeviceDto>>> getListDevices(@Header("Authorization") String token, @Query("roomId") String roomId);

    @POST("/api/v1/devices")
    Call<ResponseDto<DeviceDto>> insertDevice(@Header("Authorization") String token, @Body DeviceDto deviceDto);

    @PUT("/api/v1/devices/{id}")
    Call<ResponseDto<DeviceDto>> updateDevice(@Header("Authorization") String token, @Body DeviceDto deviceDto, @Path("id") String deviceId);

    @DELETE("/api/v1/devices/{id}")
    Call<ResponseDto<Void>> deleteDevice(@Header("Authorization") String token, @Path("id") String id);

    // notifications
    @GET("/api/v1/notifications")
    Call<ResponseDto<List<NotificationDto>>> getListNotifications(@Header("Authorization") String tokenServer);

    @PUT("/api/v1/notifications/{id}")
    Call<ResponseDto<NotificationDto>> updateNotification(@Header("Authorization") String token, @Body NotificationDto notificationDto,  @Path("id") String notificationId);

    // users
    @GET("/api/v1/users/info")
    Call<ResponseDto<UserDto>> getUser(@Header("Authorization") String token);

    @POST("/api/v1/users/change-password")
    Call<ResponseDto<Void>> changePassword(@Header("Authorization") String token, @Body UserChangePasswordDto userChangePasswordDto);

    @POST("/api/v1/users/tokenFCM")
    Call<ResponseDto<Void>> saveTokenFCM(@Header("Authorization") String token, @Body UserDto userDto);
}
