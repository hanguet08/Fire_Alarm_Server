package vn.edu.hust.sis.khangnv.firealarmapp.firebase_cloud_message;


import static vn.edu.hust.sis.khangnv.firealarmapp.ui.main.MainActivity.NOTIFICATION_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import vn.edu.hust.sis.khangnv.firealarmapp.MyApplication;
import vn.edu.hust.sis.khangnv.firealarmapp.R;
import vn.edu.hust.sis.khangnv.firealarmapp.resources_local.DataLocalManager;
import vn.edu.hust.sis.khangnv.firealarmapp.ui.main.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "FCM";
    PendingIntent notificationPendingIntent;
    private String title;
    private String body;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        super.onMessageReceived(remoteMessage);

        // Log.d(TAG, "From: " + remoteMessage.getFrom());
        /*if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            Log.d(TAG, "field 1: " + data.get("username"));
            Log.d(TAG, "field 2: " + data.get("password"));

        }*/
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        }
        sendNotification(title, body);
    }

    private void sendNotification(String title, String body) {
        /*Bitmap androidImage = BitmapFactory
                .decodeResource(getResources(), R.drawable.icons8_warning_48);*/

        @SuppressLint({"InlinedApi", "ResourceAsColor"}) NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.drawable.icons8_fire_16)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icons8_warning_48))
                        .setContentIntent(notificationPendingIntent)
                        // .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(androidImage))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .setAutoCancel(true);


        /* Notification notification = notificationBuilder.build(); */
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(mNotifyManager != null){
            mNotifyManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.i("New FCM token: ", s);
        sendRegistrationToLocal(s);
    }

    // Save New FCM token to Data local
    private void sendRegistrationToLocal(String tokenFCM) {
        DataLocalManager.setFCMTokenLocal(tokenFCM);
    }
}
