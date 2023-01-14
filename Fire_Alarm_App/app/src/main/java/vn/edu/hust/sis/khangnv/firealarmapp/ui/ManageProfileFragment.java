package vn.edu.hust.sis.khangnv.firealarmapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.hust.sis.khangnv.firealarmapp.R;
import vn.edu.hust.sis.khangnv.firealarmapp.api.APIClient;
import vn.edu.hust.sis.khangnv.firealarmapp.api.APIInterface;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.NotificationDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.UserDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.ResponseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.resources_local.DataLocalManager;
import vn.edu.hust.sis.khangnv.firealarmapp.ui.auth.SignInActivity;
import vn.edu.hust.sis.khangnv.firealarmapp.ui.main.MainActivity;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.StatusSeenNotification;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;
import vn.edu.hust.sis.khangnv.firealarmapp.viewmodel.UserViewModel;

public class ManageProfileFragment extends Fragment {
    private Context mContext;
    private UserDto mUserDto;
    private UserViewModel userViewModel;
    private TextView txtEmail, txtFullName, txtAge, txtAddress, txtPhone, logout, txtNoUserProfile;
    private LinearLayout coverProfile;
    private static final String LOG_TAG = ManageProfileFragment.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public ManageProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_profile, container, false);

        // init UI
        initUI(view);

        // init listener
        initListener();

        // call API get data
        bindData();

        Utils.sleep(200);
        return view;
    }

    private void initUI(View view) {
        txtEmail = view.findViewById(R.id.txtEmail);
        txtFullName = view.findViewById(R.id.txtFullName);
        txtAge = view.findViewById(R.id.txtAge);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtPhone = view.findViewById(R.id.txtPhone);
        logout = view.findViewById(R.id.logout);
        coverProfile = view.findViewById(R.id.coverProfile);
        txtNoUserProfile = view.findViewById(R.id.tvNoUserProfile);
        progressDialog = Utils.buildProgressDialog(mContext, "LOG OUT", "Please wait for logout...");
    }

    private void initListener() {
        // click Logout button
        logout.setOnClickListener(view -> {
            logout();
        });
    }

    // call API and bind data to view
    private void bindData() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUser();
        userViewModel.getUserDtoLiveData().observe(getViewLifecycleOwner(), userDto -> {
            if(userDto == null) {
                txtNoUserProfile.setVisibility(View.VISIBLE);
                coverProfile.setVisibility(View.GONE);
            } else {
                txtEmail.setText(userDto.getEmail());
                txtFullName.setText(userDto.getFullName());
                txtAddress.setText(userDto.getAddress());
                txtPhone.setText(userDto.getPhoneNumber());
                // txtAge.setText(String.valueOf(userDto.getAge()));
            }
        });
    }

    // logout
    private void logout() {
        ManageProfileFragment.AsyncTaskProfile asyncTaskProfile = new ManageProfileFragment.AsyncTaskProfile(mContext);
        asyncTaskProfile.execute();
    }

    // Call API in background use AsyncTask
    private class AsyncTaskProfile extends AsyncTask<Void, String, Void> {
        private final ProgressDialog dialog;

        private AsyncTaskProfile(Context context) {
            this.dialog = Utils.buildProgressDialog(context, "LOGOUT", "Please wait for logout...");
        }

        @Override
        protected void onPostExecute(Void string) {
            // execution of result of Long time consuming operation
            if (dialog.isShowing()) {
                Utils.sleep(500);
                dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            APIInterface apiInterface = APIClient.getRetrofitClient().create(APIInterface.class);
            Call<ResponseDto<Void>> call = apiInterface.logout(DataLocalManager.getAccessTokenServer());
            call.enqueue(new Callback<ResponseDto<Void>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDto<Void>> call, @NonNull Response<ResponseDto<Void>> response) {
                    if(response.code() == 200) {
                        DataLocalManager.setAccessTokenServer("");
                        DataLocalManager.setRefreshTokenServer("");


                        Utils.showToast(mContext, response.body().getMessage());
                        Utils.sleep(300);
                        Intent intent = new Intent(mContext, SignInActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                        progressDialog.dismiss();
                    } else {
                        try {
                            Utils.sleep(800);
                            progressDialog.dismiss();
                            String messageError = Utils.getErrorMsg(response.errorBody().string());
                            Utils.logError(LOG_TAG, response.code(), messageError);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseDto<Void>> call, Throwable t) {
                    Log.e("Logout onFailure","Error" + t);
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }
    }
}