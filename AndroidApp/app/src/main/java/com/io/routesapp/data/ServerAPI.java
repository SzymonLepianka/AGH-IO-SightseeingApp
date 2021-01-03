package com.io.routesapp.data;

import com.io.routesapp.ui.places.model.Place;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServerAPI {

    @GET("places")
    Call<String> getPlaces();
}
