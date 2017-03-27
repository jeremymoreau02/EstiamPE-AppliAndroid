package com.jeremy.estiam.appliandroid.api;


import com.jeremy.estiam.appliandroid.models.Adresse;
import com.jeremy.estiam.appliandroid.models.Message;
import com.jeremy.estiam.appliandroid.models.ResponsePerso;
import com.jeremy.estiam.appliandroid.models.Shipping;
import com.jeremy.estiam.appliandroid.models.User;
import com.jeremy.estiam.appliandroid.models.UserConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    String ENDPOINT = "http://193.70.40.193:3000/";

    @POST("api/connection")
    Call<User> connection(@Body UserConnection user);

    @PUT("api/inscription")
    Call<User> inscription(@Body User user);

    @POST("api/users/{id}")
    Call<String> updateUser(@Path("id") int id, @Header("x-access-token") String token, @Body User user);

    @GET("api/users/{id}")
    Call<User> getUser(@Path("id") int id, @Header("x-access-token") String token);

    @PUT("api/contact")
    Call<String> sendMessage(@Header("x-access-token") String token, @Body Message message);

    @GET("api/address/{id}")
    Call<List<Adresse>> getAdresses(@Path("id") int id, @Header("x-access-token") String token);

    @POST("api/address/{id}")
    Call<ResponsePerso> updateAdresse(@Path("id") int id, @Header("x-access-token") String token, @Body Adresse adresse);

    @PUT("api/address")
    Call<ResponsePerso> createAdresse(@Header("x-access-token") String token, @Body Adresse adresse);

    @GET("api/shipping")
    Call<List<Shipping>> getMethodes(@Header("x-access-token") String token);

}