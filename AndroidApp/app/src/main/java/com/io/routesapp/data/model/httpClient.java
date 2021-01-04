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

    public Place getPlace(int id){

        //TODO send request to server to get place details, parse place information
        // and return new place object

        return new Place(
                0,
                "Wawel",
                true,50.054316,19.9350402,
                0,
                0,
                0,
                "Wawel Castle");
    }

    public ArrayList<PlaceReview> getPlaceReviews(int id){

        //TODO the same for reviews
        // review constructor: PlaceReview(int placeID, int authorID, String content)

        return  new ArrayList<>();
    }

    public ArrayList<Place> getFavouritePlaces(int userID){

        //TODO get list of favourite places IDs for the user
        // then get all that places information and return list of favourite places

        return new ArrayList<>();
    }

    public ArrayList<Route> getRoutes(){
        //TODO get all public routes
        // route constructor: Route(int id, int accumulated score)
        // return list of routes

        return new ArrayList<>();
    }

    public ArrayList<Place> getPointsOfRoute(int id){
        //TODO get points of route
        return new ArrayList<>();
    }

    public ArrayList<RouteReview> getRouteReviews(int id){
        //TODO get reviews for route
        // constructor: RouteReview(int routeID, int authorID, String content)
        return new ArrayList<>();
    }

    public ArrayList<Route> getFavouriteRoutes(int userID){
        //TODO get favourite routes list for this user
        return  new ArrayList<>();
    }
}
