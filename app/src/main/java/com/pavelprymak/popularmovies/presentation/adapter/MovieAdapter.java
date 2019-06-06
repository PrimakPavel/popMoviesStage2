package com.pavelprymak.popularmovies.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.popularmovies.R;
import com.pavelprymak.popularmovies.databinding.ItemViewMovieBinding;
import com.pavelprymak.popularmovies.network.pojo.moviesList.ResultsItem;
import com.squareup.picasso.Picasso;

import static com.pavelprymak.popularmovies.network.Constants.PICTURE_BASE_URL;

public class MovieAdapter extends PagedListAdapter<ResultsItem, MovieAdapter.ViewHolderMovie> {
    private Context mContext;
    private MoviesListItemClickListener clickListener;



    private static final DiffUtil.ItemCallback<ResultsItem> diffUtilCallback = new DiffUtil.ItemCallback<ResultsItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull ResultsItem oldItem, @NonNull ResultsItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ResultsItem oldItem, @NonNull ResultsItem newItem) {
            return oldItem.getId() == newItem.getId();
        }
    };

    public MovieAdapter(MoviesListItemClickListener listener) {
        super(diffUtilCallback);
        clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolderMovie onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        ItemViewMovieBinding itemViewMovieBinding = DataBindingUtil.inflate(layoutInflater,R.layout.item_view_movie,parent,false);
        return new ViewHolderMovie(itemViewMovieBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMovie holder, int position) {
        holder.bind(position);
    }

    public class ViewHolderMovie extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemViewMovieBinding binding;

        private static final String EMPTY = "";

        ViewHolderMovie(@NonNull ItemViewMovieBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            itemBinding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            ResultsItem movie = getItem(position);
            if (movie != null && mContext != null) {
                //LOGO
                if (movie.getPosterPath() != null) {
                    Picasso.get()
                            .load(PICTURE_BASE_URL + movie.getPosterPath())
                            .into(binding.movieImageIv);
                } else {
                    binding.movieImageIv.setImageDrawable(null);
                }
                //TITLE
                if (movie.getOriginalTitle() != null) {
                    binding.movieTitleTv.setText(movie.getOriginalTitle());
                } else {
                    binding.movieTitleTv.setText(EMPTY);
                }
                //RELEASE DATE
                if (movie.getReleaseDate() != null) {
                    String releaseDate = mContext.getResources().getString(R.string.movie_release_date) + movie.getReleaseDate();
                    binding.movieReleaseDateTv.setText(releaseDate);
                } else {
                    binding.movieReleaseDateTv.setText(EMPTY);
                }
                //ORIGINAL LANGUAGE
                if (movie.getOriginalLanguage() != null) {
                    String originalLanguage = mContext.getResources().getString(R.string.movie_original_language) + movie.getOriginalLanguage();
                   binding.movieOriginalLanguageTv.setText(originalLanguage);
                } else {
                   binding.movieOriginalLanguageTv.setText(EMPTY);
                }
                //RATING
                if (movie.getVoteAverage() > 0.0) {
                    String rating = mContext.getResources().getString(R.string.movie_rating) + movie.getVoteAverage();
                    binding.movieRatingTv.setText(rating);
                } else {
                    binding.movieRatingTv.setText(EMPTY);
                }
            }
        }

        @Override
        public void onClick(View v) {
            ResultsItem movie = getItem(getAdapterPosition());
            if (movie != null) {
                clickListener.onMovieItemClick(movie.getId());
            }
        }
    }
}
