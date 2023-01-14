package vn.edu.hust.sis.khangnv.firealarmapp.ui.auth.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import vn.edu.hust.sis.khangnv.firealarmapp.R;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.UserDto;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;

public class SignUpActivity_Firebase extends AppCompatActivity {
    private EditText emailEdt, password, fullNameEdt, confirmPasswordEdt;
    private Button signUpBtn;
    private TextView notificationTxt;
    private static final String LOG_TAG = SignUpActivity_Firebase.class.getSimpleName();

    private FirebaseAuth mAuth;
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
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        notificationTxt = (TextView) findViewById(R.id.txtNotificationSignUp);

        // use for firebase auth
        progressDialog = new ProgressDialog(SignUpActivity_Firebase.this);
        mAuth = FirebaseAuth.getInstance();
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

    private void initListener(){
        signUpBtn.setOnClickListener(view -> {
            notificationTxt.setVisibility(View.GONE);
            String emailValue = emailEdt.getText().toString().trim();
            String fullNameValue = fullNameEdt.getText().toString().trim();
            String passwordValue = password.getText().toString().trim();
            String confirmPasswordValue = confirmPasswordEdt.getText().toString().trim();
            // String addressValue = addressEdt.getText().toString().trim();
            // String phoneNumberValue = phoneNumberEdt.getText().toString().trim();
            // String fcmToken = DataLocalManager.getFCMTokenLocal();

            // validate
            boolean isValid = validate(emailValue, fullNameValue, passwordValue, confirmPasswordValue);

            if(isValid) {
                progressDialog.setTitle("REGISTER");
                progressDialog.setMessage("Please wait for register!");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                // call API
                createAccount(emailValue, passwordValue, fullNameValue);

                // hidden dialog
                progressDialog.dismiss();
            }
        });
    }

    private void createAccount(String email, String password, String fullName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Utils.showToast(getBaseContext(), "Create user with Firebase success!");
                        UserDto user = new UserDto(email, password, fullName);

                        FirebaseDatabase.getInstance().getReference("users")
                                .child(mAuth.getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    Intent intent = new Intent(SignUpActivity_Firebase.this, SignInActivity_Firebase.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                });
                    } else {
                        Utils.showToast(getBaseContext(), "Create user with Firebase failure!");
                    }
                });
    }
}