package com.io.routesapp.ui.places.repository;

import com.io.routesapp.ui.places.model.Place;

import java.util.ArrayList;

public class PlacesRepository {
    ArrayList<Place> placesList = new ArrayList<>();

    ArrayList<Place> init(){
        placesList.add(new Place(
                0,
                "Wawel",
                true,50.054316,19.9350402,
                0,
                0,
                0,
                "Wawel Castle"));
        placesList.add(new Place(
                1,
                "Sukiennice",
                true,50.0613741,19.9361222,
                0,
                0,
                0,
                "The Cloth Hall"));
        placesList.add(new Place(
                2,
                "Barbakan Krakowski",
                true,50.065569, 19.941689,
                0,
                0,
                0,
                "Kraków Barbican"));
        return placesList;
    }

    public ArrayList<Place> getPlaces(){
        this.init();
        return placesList;
    }
}
