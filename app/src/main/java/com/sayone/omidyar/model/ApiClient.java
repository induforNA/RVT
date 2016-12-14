package com.sayone.omidyar.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sayone on 8/12/16.
 */

public class ApiClient {

    public static final String BASE_URL = "http://52.66.160.79/api/v1/";
    private static Retrofit retrofit = null;
//
//
    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
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
