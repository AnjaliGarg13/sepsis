package com.nsut.spotsepsis.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nsut.spotsepsis.retrofit.ApiInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenerateRetrofitInstance {

    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient = new OkHttpClient();

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static ApiInterface getApiInterface(){
        if(retrofit == null){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                    .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                    .readTimeout(5, TimeUnit.MINUTES); // read timeout
            okHttpClient = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.43.179:5000")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }


}
