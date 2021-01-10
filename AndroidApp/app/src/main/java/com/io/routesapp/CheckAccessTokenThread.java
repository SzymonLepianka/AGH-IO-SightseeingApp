package com.io.routesapp;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckAccessTokenThread implements Runnable {
    String DPRefreshTokenUrl = "http://10.0.2.2:8081/api/refreshToken?clientID=1&refreshToken=";

    @Override
    public void run() {
        String accessToken = MainActivity.getLoggedInUser().getCookies().get("AccessToken");

        while (true) {
            while (!ifExpiresSoon(accessToken)) {
                try {
                    Thread.sleep(600000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            refreshTokens();
        }
    }

    private Boolean ifExpiresSoon(String accessToken) {
        String[] split_string = accessToken.split("\\.");
        String base64EncodedBody = split_string[1];

        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));

        String[] split = body.split(",");

        if (split[2].startsWith("exp", 1)){
            long expiresAt = Long.parseLong(split[2].substring(6));
            System.out.println(expiresAt);
            return  (System.currentTimeMillis() - expiresAt) < 600000 ; // 1000L - obecny czas (ilość sekund od 1970 roku)
        }

        return false;
    }

    private void refreshTokens() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(DPRefreshTokenUrl + MainActivity.getLoggedInUser().getCookies().get("RefreshToken"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Error in response from DP Server :( error code: " + String.valueOf(response.code()));
                } else {
                    String cookie = (response.headers().values("Set-Cookie").get(0).split(";")[0]);
                    MainActivity.getLoggedInUser().getCookies().put(cookie.split("=")[0], cookie.split("=")[1]);
                    cookie = (response.headers().values("Set-Cookie").get(1).split(";")[0]);
                    MainActivity.getLoggedInUser().getCookies().put(cookie.split("=")[0], cookie.split("=")[1]);
                }
            }
        });

        while (MainActivity.getLoggedInUser().getCookies().size() != 3) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
