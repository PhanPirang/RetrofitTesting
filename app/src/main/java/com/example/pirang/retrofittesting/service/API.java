package com.example.pirang.retrofittesting.service;

import com.example.pirang.retrofittesting.MyResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by Pirang on 11/16/2016.
 */

public class API {

    private static final String RESTAURANT_API_KEY = "Authorization: Basic cmVzdGF1cmFudEFETUlOOnJlc3RhdXJhbnRQQFNTV09SRA==";

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

    public interface RestaurantService{

        @Headers(RESTAURANT_API_KEY)
        @GET("http://192.168.178.107:8080/RESTAURANT_API/v1/api/admin/restaurants?user=42&page=1&limit=15")
        Call<JsonObject> getRestaurants();

        @Multipart
        @Headers(RESTAURANT_API_KEY)
        @POST("http://192.168.178.107:8080/RESTAURANT_API/v1/api/admin/restaurants/multiple/register/test")
        Call<JsonObject> addRestaurants(
                @Part("NAME") RequestBody name,
                @Part List<MultipartBody.Part> restaurants,
                @Part List<MultipartBody.Part> menus
        );

    }

}
