package com.io.routesapp.data;

import android.content.Context;
import android.util.Log;

import com.io.routesapp.MainActivity;
import com.io.routesapp.R;
import com.io.routesapp.data.model.LoggedInUser;
import com.io.routesapp.ui.places.model.Place;
import com.io.routesapp.ui.places.model.PlaceReview;
import com.io.routesapp.ui.reviews.Review;
import com.io.routesapp.ui.routes.model.Route;
import com.io.routesapp.ui.routes.model.RouteReview;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.okhttp3.OkHttp3CookieHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class httpClient {
    private static JSONObject placesListJSON;
    private static JSONObject placeJSON;
    private static JSONObject placeCommentListJSON;
    private static JSONObject routesListJSON;
    private static JSONObject routeCommentListJSON;
    private static JSONObject userJSON;
    private Context context;
    private OkHttpClient client;
    private String baseURL;
    private String accessToken;

    public httpClient(Context context, String activityName) {
        this.context = context;
        baseURL = context.getResources().getString(R.string.baseUrl);
        if (activityName.equals("MainActivity")) {
            accessToken = MainActivity.getLoggedInUser().getCookies().get("AccessToken");
        }
    }

    public ArrayList<Place> getPlaces() throws JSONException, InterruptedException {
        placesListJSON = null;

        String url = baseURL + "/places"; //10.0.2.2 - localhost

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        Request request = new Request.Builder()
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
                    placesListJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        while (placesListJSON == null) {
            Thread.sleep(10);
        }

        ArrayList<Place> placesList = new ArrayList<>();
        JSONArray idArray = placesListJSON.names();
        for (int i = 0; i < idArray.length(); i++){
            String id = (String) idArray.get(i);
            JSONObject placeInfo = placesListJSON.getJSONObject(String.valueOf(id));
            Boolean valid = placeInfo.getBoolean("valid");
            Double latitude = placeInfo.getDouble("latitude");
            Double longitude = placeInfo.getDouble("longitude");
            String name = placeInfo.getString("name");
            String description = placeInfo.getString("description");
            int accumulatedScore = placeInfo.getInt("accumulatedScore");
            int usersVoted = placeInfo.getInt("usersVoted");
            Place newPlaceFromJSON = new Place(Integer.parseInt(id), name, valid,
                    latitude, longitude, 0,
                    accumulatedScore, usersVoted, description);
            placesList.add(newPlaceFromJSON);
        }

        return placesList;
    }

    public Place getPlace(int id) throws JSONException, InterruptedException {
        //setting json to null so as to wait for the new response
        placeJSON = null;

        // send request to server to get place details, parse place information
        // and return new place object

        //URL wymagany path=places/{id}
        String url = baseURL + "/places/" + id; //10.0.2.2 - localhost

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        Request request = new Request.Builder()
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
                    placeJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        while (placeJSON == null) {
            Thread.sleep(10);
        }

        JSONObject placeInfo = placeJSON.getJSONObject(String.valueOf(id));
        Boolean valid = placeInfo.getBoolean("valid");
        Double latitude = placeInfo.getDouble("latitude");
        Double longitude = placeInfo.getDouble("longitude");
        String name = placeInfo.getString("name");
        String description = placeInfo.getString("description");
        int accumulatedScore = placeInfo.getInt("accumulatedScore");
        int usersVoted = placeInfo.getInt("usersVoted");
        Place place = new Place(id, name, valid,
                latitude, longitude, 0,
                accumulatedScore, usersVoted, description);

        return place;
    }

    public ArrayList<PlaceReview> getPlaceReviews(int id) throws JSONException, InterruptedException{
        //the same for reviews
        // review constructor: PlaceReview(int placeID, int authorID, String content)
        placeCommentListJSON = null;


        String url = baseURL + "/places/" + id + "/comments"; //10.0.2.2 - localhost

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        Request request = new Request.Builder()
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
                    placeCommentListJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        while (placeCommentListJSON == null) {
            Thread.sleep(10);
        }

        //dla każdego comment zwraca JSON z CommentID, UserID, Content
        ArrayList<PlaceReview> placeReviewList = new ArrayList<>();
        JSONArray idArray = placeCommentListJSON.names();
        if (idArray != null) {
            for (int i = 0; i < idArray.length() ; i++){
                String reviewId = String.valueOf(idArray.get(i));
                JSONObject placeInfo = placeCommentListJSON.getJSONObject(reviewId);
                String authorID = placeInfo.getString("username");
                String content = placeInfo.getString("content");
                PlaceReview newPlaceReviewFromJSON = new PlaceReview(reviewId, authorID, content);
                placeReviewList.add(newPlaceReviewFromJSON);
            }
        }
        return  placeReviewList;
    }

    //nie było mapowania dla ulubonych w Server \Gosia
    public ArrayList<Place> getFavouritePlaces(int userID){

        //TODO get list of favourite places IDs for the user
        // then get all that places information and return list of favourite places

        return new ArrayList<>();
    }

    public ArrayList<Route> getRoutes() throws JSONException, InterruptedException {
        routesListJSON = null;
        //TODO get all public routes
        // route constructor: Route(int id, int accumulated score)
        // return list of routes
        String url = baseURL + "/routes"; //10.0.2.2 - localhost

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        Request request = new Request.Builder()
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
                    routesListJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        while (routesListJSON== null) {
            Thread.sleep(10);
        }

        ArrayList<Route> routesList = new ArrayList<>();
        JSONArray idArray = routesListJSON.names();
        for (int i = 0; i < idArray.length(); i++){
            String id = (String) idArray.get(i);
            JSONObject placeInfo = routesListJSON.getJSONObject(String.valueOf(id));
            int score = placeInfo.getInt("accumulatedScore");
            Route newRouteFromJSON = new Route(Integer.parseInt(id), score);
            routesList.add(newRouteFromJSON);
        }
        return routesList;
    }

    public ArrayList<Place> getPointsOfRoute(int id)  throws JSONException, InterruptedException{
        //TODO get points of route
        return new ArrayList<>();
    }

    public ArrayList<RouteReview> getRouteReviews(int id)  throws JSONException, InterruptedException{
        // get reviews for route
        // constructor: RouteReview(int routeID, int authorID, String content)
        routeCommentListJSON = null;
        String url = baseURL + "/routes/" + id + "/comments"; //10.0.2.2 - localhost

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        Request request = new Request.Builder()
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
                    routeCommentListJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        while (routeCommentListJSON == null) {
            Thread.sleep(10);
        }


        ArrayList<RouteReview> routeReviewList = new ArrayList<>();
        JSONArray idArray = routeCommentListJSON.names();
        for (int i = 0; i < idArray.length(); i++){
            String reviewId = String.valueOf(idArray.get(i));
            JSONObject placeInfo = routeCommentListJSON.getJSONObject(reviewId);
            String authorID = placeInfo.getString("username");
            String content = placeInfo.getString("content");
            RouteReview newRouteReviewFromJSON = new RouteReview(reviewId, authorID, content);
            routeReviewList.add(newRouteReviewFromJSON);
        }
        return  routeReviewList;
    }

    //nie było ulubionych \Gosia
    public ArrayList<Route> getFavouriteRoutes(int userID){
        //TODO get favourite routes list for this user
        return  new ArrayList<>();
    }

    public LoggedInUser getUserData(String username) throws InterruptedException, JSONException {
        String url = baseURL + "users/" + username;

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken" , accessToken);

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
                    throw new IOException(String.valueOf(response.code()));
                } else {
                    try {
                        userJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                        Log.d("OKHTTP RESPONSE", userJSON.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        while (userJSON == null) {
            Thread.sleep(10);
        }

        String userID = userJSON.getString("username");
        String displayName = userJSON.getString("first_name") +
                " " + userJSON.getString("surname");
        String email = userJSON.getString("email");
        return new LoggedInUser(userID, displayName, email, MainActivity.getLoggedInUser().getCookies());
    }

    public boolean isTokenValid(String accessToken) throws InterruptedException {
        String url = "http://10.0.2.2:8081/api/validateToken?clientID=2&accessToken=" + accessToken;

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        final String[] responseMessage = {null};

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException(String.valueOf(response.code()));
                } else {
                    responseMessage[0] = response.body().string();
                }
            }
        });

        while (responseMessage[0] == null){
            Thread.sleep(10);
        }

        return responseMessage[0].equals("Access Token is valid");
    }
}
