package com.example.pirang.retrofittesting.util;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Pirang on 12/2/2016.
 */

public class RetrofitUtility {

    // This method  converts String to RequestBody
    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }

}
