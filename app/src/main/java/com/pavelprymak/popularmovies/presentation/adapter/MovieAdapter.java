package com.pavelprymak.popularmovies.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.popularmovies.R;
import com.pavelprymak.popularmovies.network.pojo.moviesList.ResultsItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        View view = layoutInflater.inflate(R.layout.item_view_movie, parent, false);
        return new ViewHolderMovie(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMovie holder, int position) {
        holder.bind(position);
    }

    public class ViewHolderMovie extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_image_iv)
        public ImageView mPosterIv;

        @BindView(R.id.movie_title_tv)
        public TextView mTitleTv;

        @BindView(R.id.movie_release_date)
        public TextView mReleaseDateTv;

        @BindView(R.id.movie_original_language)
        public TextView mOriginalLanguageTv;

        @BindView(R.id.movie_rating)
        public TextView mRatingTv;

        private static final String EMPTY = "";


        ViewHolderMovie(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            ResultsItem movie = getItem(position);
            if (movie != null && mContext != null) {
                //LOGO
                if (movie.getPosterPath() != null) {
                    Picasso.get()
                            .load(PICTURE_BASE_URL + movie.getPosterPath())
                            .into(mPosterIv);
                } else {
                    mPosterIv.setImageDrawable(null);
                }
                //TITLE
                if (movie.getOriginalTitle() != null) {
                    mTitleTv.setText(movie.getTitle());
                } else {
                    mTitleTv.setText(EMPTY);
                }
                //RELEASE DATE
                if (movie.getReleaseDate() != null) {
                    String releaseDate = mContext.getResources().getString(R.string.movie_release_date) + movie.getReleaseDate();
                    mReleaseDateTv.setText(releaseDate);
                } else {
                    mReleaseDateTv.setText(EMPTY);
                }
                //ORIGINAL LANGUAGE
                if (movie.getOriginalLanguage() != null) {
                    String originalLanguage = mContext.getResources().getString(R.string.movie_original_language) + movie.getOriginalLanguage();
                    mOriginalLanguageTv.setText(originalLanguage);
                } else {
                    mOriginalLanguageTv.setText(EMPTY);
                }
                //RATING
                if (movie.getVoteAverage() > 0.0) {
                    String rating = mContext.getResources().getString(R.string.movie_rating) + movie.getVoteAverage();
                    mRatingTv.setText(rating);
                } else {
                    mRatingTv.setText(EMPTY);
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
