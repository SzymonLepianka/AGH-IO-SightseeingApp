package com.io.routesapp.data.model;

import android.content.Context;
import android.util.Log;

import com.io.routesapp.R;
import com.io.routesapp.ui.places.model.Place;
import com.io.routesapp.ui.places.model.PlaceReview;
import com.io.routesapp.ui.reviews.Review;
import com.io.routesapp.ui.routes.model.Route;
import com.io.routesapp.ui.routes.model.RouteReview;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class httpClient {
    private static JSONObject placesListJSON;
    private static JSONObject placeJSON;
    private static JSONObject placeCommentListJSON;
    private static JSONObject routesListJSON;
    private static JSONObject routeCommentListJSON;
    private Context context;
    private OkHttpClient client;
    private String baseURL;

    public httpClient(Context context) {
        this.context = context;
        client = new OkHttpClient();
        baseURL = context.getResources().getString(R.string.baseUrl);
    }

    public ArrayList<Place> getPlaces() throws JSONException, InterruptedException {
        String url = baseURL + "/places"; //10.0.2.2 - localhost

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

    public Place getPlace(int id) throws JSONException, InterruptedException{

        // send request to server to get place details, parse place information
        // and return new place object

        //tam chyba był inny URL wymagany path=places/{id}
        String url = baseURL + "/places/" + id; //10.0.2.2 - localhost

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

        //taki URL mamy w Server/Controllers/PlacesController getPlaceComment
        String url = baseURL + "/places/" + id + "/comments"; //10.0.2.2 - localhost

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
        for (int i = 0; i < idArray.length(); i++){
            JSONObject placeInfo = placeCommentListJSON.getJSONObject(String.valueOf(id));
            int authorID = placeInfo.getInt("username");
            String content = placeInfo.getString("content");
            PlaceReview newPlaceReviewFromJSON = new PlaceReview(id, authorID, content);
            placeReviewList.add(newPlaceReviewFromJSON);
        }

        return  placeReviewList;
    }

    //TODO nie było mapowania dla ulubonych w Server \Gosia
    public ArrayList<Place> getFavouritePlaces(int userID){

        //TODO get list of favourite places IDs for the user
        // then get all that places information and return list of favourite places

        return new ArrayList<>();
    }

    public ArrayList<Route> getRoutes() throws JSONException, InterruptedException {
        //get all public routes
        // route constructor: Route(int id, int accumulated score)
        // return list of routes
        String url = baseURL + "/routes"; //10.0.2.2 - localhost

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
        String url = baseURL + "/routes/" + id + "/comments"; //10.0.2.2 - localhost

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

        //dla każdego route zwraca JSON z RouteID, UserID, Content
        //TODO sprawdziłabym w AGH-IO-SightseeingApp/Server/src/main/java/Server/Controllers/RoutesController.java  buildJsonRouteCommen tam przy Mapping
        //  wrzuca routeID i potem routeID jeszcze raz (JSON in JSON ? ) (chyba ja to robiłam, więc my bad) /Gosia
        ArrayList<RouteReview> routeReviewList = new ArrayList<>();
        JSONArray idArray = routeCommentListJSON.names();
        for (int i = 0; i < idArray.length(); i++){
            JSONObject placeInfo = routeCommentListJSON.getJSONObject(String.valueOf(id));
            int authorID = placeInfo.getInt("userId");
            String content = placeInfo.getString("content");
            RouteReview newRouteReviewFromJSON = new RouteReview(id, authorID, content);
            routeReviewList.add(newRouteReviewFromJSON);
        }
        return  routeReviewList;
    }

    //TODO same here, nie było ulubionych \Gosia
    public ArrayList<Route> getFavouriteRoutes(int userID){
        //TODO get favourite routes list for this user
        return  new ArrayList<>();
    }
}
