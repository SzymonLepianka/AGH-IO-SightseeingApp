package com.io.routesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.io.routesapp.data.model.LoggedInUser;
import com.io.routesapp.data.httpClient;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private static LoggedInUser loggedInUser;
    public static httpClient HTTPClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        loggedInUser = new LoggedInUser(intent.getStringExtra("username"),
                intent.getStringExtra("displayName"),
                intent.getStringExtra("email"), new HashMap<String, String>());

        loggedInUser.setCookie("AccessToken", intent.getStringExtra("AccessToken"));
        loggedInUser.setCookie("RefreshToken", intent.getStringExtra("RefreshToken"));

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        HTTPClient = new httpClient(this.getApplicationContext());
        //initializing a shared repository
        SharedRoutesPlacesRepository sharedRepo = new SharedRoutesPlacesRepository();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_my_profile,
                R.id.nav_discover_places, R.id.nav_discover_routes,
                R.id.nav_my_fav_places, R.id.nav_my_fav_routes, R.id.nav_help)
                .setDrawerLayout(mDrawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        TextView name = (TextView) headerView.findViewById(R.id.drawer_name);
        name.setText(loggedInUser.getDisplayName());
        TextView email = (TextView) headerView.findViewById((R.id.drawer_email));
        email.setText(loggedInUser.getEmail());

        CheckAccessTokenThread thread = new CheckAccessTokenThread();
        thread.run();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static LoggedInUser getLoggedInUser() {
        return loggedInUser;
    }
}