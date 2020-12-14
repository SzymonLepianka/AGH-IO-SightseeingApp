package com.io.routesapp.ui.routes.model;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.io.routesapp.R;
import com.io.routesapp.ui.places.model.Place;

import java.util.ArrayList;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private ArrayList<Route> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView routeNameView;
        private final TextView routeDescriptionView;
        private final CardView cardView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            routeNameView = (TextView) view.findViewById(R.id.route_name);
            routeDescriptionView = (TextView) view.findViewById(R.id.route_description);
            cardView = (CardView) view.findViewById(R.id.route_card_view);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.nav_route_information);
                }
            });
        }

        public TextView getRouteNameView() {
            return routeNameView;
        }

        public TextView getRouteDescriptionView() {
            return routeDescriptionView;
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet  containing the data to populate views to be used
     * by RecyclerView.
     */
    public RouteAdapter(ArrayList<Route> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_route, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getRouteNameView().setText(localDataSet.get(position).getName());
        viewHolder.getRouteDescriptionView().setText(localDataSet.get(position).generateDescription());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}