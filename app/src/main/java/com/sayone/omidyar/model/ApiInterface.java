package com.sayone.omidyar.model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sayone on 8/12/16.
 */

public interface ApiInterface {

    @Headers({
            "Authorization: Token 2fb88b01c22ac470cbb969f604e9b3c87d6c8c7d",
            "Content-Type: application/json"
    })
    @POST("generate-mini-excel/")
    Call<ExportData> getExported(@Body DataWithId dataWithId);
}
