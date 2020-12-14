package com.io.routesapp.ui.places.model;

import com.io.routesapp.ui.reviews.Review;

public class PlaceReview extends Review {
    int placeID;

    public PlaceReview(int placeID, int authorID, String content) {
        this.placeID = placeID;
        this.setAuthorID(authorID);
        this.setContent(content);
    }

    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }

}
