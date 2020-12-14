package com.io.routesapp.ui.routes.model;

import com.io.routesapp.ui.reviews.Review;

public class RouteReview extends Review {
    int routeID;

    public RouteReview(int placeID, int authorID, String content) {
        this.routeID = placeID;
        this.setAuthorID(authorID);
        this.setContent(content);
    }

    public int getPlaceID() {
        return routeID;
    }

    public void setPlaceID(int routeID) {
        this.routeID = routeID;
    }

}
