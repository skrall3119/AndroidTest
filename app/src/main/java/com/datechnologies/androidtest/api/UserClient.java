package com.datechnologies.androidtest.api;

import android.service.autofill.UserData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserClient {

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> checkLoginInfo(@Field(value="email", encoded=true)String username, @Field(value ="password", encoded = true) String password);
}
