package com.io.routesapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.io.routesapp.MainActivity;
import com.io.routesapp.R;
import com.io.routesapp.StartActivity;
import com.io.routesapp.data.LoginDataSource;
import com.io.routesapp.data.LoginRepository;
import com.io.routesapp.data.model.LoggedInUser;

public class ProfileFragment extends Fragment {

    private LoggedInUser loggedInUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loggedInUser = MainActivity.getLoggedInUser();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView username = root.findViewById(R.id.username);
        final TextView displayName = root.findViewById(R.id.displayName);
        username.setText(loggedInUser.getUserId());
        displayName.setText(loggedInUser.getDisplayName());
        Button logout_button = root.findViewById(R.id.log_out);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StartActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}