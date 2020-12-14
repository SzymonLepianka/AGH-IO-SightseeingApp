package com.io.routesapp.ui.places.placeInformation;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.io.routesapp.R;
import com.io.routesapp.ui.places.model.PlaceReview;
import com.io.routesapp.ui.places.model.PlaceReviewAdapter;
import com.io.routesapp.ui.places.repository.PlaceReviewsRepository;

import java.util.ArrayList;

public class PlaceInformationFragment extends Fragment {
    private PlaceInformationViewModel placeInformationViewModel;
    RecyclerView mRecyclerView;
    PlaceReviewAdapter placeReviewAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PlaceReview> reviewsList;
    FloatingActionButton addFAB;
    View reviewField;


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
            LatLng wawel = new LatLng(50.054316,19.9350402);
            googleMap.addMarker(new MarkerOptions().position(wawel).title("Wawel"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wawel, 15));
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initReviewsList();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        placeInformationViewModel =
                ViewModelProviders.of(this).get(PlaceInformationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_place_information, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.review_list);
        layoutManager = new LinearLayoutManager(getActivity());

        placeReviewAdapter = new PlaceReviewAdapter(reviewsList);
        mRecyclerView.setAdapter(placeReviewAdapter);

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
                reviewsList.add(new PlaceReview(0, 0, reviewText.getText().toString()));
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
        PlaceReviewsRepository repo = new PlaceReviewsRepository();
        reviewsList = repo.getReviewsList();
    }
}
