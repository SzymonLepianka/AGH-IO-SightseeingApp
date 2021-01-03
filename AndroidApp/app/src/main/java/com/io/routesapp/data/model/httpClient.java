package com.io.routesapp.data.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.io.routesapp.MainActivity;
import com.io.routesapp.ui.places.model.Place;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

public class httpClient {
    private static JSONObject placesListJSON;
    private Context context;
    String placesList;

    public httpClient(Context context) {
        this.context = context;
    }

    public void loadPlaces() throws IOException, JSONException {
        String url = "http://10.0.2.2:8080/places";

        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        //okhttp3.Response response = client.newCall(request).execute();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("OKHTTP3", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()){
                    Log.d("OKHTTP3", String.valueOf(response.code()));
                    return;
                }
                try {
                    placesListJSON = new JSONObject(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //placesList = response.body();
    }

    public JSONObject getPlacesJSON() throws IOException, JSONException, InterruptedException {
        String url = "http://10.0.2.2:8080/places";

        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("OKHTTP3", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()){
                    Log.d("OKHTTP3", String.valueOf(response.code()));
                    return;
                }
                try {
                    placesListJSON = new JSONObject(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread.sleep(500);
        return placesListJSON;
    }

}
