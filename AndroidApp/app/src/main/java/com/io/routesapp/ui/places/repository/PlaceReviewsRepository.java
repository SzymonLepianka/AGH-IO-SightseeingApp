package com.io.routesapp.ui.places.repository;

import com.io.routesapp.ui.places.model.PlaceReview;

import java.util.ArrayList;

public class PlaceReviewsRepository {
    ArrayList<PlaceReview> reviewsList = new ArrayList<>();

    ArrayList<PlaceReview> init() {
        for (int i = 0; i < 5; i++) {
            reviewsList.add(new PlaceReview(
                    "0", "", "Wonderful place!"
            ));
        }
        return reviewsList;
    }

    public ArrayList<PlaceReview> getReviewsList(){
        this.init();
        return reviewsList;
    }
}
