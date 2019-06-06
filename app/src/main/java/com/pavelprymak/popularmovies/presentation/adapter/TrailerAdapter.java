package com.pavelprymak.popularmovies.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.popularmovies.R;
import com.pavelprymak.popularmovies.databinding.ItemViewTrailerBinding;
import com.pavelprymak.popularmovies.network.pojo.movieVideos.TrailersResultsItem;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private List<TrailersResultsItem> mTrailers;
    private TrailerListClickListener clickListener;
    private Context mContext;

    public TrailerAdapter(TrailerListClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void updateList(List<TrailersResultsItem> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemViewTrailerBinding itemViewTrailerBinding = DataBindingUtil.inflate(inflater, R.layout.item_view_trailer, parent, false);
        return new TrailerViewHolder(itemViewTrailerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mTrailers == null) return 0;
        else return mTrailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemViewTrailerBinding binding;
        private static final String EMPTY = "";

        TrailerViewHolder(@NonNull ItemViewTrailerBinding trailerBinding) {
            super(trailerBinding.getRoot());
            this.binding = trailerBinding;
            trailerBinding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            TrailersResultsItem trailer = mTrailers.get(position);
            if (trailer != null && mContext != null) {
                //Name
                if (trailer.getName() != null) {
                    binding.trailerTitle.setText(trailer.getName());
                } else {
                    binding.trailerTitle.setText(EMPTY);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (mTrailers != null) {
                TrailersResultsItem trailer = mTrailers.get(getAdapterPosition());
                if (trailer != null) {
                    clickListener.onTrailerItemClick(trailer.getId(), trailer.getKey());
                }
            }
        }
    }
}
