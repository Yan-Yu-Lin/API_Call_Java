package com.example.api_call;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_search).setOnClickListener(v -> {
            // Define URL
            String URL = "https://tools-api.italkutalk.com/java/lab12";

            // Build request
            Request request = new Request.Builder().url(URL).build();

            // Create UnsafeOkHttpClient instance instead of regular OkHttpClient
            OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

            // Execute the request
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();

                        // Parse JSON using GSON
                        Gson gson = new Gson();
                        Data data = gson.fromJson(responseData, Data.class);

                        // Get first result from the array
                        String station = data.result.results[0].Station;
                        String destination = data.result.results[0].Destination;

                        // Show dialog on UI thread
                        runOnUiThread(() -> {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("台北捷運列車到站站名")
                                    .setMessage("列車目前位置：" + station + "\n列車行駛目的地：" + destination)
                                    .setPositiveButton("確定", null)
                                    .show();
                        });
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    // Handle failure
                    Log.e("API_CALL", "API call failed", e);
                    runOnUiThread(() -> {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("錯誤")
                                .setMessage("連線失敗")
                                .setPositiveButton("確定", null)
                                .show();
                    });
                }
            });
        });
    }
}
