package com.io.routesapp.ui.start;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.io.routesapp.MainActivity;
import com.io.routesapp.R;

public class StartFragment extends Fragment {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_start, container, false);
        Button login = root.findViewById(R.id.login_button);
        Button create_account = root.findViewById(R.id.create_account);
        final WebView login_oauth_webview = root.findViewById(R.id.login_oauth_webview);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
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
