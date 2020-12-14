package com.io.routesapp;

import com.io.routesapp.ui.places.model.Place;
import com.io.routesapp.ui.places.repository.PlacesRepository;
import com.io.routesapp.ui.routes.model.Route;

import java.util.ArrayList;

public class SharedRoutesPlacesRepository {
    ArrayList<Place> placesAvailable;
    ArrayList<Route> routesAvailable;

    public SharedRoutesPlacesRepository() {
        this.init();
    }

    public void init(){
        placesAvailable = new PlacesRepository().getPlaces();
        routesAvailable = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Route route = new Route(i);
            ArrayList<Place> placesInRoute = new ArrayList<>();
            for (Place place : placesAvailable) {
                placesInRoute.add(place);
            }
            route.setPlacesOfRoute(placesInRoute);
            routesAvailable.add(route);
        }
    }

    public ArrayList<Place> getPlacesAvailable() {
        return placesAvailable;
    }

    public ArrayList<Route> getRoutesAvailable() {
        return routesAvailable;
    }
}
