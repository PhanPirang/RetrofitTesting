package com.example.pirang.retrofittesting.service;

import com.example.pirang.retrofittesting.MyResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Pirang on 11/16/2016.
 */

public class API {

    public interface ArticleService {
        @GET("/v1/api/articles")
        Call<MyResponse> getArticles();

        @POST("/v1/api/articles")
        Call<JsonObject> addArticle(@Body MyResponse.Data article);

        @DELETE("/v1/api/articles/{id}")
        Call<JsonObject> deleteArticle(@Path("id") Integer id);

        @PUT("/v1/api/articles/{id}")
        Call<JsonObject> updateArticle(@Body MyResponse.Data newArticle, @Path("id") Integer id);
    }

    public interface UserService{

        @Multipart
        @POST("/v1/api/users")
        Call<JsonElement> addUser(
                @Part("EMAIL") RequestBody email,
                @Part("NAME") RequestBody name,
                @Part("PASSWORD") RequestBody pwd,
                @Part("GENDER") RequestBody gender,
                @Part("TELEPHONE") RequestBody tel,
                @Part("FACEBOOK_ID") RequestBody id,
                @Part MultipartBody.Part file
        );
    }

}
