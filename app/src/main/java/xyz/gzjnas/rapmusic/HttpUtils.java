package xyz.gzjnas.rapmusic;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils extends AsyncTask<String, Void, String> {
    private static final String TAG = "HttpUtils";

    @Override
    protected String doInBackground(String... urls) {
        String url = urls[0];
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e(TAG, "Error executing HTTP GET request: " + e.getMessage());
            return null;
        }
    }
    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Log.d(TAG, "HTTP GET response: " + result);
        } else {
            Log.e(TAG, "HTTP GET request failed");
        }
    }
}