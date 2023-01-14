package vn.edu.hust.sis.khangnv.firealarmapp.ui.auth.firebase;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.edu.hust.sis.khangnv.firealarmapp.R;
import vn.edu.hust.sis.khangnv.firealarmapp.resources_local.DataLocalManager;
import vn.edu.hust.sis.khangnv.firealarmapp.ui.main.MainActivity;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;

public class SignInActivity_Firebase extends AppCompatActivity {
    private EditText emailField, passwordField;
    private Button loginBtn;
    private TextView forgotPassword, signUp, notificationTxt;
    private static final String LOG_TAG = SignInActivity_Firebase.class.getSimpleName();
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // init UI
        initUI();

        // init listener
        initLister();
    }

    private void initUI() {
        emailField = (EditText) findViewById(R.id.emailEdt);
        passwordField = (EditText) findViewById(R.id.passwordEdt);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        forgotPassword = (TextView) findViewById(R.id.forgotpass);
        signUp = (TextView) findViewById(R.id.signUp);
        notificationTxt = (TextView) findViewById(R.id.txtNotification);
        progressDialog = new ProgressDialog(SignInActivity_Firebase.this);
        mAuth = FirebaseAuth.getInstance();
    }

    // validation
    private boolean validate(String emailValue, String passwordValue) {
        if(emailValue.isEmpty() || passwordValue.isEmpty()){
            notificationTxt.setText(R.string.warning_enter_all_fields);
            notificationTxt.setVisibility(View.VISIBLE);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            notificationTxt.setText(R.string.warning_email_invalid);
            notificationTxt.setVisibility(View.VISIBLE);
        } else {
            return true;
        };
        return false;
    }

    private void initLister() {
        loginBtn.setOnClickListener(view -> {
            notificationTxt.setVisibility(View.GONE);
            String emailValue = emailField.getText().toString().trim();
            String passwordValue = passwordField.getText().toString().trim();

            // validate input
            boolean isValid = validate(emailValue, passwordValue);

            if(isValid) {
                progressDialog.setTitle("SIGN IN");
                progressDialog.setMessage("Please wait for sign in!");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                // call API
                signInWithEmailAndPassword(emailValue, passwordValue);
            }
        });

        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity_Firebase.this, SignUpActivity_Firebase.class);
            startActivity(intent);
            finishAffinity();
        });
        forgotPassword.setOnClickListener(view -> Log.d("SignIn activity", "Forgot password"));
    }

    private void signInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // signIn success
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        if(user != null) {
                            String uid = user.getUid();
                            DataLocalManager.setClientId(uid);

                            // set token
                            user.getIdToken(true).addOnSuccessListener(result -> {
                                String idToken = result.getToken();
                                DataLocalManager.setAccessTokenServer(idToken);
                            });
                        }

                        Intent intent = new Intent(SignInActivity_Firebase.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        Utils.sleep(1000);
                        progressDialog.dismiss();
                    } else {
                        Utils.showToast(getApplicationContext(), "Authentication failed");
                        Utils.sleep(1000);
                        progressDialog.dismiss();
                    }
                });
    }
}