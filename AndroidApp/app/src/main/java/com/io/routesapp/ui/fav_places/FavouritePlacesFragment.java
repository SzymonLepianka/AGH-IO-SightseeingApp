package com.io.routesapp.ui.fav_places;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.io.routesapp.R;
import com.io.routesapp.SharedRoutesPlacesRepository;
import com.io.routesapp.ui.places.discoverPlaces.DiscoverPlacesViewModel;
import com.io.routesapp.ui.places.model.Place;
import com.io.routesapp.ui.places.model.PlaceAdapter;
import com.io.routesapp.ui.places.repository.PlacesRepository;

import java.util.ArrayList;

public class FavouritePlacesFragment extends Fragment {

    private DiscoverPlacesViewModel discoverPlacesViewModel;
    RecyclerView mRecyclerView;
    PlaceAdapter placeAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Place> placesList;
    FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        discoverPlacesViewModel =
                ViewModelProviders.of(this).get(DiscoverPlacesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favourite_places, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.places_list);
        layoutManager = new LinearLayoutManager(getActivity());

        placeAdapter = new PlaceAdapter(SharedRoutesPlacesRepository.favouritePlaces);
        mRecyclerView.setAdapter(placeAdapter);

        fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_my_fav_places_to_nav_fav_places_map);
            }
        });
        return root;
    }

    private void initPlacesList() {
        PlacesRepository repo = new PlacesRepository();
        placesList = repo.getPlaces();
    }

    private void getPlacesFromSharedRepo() {
        placesList =  SharedRoutesPlacesRepository.placesAvailable;
    }

}