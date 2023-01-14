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
import vn.edu.hust.sis.khangnv.firealarmapp.dto.DeviceDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.ResponseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.resources_local.DataLocalManager;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;

public class DeviceViewModel extends ViewModel {
    private MutableLiveData<List<DeviceDto>> deviceListLiveData;
    private List<DeviceDto> mListDevices;
    private final String TAG = "Call Api Device";
    APIInterface apiService;

    // constructor
    public DeviceViewModel() {
        this.deviceListLiveData = new MutableLiveData<>();
        this.mListDevices = new ArrayList<DeviceDto>();
        apiService = APIClient.getRetrofitClient().create(APIInterface.class);
    }

    // getter
    public MutableLiveData<List<DeviceDto>> getDeviceListLiveData() {
        return this.deviceListLiveData;
    }

    // call API : get all device
    public void getDeviceList(String roomId) {
        Call<ResponseDto<List<DeviceDto>>> call = apiService.getListDevices(DataLocalManager.getAccessTokenServer(), roomId);

        call.enqueue(new Callback<ResponseDto<List<DeviceDto>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<List<DeviceDto>>> call, @NonNull Response<ResponseDto<List<DeviceDto>>> response) {
                if(response.code() == 200) {
                    mListDevices = new ArrayList<DeviceDto>(response.body().getData());
                    deviceListLiveData.setValue(mListDevices);
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
            public void onFailure(Call<ResponseDto<List<DeviceDto>>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }

    // call API : insert one device
    public void insertDevice(DeviceDto deviceDto, Context mContext) {
        Call<ResponseDto<DeviceDto>> call = apiService.insertDevice(DataLocalManager.getAccessTokenServer(), deviceDto);
        call.enqueue(new Callback<ResponseDto<DeviceDto>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<DeviceDto>> call, @NonNull Response<ResponseDto<DeviceDto>> response) {
                if(response.code() == 200) {
                    getDeviceList(deviceDto.getRoomId());
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
            public void onFailure(Call<ResponseDto<DeviceDto>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }

    // call API : update one device
    public void updateDevice(DeviceDto deviceDto, String deviceId, Context mContext) {
        Call<ResponseDto<DeviceDto>> call = apiService.updateDevice(DataLocalManager.getAccessTokenServer(), deviceDto, deviceId);
        call.enqueue(new Callback<ResponseDto<DeviceDto>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<DeviceDto>> call, @NonNull Response<ResponseDto<DeviceDto>> response) {
                if(response.code() == 200) {
                    getDeviceList(deviceDto.getRoomId());
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
            public void onFailure(Call<ResponseDto<DeviceDto>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }

    // call API : delete one device
    public void deleteDevice(String deviceId, String roomId, Context mContext) {
        Call<ResponseDto<Void>> call = apiService.deleteDevice(DataLocalManager.getAccessTokenServer(), deviceId);
        call.enqueue(new Callback<ResponseDto<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<Void>> call, @NonNull Response<ResponseDto<Void>> response) {
                if(response.code() == 200) {
                    getDeviceList(roomId);
                    Utils.showToast(mContext, response.body().getMessage());
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
            public void onFailure(Call<ResponseDto<Void>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }
}
