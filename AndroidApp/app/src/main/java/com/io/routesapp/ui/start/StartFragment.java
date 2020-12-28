package com.io.routesapp.ui.start;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.io.routesapp.MainActivity;
import com.io.routesapp.R;
import com.io.routesapp.StartActivity;

import java.util.Objects;

public class StartFragment extends Fragment {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_start_page, container, false);
        Button login = root.findViewById(R.id.login_button);
        Button login_oauth = root.findViewById(R.id.login_oauth_button);
        Button create_account = root.findViewById(R.id.create_account);
        final WebView login_oauth_webview = root.findViewById(R.id.login_oauth_webview);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        login_oauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_oauth_webview.setVisibility(View.VISIBLE);
                login_oauth_webview.loadUrl("https://google.com");
            }
        });
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        (((AppCompatActivity) getActivity()).getSupportActionBar()).hide();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
