package com.io.routesapp.data;

import android.util.Log;

import com.io.routesapp.data.model.LoggedInUser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private static Response responseCopy;
    HashMap<String, String> cookies = new HashMap<>();

    public Result<LoggedInUser> login(String username, String password) {

        try {
                // TODO: handle loggedInUser authentication
                getAuthCode();

                Log.d("OAUTHCODE", cookies.toString());

                getAccessTokenAndRefreshToken();

                Log.d("COOKIES", cookies.toString());

                LoggedInUser fakeUser =
                            new LoggedInUser(
                                    java.util.UUID.randomUUID().toString(),
                                    "Jane Doe");

                    return new Result.Success<>(fakeUser);
            } catch (Exception e) {
                return new Result.Error(new IOException("Error logging in", e));
            }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    private void getAuthCode(){
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("clientID", "2")
                .add("username", "slepianka3")
                .add("password", "password")
                .build();

        Log.d("OKHTTP POST BODY", body.toString());

        Request request = new Request.Builder()
                .url("http://10.0.2.2:8081/web/login")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d("OKHTTP", response.message());
                } else {
                    String cookie = (response.headers().get("Set-Cookie").split(";")[0]);
                    cookies.put(cookie.split("=")[0], cookie.split("=")[1]);
                }
            }
        });
    }

    private void getAccessTokenAndRefreshToken() throws InterruptedException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8081/api/createToken?clientID=2&authCode=" + cookies.get("AuthCode"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d("OKHTTP", response.message());
                    throw new IOException(String.valueOf(response.code()));
                } else {
                    responseCopy = response;
                    String cookie = (response.headers().values("Set-Cookie").get(0).split(";")[0]);
                    cookies.put(cookie.split("=")[0], cookie.split("=")[1]);
                    cookie = (response.headers().values("Set-Cookie").get(1).split(";")[0]);
                    cookies.put(cookie.split("=")[0], cookie.split("=")[1]);
                }
            }
        });
        Log.d("OKHTTP RESPONSE COPY", responseCopy.headers().toString());
    }

    private void postAccessToken(){
        OkHttpClient client = new OkHttpClient();
    }
}