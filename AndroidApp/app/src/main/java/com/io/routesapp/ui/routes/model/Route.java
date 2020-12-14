package com.io.routesapp.ui.routes.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.io.routesapp.ui.places.model.Place;

import java.util.ArrayList;

public class Route {
    int id;
    int authorID;
    int accumulatedScore;
    int usersVoted;
    String name;
    Boolean isPublic;
    ArrayList<Place> placesOfRoute;


    public String getName() {
        return name;
    }

    public Route(int id) {
        placesOfRoute = new ArrayList<>();
        this.id = id;
        name = "Route " + Integer.toString(id);
    }

    public int getId() {
        return id;
    }

    public int getAuthorID() {
        return authorID;
    }

    public int getAccumulatedScore() {
        return accumulatedScore;
    }

    public int getUsersVoted() {
        return usersVoted;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public ArrayList<Place> getPlacesOfRoute() {
        return placesOfRoute;
    }

    private ArrayList<String> getPlacesNames(){
        ArrayList<String> placesNames = new ArrayList<>();
        for (Place place : placesOfRoute){
            placesNames.add(place.getName());
        }
        return placesNames;
    }

    public void setPlacesOfRoute(ArrayList<Place> placesOfRoute) {
        this.placesOfRoute = placesOfRoute;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String generateDescription(){
        return String.join(" -> ", this.getPlacesNames());
    }
}
