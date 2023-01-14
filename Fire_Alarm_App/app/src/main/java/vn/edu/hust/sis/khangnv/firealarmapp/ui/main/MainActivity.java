package vn.edu.hust.sis.khangnv.firealarmapp.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.hust.sis.khangnv.firealarmapp.R;
import vn.edu.hust.sis.khangnv.firealarmapp.api.APIClient;
import vn.edu.hust.sis.khangnv.firealarmapp.api.APIInterface;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.UserDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.authDto.LoginResponseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.ResponseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.resources_local.DataLocalManager;
import vn.edu.hust.sis.khangnv.firealarmapp.ui.ChangePasswordFragment;
import vn.edu.hust.sis.khangnv.firealarmapp.ui.ManageHouseFragment;
import vn.edu.hust.sis.khangnv.firealarmapp.ui.ManageNotificationFragment;
import vn.edu.hust.sis.khangnv.firealarmapp.ui.ManageProfileFragment;
import vn.edu.hust.sis.khangnv.firealarmapp.ui.auth.SignInActivity;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;
import vn.edu.hust.sis.khangnv.firealarmapp.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {
    private static final int FRAGMENT_HOUSE = 0;
    private static final int FRAGMENT_NOTIFICATION = 1;
    private static final int FRAGMENT_PROFILE = 2;
    private static final int FRAGMENT_CHANGE_PASSWORD = 3;
    public static final int NOTIFICATION_ID = 10;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ProgressDialog progressDialog;

    private BottomNavigationView mNavBottomView;
    private int currentFragment = FRAGMENT_NOTIFICATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init UI
        initUI();

        // init listener
        initListener();

        // update tokenFCM to server
        saveTokenFCM();
    }

    // init Menu
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        return true;
    }

    // listener in menu item
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_house:
                replaceFragment(new ManageHouseFragment(), FRAGMENT_HOUSE, getString(R.string.Title_ActionBar_House_Fragment));
                return true;
            case R.id.nav_manage_device:
                Utils.showToast(getApplicationContext(), "Device in menu");
                return true;
            case R.id.nav_notification:
                replaceFragment(new ManageNotificationFragment(), FRAGMENT_NOTIFICATION, getString(R.string.Title_ActionBar_Notification_Fragment));
                return true;
            case R.id.nav_my_profile:
                replaceFragment(new ManageProfileFragment(), FRAGMENT_PROFILE, getString(R.string.Title_ActionBar_Profile_Fragment));
                return true;
            case R.id.nav_change_password:
                replaceFragment(new ChangePasswordFragment(), FRAGMENT_CHANGE_PASSWORD, getString(R.string.Title_ActionBar_ChangePass_Fragment));
                return true;
            case R.id.nav_sign_out:
                progressDialog.show();

                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initUI() {
        mNavBottomView = findViewById(R.id.bottomNav);
        mNavBottomView.setSelectedItemId(R.id.action_house);
        progressDialog = Utils.buildProgressDialog(MainActivity.this, "LOG OUT", "Please wait for logout...");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        replaceFragment(new ManageHouseFragment(), FRAGMENT_HOUSE, getString(R.string.Title_ActionBar_House_Fragment));
    }

    @SuppressLint("NonConstantResourceId")
    private void initListener() {
        // listener in nav_bottom
        mNavBottomView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_house:
                    replaceFragment(new ManageHouseFragment(), FRAGMENT_HOUSE, getString(R.string.Title_ActionBar_House_Fragment));
                    break;
                case R.id.action_notification:
                    replaceFragment(new ManageNotificationFragment(), FRAGMENT_NOTIFICATION, getString(R.string.Title_ActionBar_Notification_Fragment));
                    break;
                case R.id.action_profile:
                    replaceFragment(new ManageProfileFragment(), FRAGMENT_PROFILE, getString(R.string.Title_ActionBar_Profile_Fragment));
                    break;
            }
            return true;
        });
    }

    // transmit fragment
    private void replaceFragment(Fragment fragment, int fragment_id, String titleActionBar) {
        if (currentFragment != fragment_id) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentFrame, fragment);
            transaction.commit();

            currentFragment = fragment_id;
            getSupportActionBar().setTitle(titleActionBar);
        }
    }

    // If the FCM token in the app is changed, update it on the server
    private void saveTokenFCM() {
        if(!Objects.equals(DataLocalManager.getFCMTokenServer(), DataLocalManager.getFCMTokenLocal())) {
            UserDto userDto = new UserDto(DataLocalManager.getFCMTokenLocal());
            UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
            userViewModel.saveTokenFCM(userDto);
        }
        Utils.logInfo("FCM TOKEN", DataLocalManager.getFCMTokenLocal());
    }

    // logout
    private void logout() {
        APIInterface apiInterface = APIClient.getRetrofitClient().create(APIInterface.class);
        Call<ResponseDto<Void>> call = apiInterface.logout(DataLocalManager.getAccessTokenServer());
        call.enqueue(new Callback<ResponseDto<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<Void>> call, @NonNull Response<ResponseDto<Void>> response) {
                if(response.code() == 200) {
                    DataLocalManager.setAccessTokenServer("");
                    DataLocalManager.setRefreshTokenServer("");

                    Utils.showToast(getApplicationContext(), response.body().getMessage());
                    Utils.sleep(500);
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                } else {
                    try {
                        Utils.sleep(500);
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
    }
}