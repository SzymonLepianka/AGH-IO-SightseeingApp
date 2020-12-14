package com.io.routesapp.ui.routes.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.io.routesapp.R;
import com.io.routesapp.ui.places.model.PlaceReview;

import java.util.ArrayList;


public class RouteReviewAdapter extends RecyclerView.Adapter<com.io.routesapp.ui.places.model.PlaceReviewAdapter.ViewHolder> {
    private ArrayList<RouteReview> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView authorNameTextView;
        private final TextView contentTextView;
        private final RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            authorNameTextView = (TextView) view.findViewById(R.id.author_name);
            ratingBar = (RatingBar) view.findViewById(R.id.author_rating);
            contentTextView = (TextView) view.findViewById(R.id.review_content);
        }

        public TextView getAuthorNameTextView() {
            return authorNameTextView;
        }

        public TextView getContentTextView() {
            return contentTextView;
        }

        public RatingBar getRatingBar() {
            return ratingBar;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet  containing the data to populate views to be used
     * by RecyclerView.
     */
    public RouteReviewAdapter(ArrayList<RouteReview> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public com.io.routesapp.ui.places.model.PlaceReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_review, viewGroup, false);

        return new com.io.routesapp.ui.places.model.PlaceReviewAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(com.io.routesapp.ui.places.model.PlaceReviewAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getAuthorNameTextView().setText(localDataSet.get(position).getAuthorID().toString());
        viewHolder.getRatingBar().setIsIndicator(true);
        viewHolder.getRatingBar().setRating((float) 4.0); //TODO get rating
        viewHolder.getContentTextView().setText(localDataSet.get(position).getContent());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}