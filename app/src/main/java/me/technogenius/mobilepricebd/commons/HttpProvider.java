package me.technogenius.mobilepricebd.commons;

import android.app.Activity;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sayemkcn on 10/2/16.
 */
public class HttpProvider {
    private Activity context;

    public HttpProvider(Activity context) {
        this.context = context;
    }

    public String fetchData(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();

    }

}
