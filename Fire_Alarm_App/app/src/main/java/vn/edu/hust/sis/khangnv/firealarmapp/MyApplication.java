package vn.edu.hust.sis.khangnv.firealarmapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import vn.edu.hust.sis.khangnv.firealarmapp.resources_local.DataLocalManager;

// subscribe in AndroidManifest.xml (application-wide scope)
public class MyApplication extends Application {
    public static final String CHANNEL_ID = "CHANNEL_ID";
    @Override
    public void onCreate() {
        super.onCreate();
        // init data local manager
        DataLocalManager.init(getApplicationContext());

        createChannelNotification();
    }

    // use for notification
    private void createChannelNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "PushNotification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
