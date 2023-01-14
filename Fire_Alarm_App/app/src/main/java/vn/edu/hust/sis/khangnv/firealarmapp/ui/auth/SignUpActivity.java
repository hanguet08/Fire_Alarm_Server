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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.hust.sis.khangnv.firealarmapp.R;
import vn.edu.hust.sis.khangnv.firealarmapp.api.APIClient;
import vn.edu.hust.sis.khangnv.firealarmapp.api.APIInterface;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto.ResponseDto;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.UserDto;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailEdt, password, fullNameEdt, confirmPasswordEdt;
    private Button signUpBtn;
    private TextView notificationTxt, redirectSignIn;
    private static final String LOG_TAG = SignUpActivity.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // init UI
        initUI();

        // init listener
        initListener();
    }

    private void initUI() {
        emailEdt = (EditText) findViewById(R.id.email);
        fullNameEdt = (EditText) findViewById(R.id.fullName);
        password = (EditText) findViewById(R.id.password);
        confirmPasswordEdt = (EditText) findViewById(R.id.repassword);
        redirectSignIn = (TextView) findViewById(R.id.redirectSignIn);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        notificationTxt = (TextView) findViewById(R.id.txtNotificationSignUp);
        progressDialog = Utils.buildProgressDialog(SignUpActivity.this, "SIGN UP", "Loading...");
    }

    private void initListener(){
        signUpBtn.setOnClickListener(view -> {
            notificationTxt.setVisibility(View.GONE);
            String emailValue = Utils.getValueFromEditText(emailEdt);
            String fullNameValue = Utils.getValueFromEditText(fullNameEdt);
            String passwordValue = Utils.getValueFromEditText(password);
            String confirmPasswordValue = Utils.getValueFromEditText(confirmPasswordEdt);

            // validate
            boolean isValid = validate(emailValue, fullNameValue, passwordValue, confirmPasswordValue);

            if(isValid) {
                UserDto userDto = new UserDto(emailValue, passwordValue, fullNameValue);

                progressDialog.show();
                // call API
                signUp(userDto);
            }
        });

        redirectSignIn.setOnClickListener(view -> {
            // ==> SignInActivity
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
            finishAffinity();
        });
    }

    // Validate
    private boolean validate(String emailValue, String fullNameValue, String passwordValue, String confirmPasswordValue) {
        if(emailValue.isEmpty() || passwordValue.isEmpty() || fullNameValue.isEmpty() || confirmPasswordValue.isEmpty() ){
            notificationTxt.setText(R.string.warning_enter_all_fields);
            notificationTxt.setVisibility(View.VISIBLE);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            notificationTxt.setText(R.string.warning_email_invalid);
            notificationTxt.setVisibility(View.VISIBLE);
        } else if (passwordValue.compareTo(confirmPasswordValue) != 0){
            notificationTxt.setText(R.string.warning_confirm_pass_wrong);
            notificationTxt.setVisibility(View.VISIBLE);
        } else if(passwordValue.length() < 6) {
            notificationTxt.setText(R.string.warning_length_pass);
            notificationTxt.setVisibility(View.VISIBLE);
        } else {
            return true;
        }
        return false;
    }

    // call API
    private void signUp(UserDto userDto) {
        APIInterface apiInterface = APIClient.getRetrofitClient().create(APIInterface.class);
        Call<ResponseDto<UserDto>> call = apiInterface.register(userDto);

        call.enqueue(new Callback<ResponseDto<UserDto>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<UserDto>> call, @NonNull Response<ResponseDto<UserDto>> response) {
                if (response.code() == 200) {
                    String message = response.body().getMessage();

                    // show Toast notify
                    Utils.showToast(getApplicationContext(), message);

                    Utils.sleep(500);
                    progressDialog.dismiss();
                    // ==> SignInActivity
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finishAffinity();
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
            public void onFailure(@NonNull Call<ResponseDto<UserDto>> call, @NonNull Throwable t) {
                Log.e(LOG_TAG,"Error" + t);
            }
        });
    }
}