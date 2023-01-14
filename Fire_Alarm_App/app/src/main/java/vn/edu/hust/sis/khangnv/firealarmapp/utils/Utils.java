package vn.edu.hust.sis.khangnv.firealarmapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;

import vn.edu.hust.sis.khangnv.firealarmapp.R;

public class Utils {
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void logMessage(String TAG, String status, String message) {
        Log.d(TAG, "Status : " + status + ", Message: " + message);
    }

    public static void logInfo(String TAG, String message) {
        Log.d(TAG, "Message: " + message);
    }

    public static void logError(String TAG, int statusCode, String error) {
        Log.e(TAG, "Status Code : " + statusCode + ", Error: " + error);
    }

    // use Gson convert string to Object
    public static String getErrorMsg(String errorBody) {
        Gson g = new Gson();
        Error dto = g.fromJson(errorBody, Error.class);
        return dto.getMessage();
    }

    public static ProgressDialog buildProgressDialog(Context context, String title, String message) {
        ProgressDialog progressDialog = new ProgressDialog(new ContextThemeWrapper(context, R.style.DialogCustom));
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static String getValueFromEditText(EditText editText) {
        return editText.getText().toString().trim();
    }
}
