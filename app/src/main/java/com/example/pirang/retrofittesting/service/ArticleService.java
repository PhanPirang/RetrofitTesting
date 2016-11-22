package com.example.pirang.retrofittesting.service;

import com.example.pirang.retrofittesting.MyResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by Pirang on 11/19/2016.
 */

public interface ArticleService {

    @GET("/v1/api/articles")
    Call<MyResponse> getArticles();

}
