package com.io.routesapp.ui.routes.repository;

import com.io.routesapp.ui.routes.model.Route;

import java.util.ArrayList;

public class RoutesRepository {
    ArrayList<Route> routesList = new ArrayList<>();

    ArrayList<Route> init(){
        routesList.add(new Route(0));
        routesList.add(new Route(1));
        routesList.add(new Route(2));
        return routesList;
    }

    public ArrayList<Route> getRoutesList(){
        this.init();
        return routesList;
    }
}
