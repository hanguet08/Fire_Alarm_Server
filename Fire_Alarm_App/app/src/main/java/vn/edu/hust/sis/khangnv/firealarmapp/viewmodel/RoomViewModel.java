package vn.edu.hust.sis.khangnv.firealarmapp.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.hust.sis.khangnv.firealarmapp.api.APIClient;
import vn.edu.hust.sis.khangnv.firealarmapp.api.APIInterface;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.RoomDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.ResponseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.resources_local.DataLocalManager;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;

public class RoomViewModel extends ViewModel {
    private MutableLiveData<List<RoomDto>> roomListLiveData;
    private List<RoomDto> mListRooms;
    private final String TAG = "Call Api Room";
    APIInterface apiService;

    // constructor
    public RoomViewModel() {
        this.roomListLiveData = new MutableLiveData<>();
        this.mListRooms = new ArrayList<RoomDto>();
        apiService = APIClient.getRetrofitClient().create(APIInterface.class);
    }

    // getter
    public MutableLiveData<List<RoomDto>> getRoomListLiveData() {
        return roomListLiveData;
    }

    // call API : get all room
    public void getRoomList(String houseId) {
        Call<ResponseDto<List<RoomDto>>> call = apiService.getListRooms(DataLocalManager.getAccessTokenServer(), houseId);

        call.enqueue(new Callback<ResponseDto<List<RoomDto>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<List<RoomDto>>> call, @NonNull Response<ResponseDto<List<RoomDto>>> response) {
                if(response.code() == 200) {
                    mListRooms = new ArrayList<RoomDto>(response.body().getData());
                    roomListLiveData.setValue(mListRooms);
                }
                else {
                    try {
                        String messageError = Utils.getErrorMsg(response.errorBody().string());
                        Utils.logError(TAG, response.code(), messageError);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error: " + e);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseDto<List<RoomDto>>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }

    // call API : insert one room
    public void insertRoom(RoomDto roomDto, Context mContext) {
        Call<ResponseDto<RoomDto>> call = apiService.insertRoom(DataLocalManager.getAccessTokenServer(), roomDto);
        call.enqueue(new Callback<ResponseDto<RoomDto>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<RoomDto>> call, @NonNull Response<ResponseDto<RoomDto>> response) {
                if(response.code() == 200) {
                    getRoomList(roomDto.getHouseId());
                    Utils.showToast(mContext, response.body().getMessage());
                }
                else {
                    try {
                        String messageError = Utils.getErrorMsg(response.errorBody().string());
                        Utils.logError(TAG, response.code(), messageError);
                        Utils.showToast(mContext, messageError);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error: " + e);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseDto<RoomDto>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }

    // call API : update one room
    public void updateRoom(RoomDto roomDto, String roomId, Context mContext) {
        Call<ResponseDto<RoomDto>> call = apiService.updateRoom(DataLocalManager.getAccessTokenServer(), roomDto, roomId);
        call.enqueue(new Callback<ResponseDto<RoomDto>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<RoomDto>> call, @NonNull Response<ResponseDto<RoomDto>> response) {
                if(response.code() == 200) {
                    getRoomList(roomDto.getHouseId());
                    Utils.showToast(mContext, response.body().getMessage());
                }
                else {
                    try {
                        String messageError = Utils.getErrorMsg(response.errorBody().string());
                        Utils.logError(TAG, response.code(), messageError);
                        Utils.showToast(mContext, messageError);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error: " + e);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseDto<RoomDto>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }


    // call API : delete one room
    public void deleteRoom(String roomId, String houseId, Context mContext) {
        Call<ResponseDto<Void>> call = apiService.deleteRoom(DataLocalManager.getAccessTokenServer(), roomId);
        call.enqueue(new Callback<ResponseDto<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<Void>> call, @NonNull Response<ResponseDto<Void>> response) {
                if(response.code() == 200) {
                    getRoomList(houseId);
                    Utils.showToast(mContext, response.body().getMessage());
                }
                else {
                    try {
                        String messageError = Utils.getErrorMsg(response.errorBody().string());
                        Utils.logError(TAG, response.code(), messageError);
                        Utils.showToast(mContext, messageError);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error: " + e);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseDto<Void>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }
}
