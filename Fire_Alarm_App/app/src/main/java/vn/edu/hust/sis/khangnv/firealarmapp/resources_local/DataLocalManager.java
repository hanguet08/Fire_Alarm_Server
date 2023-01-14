package vn.edu.hust.sis.khangnv.firealarmapp.resources_local;

import android.content.Context;

public class DataLocalManager {
    // access token authenticate
    private static final String ACCESS_TOKEN_SERVER = "ACCESS_TOKEN_SERVER";

    // refresh token authenticate
    private static final String REFRESH_TOKEN_SERVER = "REFRESH_TOKEN_SERVER";

    // id of login client
    private static final String CLIENT_ID = "CLIENT_ID";

    // token use for identify device in local app
    private static final String FCM_TOKEN_LOCAL = "FCM_TOKEN_LOCAL";

    // token use for identify device in server
    private static final String FCM_TOKEN_SERVER = "FCM_TOKEN_SERVER";

    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context){
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DataLocalManager getInstance(){
        if(instance == null){
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static void setAccessTokenServer(String accessTokenServer) {
        String tokenBearer = "Bearer " + accessTokenServer;
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(ACCESS_TOKEN_SERVER,tokenBearer);
    }
    public static String getAccessTokenServer(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(ACCESS_TOKEN_SERVER);
    }

    public static void setRefreshTokenServer(String refreshTokenServer) {
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(REFRESH_TOKEN_SERVER,refreshTokenServer);
    }
    public static String getRefreshTokenServer(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(REFRESH_TOKEN_SERVER);
    }

    public static void setClientId(String id){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(CLIENT_ID,id);
    }
    public static String getClientId(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(CLIENT_ID);
    }

    public static void setFCMTokenLocal(String token){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(FCM_TOKEN_LOCAL,token);
    }
    public static String getFCMTokenLocal(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(FCM_TOKEN_LOCAL);
    }

    public static void setFCMTokenServer(String token){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(FCM_TOKEN_SERVER,token);
    }
    public static String getFCMTokenServer(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(FCM_TOKEN_SERVER);
    }
}
