package com.io.routesapp.ui.routes.discoverRoutes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.io.routesapp.R;
import com.io.routesapp.SharedRoutesPlacesRepository;
import com.io.routesapp.ui.routes.model.Route;
import com.io.routesapp.ui.routes.model.RouteAdapter;
import com.io.routesapp.ui.routes.repository.RoutesRepository;

import java.util.ArrayList;

public class DiscoverRoutesFragment extends Fragment {

    RecyclerView mRecyclerView;
    RouteAdapter routeAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Route> routesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discover_routes, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.routes_list);
        layoutManager = new LinearLayoutManager(getActivity());

        routeAdapter = new RouteAdapter(SharedRoutesPlacesRepository.routesAvailable);
        mRecyclerView.setAdapter(routeAdapter);

        return root;
    }

    private void initRoutesList() {
        RoutesRepository repo = new RoutesRepository();
        routesList = repo.getRoutesList();
    }

    private void getRoutesFromSharedRepo() {
        routesList = SharedRoutesPlacesRepository.routesAvailable;
    }
}