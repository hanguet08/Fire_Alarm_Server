package vn.edu.hust.sis.khangnv.firealarmapp.viewmodel;

import android.annotation.SuppressLint;
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
import vn.edu.hust.sis.khangnv.firealarmapp.dto.NotificationDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.ResponseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.resources_local.DataLocalManager;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;

public class NotificationViewModel extends ViewModel {
    private MutableLiveData<List<NotificationDto>> notificationListLiveData;
    private List<NotificationDto> mListNotification;
    private final String TAG = "CallApiGetNotificationList";
    APIInterface apiService;

    // constructor
    public NotificationViewModel() {
        this.notificationListLiveData = new MutableLiveData<>();
        this.mListNotification = new ArrayList<NotificationDto>();
        apiService = APIClient.getRetrofitClient().create(APIInterface.class);
    }

    // getter
    public MutableLiveData<List<NotificationDto>> getNotificationListLiveData() {
        return this.notificationListLiveData;
    }

    // call API : get all notification
    public void getNotificationList() {
        Call<ResponseDto<List<NotificationDto>>> call = apiService.getListNotifications(DataLocalManager.getAccessTokenServer());

        call.enqueue(new Callback<ResponseDto<List<NotificationDto>>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull Call<ResponseDto<List<NotificationDto>>> call, @NonNull Response<ResponseDto<List<NotificationDto>>> response) {
                if(response.code() == 200) {
                    mListNotification = new ArrayList<NotificationDto>(response.body().getData());
                    notificationListLiveData.setValue(mListNotification);
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
            public void onFailure(@NonNull Call<ResponseDto<List<NotificationDto>>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }

    // call API : update one notification
    public void updateNotification(NotificationDto notificationDto, String notificationId, Context mContext) {
        Call<ResponseDto<NotificationDto>> call = apiService.updateNotification(DataLocalManager.getAccessTokenServer(), notificationDto, notificationId);

        call.enqueue(new Callback<ResponseDto<NotificationDto>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull Call<ResponseDto<NotificationDto>> call, @NonNull Response<ResponseDto<NotificationDto>> response) {
                if(response.code() == 200) {
                    getNotificationList();
                    Utils.showToast(mContext, "Seen");
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
            public void onFailure(@NonNull Call<ResponseDto<NotificationDto>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }

}
