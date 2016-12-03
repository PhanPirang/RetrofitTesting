package com.example.pirang.retrofittesting;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.pirang.retrofittesting.service.API;
import com.example.pirang.retrofittesting.service.ServiceGenerator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String AMS_API_BASE_URL = "http://120.136.24.174:1301/";
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_CODE_PICKER = 0;
    private API.ArticleService service;

    private static final int REQUEST_CODE = 0x11;

    String[] permissions = {"android.permission-group.STORAGE"};

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ArrayList<Image> images = new ArrayList<>();
    private TextView textview;

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview = (TextView) findViewById(R.id.textview);

        Button btn = (Button) findViewById(R.id.button);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyStoragePermissions(MainActivity.this);
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
                }
            });
        }

        findViewById(R.id.multiple_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(MainActivity.this)
                        .returnAfterCapture(false) // set whether camera action should return immediate result or not
                        .folderMode(true) // set folder mode (false by default)
                        .folderTitle("Folder") // folder selection title
                        .imageTitle("Tap to select") // image selection title
                        .single() // single mode
                        .multi() // multi mode (default mode)
                        .limit(10) // max images can be selected (99 by default)
                        .showCamera(true) // show camera or not (true by default)
                        .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                        .origin(images) // original selected images, used in multi mode
                        .start(REQUEST_CODE_PICKER); // start image picker activity with request code
            }
        });

        //getArticles();
        service = ServiceGenerator.createService(API.ArticleService.class);
        //addArticle();
        getArticles();
        //deleteArticle(340);
        //updateArticle();
        //addUser();

    }

    private void printImages(List<Image> images) {
        if (images == null) return;

        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0, l = images.size(); i < l; i++) {
            stringBuffer.append(images.get(i).getPath()).append("\n");
        }
        Log.e("ooooo", "PATH => " + stringBuffer.toString());

    }



    ///// Test upload array of image

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            images = (ArrayList<Image>) ImagePicker.getImages(data);
            //printImages(images);

            List<MultipartBody.Part> menus = new ArrayList<>();
            List<MultipartBody.Part> restaurants = new ArrayList<>();

            for(Image image : images){
                File file = new File(image.getPath());
                RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("MENU", file.getName(), reqFile);
                menus.add(body);
            }

            for(Image image : images){
                File file = new File(image.getPath());
                RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("RESTAURANT", file.getName(), reqFile);
                restaurants.add(body);
            }

            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "Khmer");
            API.RestaurantService restaurantService = ServiceGenerator.createService(API.RestaurantService.class);
            Call<JsonObject> call = restaurantService.addRestaurants(name, restaurants, menus);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e("ooooo", "POST Restaurant => " + response.body().toString() + "");
                    textview.setText(response.body().toString());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    t.printStackTrace();
                }
            });


        }

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            File file = new File(filePath);

            RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("PHOTO", file.getName(), reqFile);
//            Log.d("THIS", data.getData().getPath());

            // add another part within the multipart request
            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), "a");
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "c");
            RequestBody pwd = RequestBody.create(MediaType.parse("text/plain"), "dsfsdfsd");
            RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), "f");
            RequestBody tel = RequestBody.create(MediaType.parse("text/plain"), "123123123");
            RequestBody fb = RequestBody.create(MediaType.parse("text/plain"), "12312312321");

            API.UserService userService = ServiceGenerator.createService(API.UserService.class);
            Call<JsonElement> call = userService.addUser(email, name, pwd, gender, tel, fb, body);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                    Log.e("ooooo", "POST => " + response.body().toString());
                    if (response.isSuccessful()){
                        Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "ALLOWED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }













    void getArticles() {
        Call<MyResponse> call = service.getArticles();
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                MyResponse myResponse = response.body();
                Log.e("ooooo", "GET Article=> " + myResponse.data.get(0).id + "");
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void addArticle() {
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

    void deleteArticle(Integer id) {
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

    void updateArticle() {
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

    void addUser() {
        API.UserService userService = ServiceGenerator.createService(API.UserService.class);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        File file = BitmapEfficient.persistImage(bmp, this);

        if (file == null) {
            Log.e("ooooo", "NULL");
        }

        // create RequestBody instance from file
        //RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("PHOTO", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), "a");
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "c");
        RequestBody pwd = RequestBody.create(MediaType.parse("text/plain"), "dsfsdfsd");
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), "f");
        RequestBody tel = RequestBody.create(MediaType.parse("text/plain"), "123123123");
        RequestBody fb = RequestBody.create(MediaType.parse("text/plain"), "12312312321");

        Call<JsonElement> call = userService.addUser(email, name, pwd, gender, tel, fb, body);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("ooooo", "POST => " + response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
