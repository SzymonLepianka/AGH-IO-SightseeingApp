package com.io.routesapp;

import com.io.routesapp.ui.places.model.Place;
import com.io.routesapp.ui.places.model.PlaceReview;
import com.io.routesapp.ui.routes.model.Route;
import com.io.routesapp.ui.routes.model.RouteReview;

import java.util.ArrayList;

public class SharedRoutesPlacesRepository {
    public static ArrayList<Place> placesAvailable;
    public static ArrayList<Route> routesAvailable;
    public static ArrayList<PlaceReview> placeReviews;
    public static ArrayList<RouteReview> routeReviews;

    public SharedRoutesPlacesRepository() {
        this.init();
    }

    public void init(){
        placesAvailable = new ArrayList<>();
        placesAvailable.add(new Place(
                0,
                "Wawel",
                true,50.054316,19.9350402,
                0,
                0,
                0,
                "Wawel Castle"));
        placesAvailable.add(new Place(
                1,
                "Sukiennice",
                true,50.0613741,19.9361222,
                0,
                0,
                0,
                "The Cloth Hall"));
        placesAvailable.add(new Place(
                2,
                "Barbakan Krakowski",
                true,50.065569, 19.941689,
                0,
                0,
                0,
                "Kraków Barbican"));

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

        placeReviews = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            placeReviews.add(new PlaceReview(0, 0, "Wonderful place!"
            ));
        }

        routeReviews = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            routeReviews.add(new RouteReview(
                    0, 0, "I loved this route, although it was a bit too short!"
            ));
        }
    }

    public ArrayList<Place> getPlacesAvailable() {
        return placesAvailable;
    }

    public ArrayList<Route> getRoutesAvailable() {
        return routesAvailable;
    }
}
