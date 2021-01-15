package com.io.routesapp.ui.places.model;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.io.routesapp.R;
import com.io.routesapp.SharedRoutesPlacesRepository;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private ArrayList<Place> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView placeNameView;
        private final TextView placeDescriptionView;
        private int placeID;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            cardView = (CardView) view.findViewById(R.id.place_card_view);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", placeID);
                    Navigation.findNavController(v).navigate(R.id.nav_place_information, bundle);
                }

            });

            placeNameView = (TextView) view.findViewById(R.id.place_name);
            placeDescriptionView = (TextView) view.findViewById(R.id.place_description);

            cardView.setOnLongClickListener( new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    Place place = new Place(0,
                            placeNameView.getText().toString(),
                            true,50.0613741,19.9361222,
                            0,
                            0,
                            0,
                            placeDescriptionView.getText().toString());
                    if (!SharedRoutesPlacesRepository.getFavouritePlacesNames().contains(place.name)) {
                        SharedRoutesPlacesRepository.favouritePlaces.add(place);
                    }
                    else{
                        int index = SharedRoutesPlacesRepository.getFavouritePlacesNames().indexOf(place.name);
                        SharedRoutesPlacesRepository.favouritePlaces.remove(SharedRoutesPlacesRepository.favouritePlaces.get(index));
                    }
                    Navigation.findNavController(v).navigate(R.id.nav_my_fav_places);
                    return true;
                }
            });
        }

        public TextView getPlaceNameView() {
            return placeNameView;
        }

        public TextView getPlaceDescriptionView() {
            return placeDescriptionView;
        }

        public void setPlaceID(int id){ placeID = id; };

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet  containing the data to populate views to be used
     * by RecyclerView.
     */
    public PlaceAdapter(ArrayList<Place> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_place, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getPlaceNameView().setText(localDataSet.get(position).getName());
        viewHolder.getPlaceDescriptionView().setText(localDataSet.get(position).getDescription());
        viewHolder.setPlaceID(localDataSet.get(position).getId());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
