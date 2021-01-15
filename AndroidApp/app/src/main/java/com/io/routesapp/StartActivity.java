package com.io.routesapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.io.routesapp.data.httpClient;
import com.io.routesapp.ui.login.LoginActivity;

import java.util.HashMap;

public class StartActivity extends AppCompatActivity {
    HashMap<String, String> cookies;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button login_button = findViewById(R.id.login_button);
        Button sign_up_button = findViewById(R.id.create_account);

        httpClient HTTPClient = new httpClient(this.getApplicationContext(), "StartActivity");

        @SuppressLint("WorldReadableFiles") SharedPreferences mySharedPreferences = getSharedPreferences(getString(R.string.settings), Context.MODE_PRIVATE);
        cookies = new HashMap<>();
        if(mySharedPreferences.contains("AccessToken")) {
            cookies.put("AccessToken", mySharedPreferences.getString("AccessToken", ""));
            try {
                if (HTTPClient.isTokenValid(cookies.get("AccessToken"))) {
                    if (mySharedPreferences.contains("RefreshToken")) {
                        cookies.put("RefreshToken", mySharedPreferences.getString("RefreshToken", ""));
                    }
                    String username = null;
                    if (mySharedPreferences.contains("RefreshToken")) {
                        username = mySharedPreferences.getString("username", "");
                    }

                    try {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("PreviousActivity", "start");
                        intent.putExtra("username", username);
                        intent.putExtra("AccessToken", cookies.get("AccessToken"));
                        intent.putExtra("RefreshToken", cookies.get("RefreshToken"));
                        startActivity(intent);
                    } catch (Exception e) {
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
           }
        } );
    }

}
