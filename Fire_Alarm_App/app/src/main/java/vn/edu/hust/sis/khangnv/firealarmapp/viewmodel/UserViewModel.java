package vn.edu.hust.sis.khangnv.firealarmapp.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.hust.sis.khangnv.firealarmapp.api.APIClient;
import vn.edu.hust.sis.khangnv.firealarmapp.api.APIInterface;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.UserChangePasswordDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.UserDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.ResponseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.resources_local.DataLocalManager;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;

public class UserViewModel extends ViewModel {
    private MutableLiveData<UserDto> userDtoLiveData;
    private MutableLiveData<String> msgChangePasswordLiveData;
    private UserDto mUserDto;
    private String msgChangPassword = "";
    private final String TAG = "Call Api User";
    APIInterface apiService;

    public UserViewModel() {
        this.msgChangePasswordLiveData = new MutableLiveData<>();
        this.userDtoLiveData = new MutableLiveData<>();
        this.apiService = APIClient.getRetrofitClient().create(APIInterface.class);
    }

    public MutableLiveData<String> getMsgChangePasswordLiveData() {
        return msgChangePasswordLiveData;
    }

    // getter
    public MutableLiveData<UserDto> getUserDtoLiveData() {
        return userDtoLiveData;
    }

    // call API : get user
    public void getUser() {
        Call<ResponseDto<UserDto>> call = apiService.getUser(DataLocalManager.getAccessTokenServer());

        call.enqueue(new Callback<ResponseDto<UserDto>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull Call<ResponseDto<UserDto>> call, @NonNull Response<ResponseDto<UserDto>> response) {
                if(response.code() == 200) {
                    mUserDto = response.body().getData();
                    userDtoLiveData.setValue(mUserDto);
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
            public void onFailure(@NonNull Call<ResponseDto<UserDto>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }

    // call API : update tokenFCM of user
    public void saveTokenFCM(UserDto userDto) {
        Call<ResponseDto<Void>> call = apiService.saveTokenFCM(DataLocalManager.getAccessTokenServer(), userDto);
        call.enqueue(new Callback<ResponseDto<Void>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull Call<ResponseDto<Void>> call, @NonNull Response<ResponseDto<Void>> response) {
                if(response.code() == 200) {
                    Utils.logMessage(TAG, "200", "Save tokenFCM to user success!");
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
            public void onFailure(@NonNull Call<ResponseDto<Void>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }


    // call API : change password user
    public void changePassword(UserChangePasswordDto userChangePasswordDto, Context mContext) {
        Call<ResponseDto<Void>> call = apiService.changePassword(DataLocalManager.getAccessTokenServer(), userChangePasswordDto);

        call.enqueue(new Callback<ResponseDto<Void>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull Call<ResponseDto<Void>> call, @NonNull Response<ResponseDto<Void>> response) {
                if(response.code() == 200) {
                   Utils.showToast(mContext, response.body().getMessage());
                    msgChangePasswordLiveData.setValue(response.body().getMessage());
                }
                else {
                    try {
                        String messageError = Utils.getErrorMsg(response.errorBody().string());
                        Utils.logError(TAG, response.code(), messageError);
                        // Utils.showToast(mContext, messageError);
                        msgChangPassword = messageError;
                        msgChangePasswordLiveData.setValue(msgChangPassword);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error: " + e);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseDto<Void>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
                // Utils.showToast(mContext, t.toString());
            }
        });
    }
}
