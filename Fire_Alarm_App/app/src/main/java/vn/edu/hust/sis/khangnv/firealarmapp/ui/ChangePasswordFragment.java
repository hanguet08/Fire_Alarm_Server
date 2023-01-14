package vn.edu.hust.sis.khangnv.firealarmapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import vn.edu.hust.sis.khangnv.firealarmapp.R;
import vn.edu.hust.sis.khangnv.firealarmapp.dto.UserChangePasswordDto;
import vn.edu.hust.sis.khangnv.firealarmapp.utils.Utils;
import vn.edu.hust.sis.khangnv.firealarmapp.viewmodel.UserViewModel;

public class ChangePasswordFragment extends Fragment {
    private Context mContext;
    private Button btnChangePassword;
    private UserViewModel userViewModel;
    private EditText edtOldPassword, edtNewPassword, edtConfirmNewPassword;
    private TextView txtNotificationChangePass;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        // init UI
        initUI(view);

        // init listener
        initListener();

        return view;
    }

    private void initUI(View view) {
        edtOldPassword = view.findViewById(R.id.edtOldPassword);
        edtNewPassword = view.findViewById(R.id.edtNewPassword);
        edtConfirmNewPassword = view.findViewById(R.id.edtConfirmNewPassword);
        txtNotificationChangePass = view.findViewById(R.id.txtNotificationChangePass);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
    }

    private void initListener() {
        // click Confirm change password button
        btnChangePassword.setOnClickListener(view -> {
            txtNotificationChangePass.setVisibility(View.GONE);

            String oldPassword = Utils.getValueFromEditText(edtOldPassword);
            String newPassword = Utils.getValueFromEditText(edtNewPassword);
            String confirmNewPassword = Utils.getValueFromEditText(edtConfirmNewPassword);

            // validate
            boolean isValid = validate(oldPassword, newPassword, confirmNewPassword);

            if(isValid) {
                // call API
                UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(oldPassword, newPassword);
                userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
                userViewModel.changePassword(userChangePasswordDto, mContext);
                userViewModel.getMsgChangePasswordLiveData().observe(getViewLifecycleOwner(), msgChangePassword -> {
                    if(msgChangePassword.equals("Old password is wrong")) {
                        txtNotificationChangePass.setTextColor(Color.parseColor("#FF0000"));
                        txtNotificationChangePass.setText(R.string.msg_change_password_fail);
                        txtNotificationChangePass.setVisibility(View.VISIBLE);
                    } else if(msgChangePassword.equals("Change password successfully!")) {
                        txtNotificationChangePass.setText(R.string.msg_change_password_success);
                        txtNotificationChangePass.setTextColor(Color.parseColor("#0000FF"));
                        txtNotificationChangePass.setVisibility(View.VISIBLE);
                    } else {
                        txtNotificationChangePass.setText("Server error!");
                        txtNotificationChangePass.setTextColor(Color.parseColor("#FF0000"));
                        txtNotificationChangePass.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    // Validate
    private boolean validate(String oldPassword, String newPassword, String confirmNewPassword) {
        if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()){
            txtNotificationChangePass.setText(R.string.warning_enter_all_fields);
            txtNotificationChangePass.setVisibility(View.VISIBLE);
        } else if (newPassword.compareTo(confirmNewPassword) != 0){
            txtNotificationChangePass.setText(R.string.warning_confirm_pass_wrong);
            txtNotificationChangePass.setVisibility(View.VISIBLE);
        } else if(newPassword.length() < 6) {
            txtNotificationChangePass.setText(R.string.warning_length_pass);
            txtNotificationChangePass.setVisibility(View.VISIBLE);
        } else {
            return true;
        }
        return false;
    }
}