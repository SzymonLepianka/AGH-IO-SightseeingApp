package com.io.routesapp.ui.places.discoverPlaces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.io.routesapp.MainActivity;
import com.io.routesapp.R;
import com.io.routesapp.SharedRoutesPlacesRepository;
import com.io.routesapp.ui.places.model.Place;

import org.json.JSONException;

import java.util.ArrayList;

public class DiscoverPlacesMapFragment extends Fragment {
    ArrayList<Place> placesList;
    ArrayList<LatLng> placesCoordinates;
    ArrayList<String> placesNames;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placesCoordinates = new ArrayList<>();
        placesNames = new ArrayList<>();
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            for (int i = 0; i < placesCoordinates.size(); i++){
                LatLng coordinates = placesCoordinates.get(i);
                googleMap.addMarker(new MarkerOptions().position(coordinates).title(placesNames.get(i)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        try {
            placesList = MainActivity.HTTPClient.getPlaces();
        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
        }
        getPlacesNamesAndCoordinates(placesList,
                placesNames, placesCoordinates);
        return inflater.inflate(R.layout.fragment_discover_places_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void getPlacesNamesAndCoordinates(ArrayList<Place> placesList, ArrayList<String> placesNames, ArrayList<LatLng> placesCoordinates){
        for (Place place : placesList){
            placesCoordinates.add(new LatLng(place.getLatitude(), place.getLongitude()));
            placesNames.add(place.getName());
        }
    }
}