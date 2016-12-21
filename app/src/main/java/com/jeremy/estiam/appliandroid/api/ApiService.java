package com.jeremy.estiam.appliandroid.api;


import com.jeremy.estiam.appliandroid.models.User;
import com.jeremy.estiam.appliandroid.models.UserConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    String ENDPOINT = "http://193.70.40.193:3000/";

    @POST("api/connection")
    Call<User> connection(@Body UserConnection user);

    @POST("api/inscription")
    Call<User> inscription(@Body User user);

    @PUT("api/user/{id}")
    Call<String> updateUser((@Path("id") Long id, @Body User user);

    @GET("api/user/{id}")
    Call<String> updateUser(@Path("id") Long id);

    @GET("api/testGet")
    Call<String> test(@Query("valeur") String string);

    @GET("api/testGet")
    Call<String> test();
}
