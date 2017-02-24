package com.sayone.omidyar.model;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sayone on 8/12/16.
 */

public class ApiClient {

    public static final String BASE_URL = "http://52.66.160.79/api/v1/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }


//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("http://52.66.160.79/api/v1/")
//            .build();
//
//    ApiInterface service = retrofit.create(ApiInterface.class);
}
