package vn.edu.hust.sis.khangnv.firealarmapp.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.hust.sis.khangnv.firealarmapp.ui.main.MainActivity;
import vn.edu.hust.sis.khangnv.firealarmapp.R;
import vn.edu.hust.sis.khangnv.firealarmapp.api.APIClient;
import vn.edu.hust.sis.khangnv.firealarmapp.api.APIInterface;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.ResponseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.authDto.LoginRequestDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.authDto.LoginResponseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.resources_local.DataLocalManager;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;

public class SignInActivity extends AppCompatActivity {
    private EditText emailField, passwordField;
    private Button loginBtn;
    private TextView forgotPassword, signUp, notificationTxt;
    private static final String LOG_TAG = SignInActivity.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // init UI
        initUI();

        // init listener
        initLister();

        // If there is token then redirect to activity
        checkToken();
    }

    private void initUI() {
        emailField = (EditText) findViewById(R.id.emailEdt);
        passwordField = (EditText) findViewById(R.id.passwordEdt);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        forgotPassword = (TextView) findViewById(R.id.forgotpass);
        signUp = (TextView) findViewById(R.id.signUp);
        notificationTxt = (TextView) findViewById(R.id.txtNotification);
        progressDialog = Utils.buildProgressDialog(SignInActivity.this, "SIGN IN", "Please wait to login...");
    }

    private void initLister() {
        // click signIn button
        loginBtn.setOnClickListener(view -> {
            notificationTxt.setVisibility(View.GONE);

            String emailValue = Utils.getValueFromEditText(emailField);
            String passwordValue = Utils.getValueFromEditText(passwordField);

            // validate input
            boolean isValid = validate(emailValue, passwordValue);

            if(isValid) {
                LoginRequestDto loginRequestDto = new LoginRequestDto(emailValue, passwordValue);
                progressDialog.show();
                // call API
                signIn(loginRequestDto);
            }
        });

        // click signUp button
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // click forgotPassword button
        forgotPassword.setOnClickListener(view -> Log.d("SignIn activity", "Forgot password"));
    }

    // validation
    private boolean validate(String emailValue, String passwordValue) {
        if(emailValue.isEmpty() || passwordValue.isEmpty()){
            notificationTxt.setText(R.string.warning_enter_all_fields);
            notificationTxt.setVisibility(View.VISIBLE);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            notificationTxt.setText(R.string.warning_email_invalid);
            notificationTxt.setVisibility(View.VISIBLE);
        } else if(passwordValue.length() < 6) {
            notificationTxt.setText(R.string.warning_length_pass);
            notificationTxt.setVisibility(View.VISIBLE);
        } else {
            return true;
        };
        return false;
    }

    // call API
    private void signIn(LoginRequestDto loginRequestDto) {
        APIInterface apiInterface = APIClient.getRetrofitClient().create(APIInterface.class);
        Call<ResponseDto<LoginResponseDto>> call = apiInterface.login(loginRequestDto);

        call.enqueue(new Callback<ResponseDto<LoginResponseDto>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<LoginResponseDto>> call, @NonNull Response<ResponseDto<LoginResponseDto>> response) {
                if(response.code() == 200) {
                    LoginResponseDto loginResponseDto = response.body().getData();
                    String message = response.body().getMessage();
                    String accessToken = loginResponseDto.getAccessToken();
                    String refreshToken = loginResponseDto.getRefreshToken();
                    String userId = loginResponseDto.getUserId();
                    String fcmToken = loginResponseDto.getFcmToken();

                    DataLocalManager.setFCMTokenServer(fcmToken);
                    DataLocalManager.setAccessTokenServer(accessToken);
                    DataLocalManager.setRefreshTokenServer(refreshToken);
                    DataLocalManager.setClientId(userId);

                    Utils.sleep(800);
                    Utils.showToast(getApplicationContext(), message);

                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();

                    progressDialog.dismiss();
                } else {
                    try {
                        Utils.sleep(500);
                        progressDialog.dismiss();
                        // ErrorMessage get by response.errorBody()
                        String messageError = Utils.getErrorMsg(response.errorBody().string());
                        Utils.logError(LOG_TAG, response.code(), messageError);
                        // notify message
                        notificationTxt.setText(messageError);
                        notificationTxt.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseDto<LoginResponseDto>> call, @NonNull Throwable t) {
                Log.e("Login onFailure","Error" + t);
            }
        });
    }

    private void checkToken() {
        if(!Objects.equals(DataLocalManager.getAccessTokenServer(), "Bearer ")) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }
    }
}