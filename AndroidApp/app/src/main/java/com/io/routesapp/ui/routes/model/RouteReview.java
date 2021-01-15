package com.io.routesapp.ui.routes.model;

import com.io.routesapp.ui.reviews.Review;

public class RouteReview extends Review {
    String routeID;

    public RouteReview(String routeID, String authorID, String content) {
        this.routeID = routeID;
        this.setAuthorID(authorID);
        this.setContent(content);
    }

    public String getPlaceID() {
        return routeID;
    }

    public void setPlaceID(String routeID) {
        this.routeID = routeID;
    }

}
