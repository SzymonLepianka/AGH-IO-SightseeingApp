package com.io.routesapp.ui.places.discoverPlaces;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.io.routesapp.ui.places.model.Place;

import java.util.List;

public class DiscoverPlacesViewModel extends ViewModel {
    MutableLiveData<List<Place>> places = new MutableLiveData<>();




}