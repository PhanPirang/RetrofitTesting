package com.example.pirang.retrofittesting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.pirang.retrofittesting.service.ArticleService;
import com.google.gson.JsonObject;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String API_BASE_URL = "http://120.136.24.174:1301/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getRetrofitImage();
//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                Request original = chain.request();
//
//                Request.Builder requestBuilder = original.newBuilder()
//                        .header("Authorization", "Basic QU1TQVBJQURNSU46QU1TQVBJUEBTU1dPUkQ=")
//                        .header("Accept", "application/json")
//                        .method(original.method(), original.body());
//
//                Request request = requestBuilder.build();
//                return chain.proceed(request);
//            }
//        });

//        OkHttpClient client = httpClient.build();
//        Retrofit retrofit = builder.client(client).build();
//        ArticleService articleService = retrofit.create(ArticleService.class);
//        Call<JSONObject> call = articleService.getArticles();
//        call.enqueue(new Callback<JSONObject>() {
//            @Override
//            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
//                Log.e("ooooo", response.toString());
//                Log.e("ooooo", call.toString());
//            }
//
//            @Override
//            public void onFailure(Call<JSONObject> call, Throwable t) {
//                Log.e("ooooo", "====>" + call.toString());
//                t.printStackTrace();
//            }
//        });




    }

//    private void showArticles(){
//        ArticleService articleService = RetrofitConfiguration.getRetrofitClient().create(ArticleService.class);
//        Call<JSONObject> call = articleService.getArticles();
//        call.enqueue(new Callback<JSONObject>() {
//            @Override
//            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
//                Log.e("ooooo", response.toString());
//                Log.e("ooooo", call.toString());
//            }
//
//            @Override
//            public void onFailure(Call<JSONObject> call, Throwable t) {
//                Log.e("ooooo", call.toString());
//                t.printStackTrace();
//            }
//        });
//
//    }

    void getRetrofitImage() {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Basic QU1TQVBJQURNSU46QU1TQVBJUEBTU1dPUkQ=")
                        .header("Accept", "application/json")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client)
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ArticleService service = retrofit.create(ArticleService.class);

        Call<MyResponse> call = service.getArticles();

        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                Log.e("ooooo",response.body().get("CODE").getAsString());

            MyResponse myResponse = response.body();
            Log.e("ooooo", myResponse.data.get(0).author.id + "");

                if (myResponse.data.get(0).pagination == null){
                    
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
