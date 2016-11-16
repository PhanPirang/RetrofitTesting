package com.example.pirang.retrofittesting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.pirang.retrofittesting.service.API;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String API_BASE_URL = "http://120.136.24.174:1301/";
    private API.ArticleService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getArticles();
        service = ServiceGenerator.createService(API.ArticleService.class);
        //addArticle();
        getArticles();
        //deleteArticle(340);
        //updateArticle();
        addUser();

    }

    void getArticles() {
        Call<MyResponse> call = service.getArticles();
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                MyResponse myResponse = response.body();
                Log.e("ooooo", "GET => " + myResponse.data.get(0).id + "");
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void addArticle(){
        MyResponse.Data article = new MyResponse.Data();
        article.title = "Test1";
        Call<JsonObject> call = service.addArticle(article);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("ooooo", "POST => " + response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void deleteArticle(Integer id){
        Call<JsonObject> call = service.deleteArticle(id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("ooooo", "DELETE => " + response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void updateArticle(){
        MyResponse.Data article = new MyResponse.Data();
        article.title = "Test Updated";
        Call<JsonObject> call = service.updateArticle(article, 499);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("ooooo", "PUT => " + response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void addUser(){
        API.UserService userService = ServiceGenerator.createService(API.UserService.class);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        File file = BitmapEfficient.persistImage(bmp, this);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), "a");
        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), "c");
        RequestBody pwd = RequestBody.create(MediaType.parse("multipart/form-data"), "dsfsdfsd");
        RequestBody gender = RequestBody.create(MediaType.parse("multipart/form-data"), "f");
        RequestBody tel = RequestBody.create(MediaType.parse("multipart/form-data"), "123123123");
        RequestBody fb = RequestBody.create(MediaType.parse("multipart/form-data"), "12312312321");
        Call<JsonObject> call = userService.addUser(email, name, pwd, gender, tel, fb, body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("ooooo", "DELETE => " + response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
