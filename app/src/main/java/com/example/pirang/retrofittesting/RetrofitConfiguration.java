package com.example.pirang.retrofittesting;

import android.app.Application;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Pirang on 11/19/2016.
 */

public class RetrofitConfiguration {

//    public static final String BASE_URL = "http://120.136.24.174:15050";
//    private static Retrofit retrofit = null;
//    private static OkHttpClient okHttpClient = null;
//
//    public static Retrofit getRetrofitClient() {
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(getOkHttpClient())
//                    .build();
//        }
//        return retrofit;
//    }

//    private static OkHttpClient getOkHttpClient(){
//        if (okHttpClient == null) {
//            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//            httpClient.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Interceptor.Chain chain) {
//                    Request original = chain.request();
//
//                    // Request customization: add request headers
//                    Request.Builder requestBuilder = original.newBuilder()
//                            .header("Authorization", "Basic QU1TQVBJQURNSU46QU1TQVBJUEBTU1dPUkQ=")
//                            .header("Accept", "application/json")
//                            .method(original.method(), original.body());
//
//                    Request request = requestBuilder.build();
//                    try {
//                        return chain.proceed(request);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            });
//            return httpClient.build();
//        }
//        return null;
//    }

    public static final String API_BASE_URL = "http://120.136.24.174:15050";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public static <S> S createService(Class<S> serviceClass, String username, String password) {
        if (username != null && password != null) {
            String credentials = username + ":" + password;

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Basic QU1TQVBJQURNSU46QU1TQVBJUEBTU1dPUkQ=")
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }


}
