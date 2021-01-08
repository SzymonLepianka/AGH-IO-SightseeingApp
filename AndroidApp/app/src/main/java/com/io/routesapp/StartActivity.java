package com.io.routesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewClientCompat;

import com.io.routesapp.ui.login.LoginActivity;

public class StartActivity extends AppCompatActivity {
    WebView webview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_start_page);
        Button login_button = findViewById(R.id.login_button);
        Button login_oauth_button = findViewById(R.id.login_oauth_button);
        Button sign_up_button = findViewById(R.id.create_account);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        login_oauth_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview = findViewById(R.id.login_oauth_webview);
                webview.loadUrl("http://10.0.2.2:8081/web/login?clientID=2");
            }
        });
    }
}
