package com.io.routesapp.ui.places.discoverPlaces;

import android.os.Bundle;
import android.util.Log;
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
import com.io.routesapp.MainActivity;
import com.io.routesapp.R;
import com.io.routesapp.SharedRoutesPlacesRepository;
import com.io.routesapp.ui.places.model.Place;
import com.io.routesapp.ui.places.model.PlaceAdapter;
import com.io.routesapp.ui.places.repository.PlacesRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class DiscoverPlacesFragment extends Fragment {

    RecyclerView mRecyclerView;
    PlaceAdapter placeAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Place> placesList = new ArrayList<>();
    FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getPlacesFromDataSource();
        } catch (IOException | JSONException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discover_places, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.places_list);
        layoutManager = new LinearLayoutManager(getActivity());

        placeAdapter = new PlaceAdapter(placesList);
        mRecyclerView.setAdapter(placeAdapter);

        fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_discover_places_to_discover_places_map);
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

    private void getPlacesFromDataSource() throws IOException, JSONException, InterruptedException {
        placesList = MainActivity.HTTPClient.getPlaces();
    }

}