package vn.edu.hust.sis.khangnv.firealarmapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    public static String BASE_URL = "https://fire-alarm12.herokuapp.com/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
