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
import vn.edu.hust.sis.khangnv.firealarmapp.dto.HouseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.ResponseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.resources_local.DataLocalManager;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;

public class HouseViewModel extends ViewModel {
    private MutableLiveData<List<HouseDto>> houseListLiveData;
    private List<HouseDto> mListHouses;
    private final String TAG = "Call Api House";
    APIInterface apiService;

    // constructor
    public HouseViewModel() {
        this.houseListLiveData = new MutableLiveData<>();
        this.mListHouses = new ArrayList<HouseDto>();
        apiService = APIClient.getRetrofitClient().create(APIInterface.class);
    }

    // getter
    public MutableLiveData<List<HouseDto>> getHouseListLiveData() {
        return houseListLiveData;
    }

    // call API : get all house
    public void getHouseList() {
        Call<ResponseDto<List<HouseDto>>> call = apiService.getListHouses(DataLocalManager.getAccessTokenServer());

        call.enqueue(new Callback<ResponseDto<List<HouseDto>>>() {
            @Override
            public void onResponse(Call<ResponseDto<List<HouseDto>>> call, @NonNull Response<ResponseDto<List<HouseDto>>> response) {
                if(response.code() == 200) {
                    mListHouses = new ArrayList<HouseDto>(response.body().getData());
                    houseListLiveData.setValue(mListHouses);
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
            public void onFailure(Call<ResponseDto<List<HouseDto>>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }

    // call API : insert one house
    public void insertHouse(HouseDto houseDto, Context mContext) {
        Call<ResponseDto<HouseDto>> call = apiService.insertHouse(DataLocalManager.getAccessTokenServer(), houseDto);
        call.enqueue(new Callback<ResponseDto<HouseDto>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<HouseDto>> call, @NonNull Response<ResponseDto<HouseDto>> response) {
                if(response.code() == 200) {
                    getHouseList();
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
            public void onFailure(Call<ResponseDto<HouseDto>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }

    // call API : update one house
    public void updateHouse(HouseDto houseDto, String houseId, Context mContext) {
        Call<ResponseDto<HouseDto>> call = apiService.updateHouse(DataLocalManager.getAccessTokenServer(), houseDto, houseId);
        call.enqueue(new Callback<ResponseDto<HouseDto>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<HouseDto>> call, @NonNull Response<ResponseDto<HouseDto>> response) {
                if(response.code() == 200) {
                    getHouseList();
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
            public void onFailure(@NonNull Call<ResponseDto<HouseDto>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }

    // call API : delete one house
    public void deleteHouse(String houseId, Context mContext) {
        Call<ResponseDto<Void>> call = apiService.deleteHouse(DataLocalManager.getAccessTokenServer(), houseId);
        call.enqueue(new Callback<ResponseDto<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<Void>> call, @NonNull Response<ResponseDto<Void>> response) {
                if(response.code() == 200) {
                    getHouseList();
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
            public void onFailure(@NonNull Call<ResponseDto<Void>> call, Throwable t) {
                Utils.logError(TAG, 400, "Call api failed " + t);
            }
        });
    }
}
