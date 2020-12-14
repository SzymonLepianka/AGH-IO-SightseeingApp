package com.io.routesapp.ui.routes.routeInformation;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.io.routesapp.R;
import com.io.routesapp.SharedRoutesPlacesRepository;
import com.io.routesapp.ui.places.model.Place;
import com.io.routesapp.ui.routes.model.Route;
import com.io.routesapp.ui.routes.model.RouteReview;
import com.io.routesapp.ui.routes.model.RouteReviewAdapter;
import com.io.routesapp.ui.routes.repository.RouteReviewsRepository;

import java.util.ArrayList;

public class RouteInformationFragment extends Fragment {
    Route route;
    RecyclerView mRecyclerView;
    RouteReviewAdapter routeReviewAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<RouteReview> reviewsList;
    FloatingActionButton addFAB;
    View reviewField;
    private GoogleMap map;
    private Polyline polyline;
    private MarkerOptions place1, place2;


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
            map = googleMap;
            ArrayList<Place> placesInRoute = route.getPlacesOfRoute();
            for (int i = 1; i < placesInRoute.size(); i++) {
                place1 = new MarkerOptions().position(
                        new LatLng(placesInRoute.get(i - 1).getLatitude(),
                                placesInRoute.get(i - 1).getLongtitude())).title(
                        placesInRoute.get(i - 1).getName());
                place2 = new MarkerOptions().position(
                        new LatLng(placesInRoute.get(i).getLatitude(),
                                placesInRoute.get(i).getLongtitude())).title(
                        placesInRoute.get(i).getName());

                map.addMarker(place1);
                map.addMarker(place2);
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place2.getPosition(), 13));
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_route_information, container, false);

        route = SharedRoutesPlacesRepository.routesAvailable.get(0);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_route);
        mapFragment.getMapAsync(callback);


        mRecyclerView = (RecyclerView) root.findViewById(R.id.review_list);
        layoutManager = new LinearLayoutManager(getActivity());

        routeReviewAdapter = new RouteReviewAdapter(SharedRoutesPlacesRepository.routeReviews);
        mRecyclerView.setAdapter(routeReviewAdapter);

        addFAB = root.findViewById(R.id.add_fab);
        reviewField = root.findViewById(R.id.review_field);

        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewField.setVisibility(View.VISIBLE);
                addFAB.setVisibility(View.GONE);
            }
        });

        RatingBar placeRatingBar = root.findViewById(R.id.place_rating_bar);
        final TextInputEditText reviewText= root.findViewById(R.id.review_text);
        final FloatingActionButton sendFAB = root.findViewById(R.id.send_fab);

        reviewText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!reviewText.getText().toString().isEmpty()){
                    sendFAB.setEnabled(true);
                }
            }
        });

        sendFAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedRoutesPlacesRepository.routeReviews.add(new RouteReview(0, 0, reviewText.getText().toString()));
                reviewField.setVisibility(View.GONE);
            }
        });

        return root;
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

    private void initReviewsList() {
        RouteReviewsRepository repo = new RouteReviewsRepository();
        reviewsList = repo.getReviewsList();
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

}
