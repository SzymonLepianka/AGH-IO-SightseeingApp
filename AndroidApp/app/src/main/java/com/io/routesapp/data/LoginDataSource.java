package com.io.routesapp.data;

import android.app.Application;
import android.util.Log;

import com.io.routesapp.R;
import com.io.routesapp.data.model.LoggedInUser;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.okhttp3.OkHttp3CookieHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    Response responseCopy;
    private String IOBaseURL = "http://10.0.2.2:8080/";
    private JSONObject userJSON;
    String userID;
    String displayName;
    HashMap<String, String> cookies = new HashMap<>();

    public Result<LoggedInUser> login(String username, String password) {

        try {
                // TODO: handle loggedInUser authentication
                getAuthCode(username, password);
                getAccessTokenAndRefreshToken();
                LoggedInUser user = getUserData(username);

                    return new Result.Success<>(user);
            } catch (Exception e) {
                return new Result.Error(new IOException("Error logging in", e));
            }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    private void getAuthCode(String username, String password) throws InterruptedException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("clientID", "2")
                .add("username", username)
                .add("password", password)
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

        while (cookies.isEmpty()){
            Thread.sleep(10);
        }
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
                    String cookie = (response.headers().values("Set-Cookie").get(0).split(";")[0]);
                    cookies.put(cookie.split("=")[0], cookie.split("=")[1]);
                    cookie = (response.headers().values("Set-Cookie").get(1).split(";")[0]);
                    cookies.put(cookie.split("=")[0], cookie.split("=")[1]);
                }
            }
        });

        while (cookies.size() != 3) {
            Thread.sleep(10);
        }
    }

    private LoggedInUser getUserData(String username) throws InterruptedException, JSONException, IOException {

        String url = IOBaseURL + "users/" + username;

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken" , cookies.get("AccessToken"));

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d("OKHTTP", String.valueOf(response.message()));
                    //throw new IOException(String.valueOf(response.code()));
                } else {
                    try {
                        responseCopy = response;
                        userJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        while (userJSON == null) {
            Thread.sleep(10);
        }

        Log.d("NAMES", String.valueOf(userJSON.names().get(0)));
        String userID = userJSON.getJSONObject((String) userJSON.names().get(0)).getString("username");
        String displayName = userJSON.getJSONObject((String) userJSON.names().get(0)).getString("firstname") +
                " " + userJSON.getJSONObject((String) userJSON.names().get(0)).getString("surname");
        return new LoggedInUser(userID, displayName);
    }
}