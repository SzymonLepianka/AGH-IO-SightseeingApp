package com.io.routesapp.ui.places.model;

public class Place {
    int id;
    String name;
    Boolean valid;
    Double latitude;
    Double longtitude;
    int googleMapsId;
    int accumulatedScore;
    int usersVoted;
    String description;

    public Place(int id, String name, Boolean validPlace,
                 Double latitude, Double longtitude, int googleMapsId,
                 int accumulatedScore, int usersVoted, String description)
    {
        this.id = id;
        this.name = name;
        this.valid = validPlace;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.googleMapsId = googleMapsId;
        this.accumulatedScore = accumulatedScore;
        this.usersVoted = usersVoted;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getValidPlace() {
        return valid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public int getGoogleMapsId() {
        return googleMapsId;
    }

    public int getAccumulatedScore() {
        return accumulatedScore;
    }

    public int getUsersVoted() {
        return usersVoted;
    }

    public String getDescription() {
        return description;
    }
}
