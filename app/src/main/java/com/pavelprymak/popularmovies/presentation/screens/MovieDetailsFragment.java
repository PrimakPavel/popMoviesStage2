package com.pavelprymak.popularmovies.presentation.screens;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.popularmovies.App;
import com.pavelprymak.popularmovies.R;
import com.pavelprymak.popularmovies.databinding.FragmentMovieDetailsBinding;
import com.pavelprymak.popularmovies.db.FavoriteMovieEntry;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.GenresItem;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.MovieDetailsResponse;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.ProductionCountriesItem;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.SpokenLanguagesItem;
import com.pavelprymak.popularmovies.network.pojo.movieReviews.ReviewsResultsItem;
import com.pavelprymak.popularmovies.network.pojo.movieVideos.TrailersResultsItem;
import com.pavelprymak.popularmovies.presentation.adapter.ReviewAdapter;
import com.pavelprymak.popularmovies.presentation.adapter.ReviewListItemClickListener;
import com.pavelprymak.popularmovies.presentation.adapter.TrailerAdapter;
import com.pavelprymak.popularmovies.presentation.adapter.TrailerListClickListener;
import com.pavelprymak.popularmovies.presentation.viewModels.MovieDetailsViewModel;
import com.pavelprymak.popularmovies.utils.StringUtils;
import com.pavelprymak.popularmovies.utils.YoutubeUtil;
import com.pavelprymak.popularmovies.utils.convertors.TimeConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.pavelprymak.popularmovies.network.Constants.PICTURE_HUGE_BASE_URL;


public class MovieDetailsFragment extends Fragment implements TrailerListClickListener, ReviewListItemClickListener {
    private FragmentMovieDetailsBinding mBinding;
    static final String ARG_MOVIE_ID = "argMovieId";
    private int mMovieId;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private MovieDetailsResponse mMovieDetails;
    private MovieDetailsViewModel detailsViewModel;


    private void onRetryBtnClick() {
        mBinding.errorLayout.setVisibility(View.GONE);
        detailsViewModel.getMovieDetailsById(mMovieId, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMovieId = getArguments().getInt(ARG_MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
        detailsViewModel.getMovieDetailsById(mMovieId, false);
        detailsViewModel.getMovieVideosById(mMovieId, false);
        detailsViewModel.getMovieReviewsById(mMovieId, false);
        detailsViewModel.movieDetailsData.getData().observe(this, this::showAllMovieDetails);
        detailsViewModel.movieDetailsData.getError().observe(this, throwable -> mBinding.errorLayout.setVisibility(View.VISIBLE));
        detailsViewModel.movieDetailsData.getLoading().observe(this, this::showProgressBar);
        detailsViewModel.movieVideosData.getData().observe(this, movieTrailersResponse -> {
            if (movieTrailersResponse != null) {
                List<TrailersResultsItem> trailersList = movieTrailersResponse.getResults();
                if (trailersList != null && !trailersList.isEmpty())
                    mBinding.trailersLabelTv.setVisibility(View.VISIBLE);
                else mBinding.trailersLabelTv.setVisibility(View.GONE);
                mTrailerAdapter.updateList(trailersList);
            }
        });
        detailsViewModel.movieReviewsData.getData().observe(this, movieReviewsResponse -> {
            if (movieReviewsResponse != null) {
                List<ReviewsResultsItem> reviewsList = movieReviewsResponse.getResults();
                if (reviewsList != null && !reviewsList.isEmpty())
                    mBinding.userReviewsLabel.setVisibility(View.VISIBLE);
                else mBinding.userReviewsLabel.setVisibility(View.GONE);
                mReviewAdapter.updateList(reviewsList);
            }
        });
        mBinding.retryBtn.setOnClickListener(v -> onRetryBtnClick());
        prepareFavoriteBtn();
        initTrailersRecyclerView();
        initReviewsRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detailsViewModel.movieDetailsData.removeObservers(this);
    }

    private void showAllMovieDetails(MovieDetailsResponse movieDetails) {
        mMovieDetails = movieDetails;
        showMoviePoster(movieDetails.getPosterPath());
        //Title
        if (movieDetails.getOriginalTitle() != null) {
            mBinding.movieTitleTv.setText(movieDetails.getOriginalTitle());
        }
        //Duration
        if (movieDetails.getRuntime() != null && movieDetails.getRuntime() > 0) {
            mBinding.durationTv.setVisibility(View.VISIBLE);
            mBinding.durationLabelTv.setVisibility(View.VISIBLE);
            mBinding.durationTv.setText(TimeConverter.convertMinToHHMM(movieDetails.getRuntime()));
        }

        //Countries
        if (movieDetails.getProductionCountries() != null) {
            ArrayList<String> countryNames = new ArrayList<>();
            for (ProductionCountriesItem country : movieDetails.getProductionCountries()) {
                if (country.getName() != null)
                    countryNames.add(country.getName());
            }
            if (!countryNames.isEmpty()) {
                mBinding.countryTv.setVisibility(View.VISIBLE);
                mBinding.countryLabelTv.setVisibility(View.VISIBLE);
                mBinding.countryTv.setText(StringUtils.buildStringWithComaSeparator(countryNames));
            }
        }
        //Release Date
        if (movieDetails.getReleaseDate() != null) {
            mBinding.releaseDateTv.setVisibility(View.VISIBLE);
            mBinding.releaseDateLabelTv.setVisibility(View.VISIBLE);
            mBinding.releaseDateTv.setText(movieDetails.getReleaseDate());
        }

        //Genre
        if (movieDetails.getGenres() != null) {
            ArrayList<String> genreNames = new ArrayList<>();
            for (GenresItem genre : movieDetails.getGenres()) {
                if (genre.getName() != null)
                    genreNames.add(genre.getName());
            }
            if (!genreNames.isEmpty()) {
                mBinding.genreTv.setVisibility(View.VISIBLE);
                mBinding.genreLabelTv.setVisibility(View.VISIBLE);
                mBinding.genreTv.setText(StringUtils.buildStringWithComaSeparator(genreNames));
            }
        }

        //Audio
        if (movieDetails.getSpokenLanguages() != null) {
            ArrayList<String> languagesNames = new ArrayList<>();
            for (SpokenLanguagesItem language : movieDetails.getSpokenLanguages()) {
                if (language.getName() != null)
                    languagesNames.add(language.getName());
            }
            if (!languagesNames.isEmpty()) {
                mBinding.audioTv.setVisibility(View.VISIBLE);
                mBinding.audioLabelTv.setVisibility(View.VISIBLE);
                mBinding.audioTv.setText(StringUtils.buildStringWithComaSeparator(languagesNames));
            }
        }
        //Rating
        if (movieDetails.getVoteAverage() > 0.0) {
            mBinding.voteTv.setVisibility(View.VISIBLE);
            mBinding.voteLabelTv.setVisibility(View.VISIBLE);
            mBinding.voteTv.setText(String.valueOf(movieDetails.getVoteAverage()));
        }

        //Budget
        if (movieDetails.getBudget() != null && movieDetails.getBudget() > 0) {
            mBinding.budgetTv.setVisibility(View.VISIBLE);
            mBinding.budgetLabelTv.setVisibility(View.VISIBLE);
            mBinding.budgetTv.setText(String.format(Locale.ENGLISH, "%,d$", movieDetails.getBudget()).replace(',', ' '));
        }

        //Revenue
        if (movieDetails.getRevenue() != null && movieDetails.getRevenue() > 0) {
            mBinding.revenueTv.setVisibility(View.VISIBLE);
            mBinding.revenueLabelTv.setVisibility(View.VISIBLE);
            mBinding.revenueTv.setText(String.format(Locale.ENGLISH, "%,d$", movieDetails.getRevenue()).replace(',', ' '));
        }

        //Homepage
        if (movieDetails.getHomepage() != null) {
            mBinding.homepageTv.setVisibility(View.VISIBLE);
            mBinding.homepageLabelTv.setVisibility(View.VISIBLE);
            mBinding.homepageTv.setText(movieDetails.getHomepage());
        }

        //Description
        if (movieDetails.getOverview() != null) {
            mBinding.movieDescriptionTv.setText(movieDetails.getOverview());
        }

    }

    private void showProgressBar(Boolean isShow) {
        if (isShow != null && isShow) {
            mBinding.progressBar.setVisibility(View.VISIBLE);
        } else {
            mBinding.progressBar.setVisibility(View.GONE);
        }
    }

    private void prepareFavoriteBtn() {
        App.dbRepo.loadFavoriteMovieById(mMovieId).observe(this, favoriteMovieEntry -> {
            if (favoriteMovieEntry == null) {
                mBinding.favoriteBtn.setText(R.string.add_to_favorite);
                mBinding.favoriteBtn.setOnClickListener(v -> addToFavorite());
            } else {
                mBinding.favoriteBtn.setText(R.string.delete_from_favorite);
                mBinding.favoriteBtn.setOnClickListener(v -> deleteFromFavorite());
            }
        });
    }

    private void addToFavorite() {
        FavoriteMovieEntry favoriteMovie = new FavoriteMovieEntry(mMovieDetails.getId(),
                new Date(),
                mMovieDetails.getTitle(),
                mMovieDetails.getPosterPath(),
                mMovieDetails.getReleaseDate(),
                mMovieDetails.getVoteAverage(),
                mMovieDetails.getOriginalLanguage());
        App.dbRepo.insertFavoriteMovie(favoriteMovie);
    }

    private void deleteFromFavorite() {
        App.dbRepo.deleteByMovieId(mMovieId);
    }

    private void showMoviePoster(String posterPath) {
        if (posterPath != null)
            Picasso.get().load(PICTURE_HUGE_BASE_URL + posterPath).into(mBinding.moviePosterIv);
    }

    private void initTrailersRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.list_colons_num), RecyclerView.VERTICAL, false);
        mBinding.recyclerTrailers.setLayoutManager(layoutManager);
        mBinding.recyclerTrailers.setHasFixedSize(true);
        mBinding.recyclerTrailers.setNestedScrollingEnabled(false);
        mTrailerAdapter = new TrailerAdapter(this);
        mBinding.recyclerTrailers.setAdapter(mTrailerAdapter);
    }

    private void initReviewsRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.list_colons_num), RecyclerView.VERTICAL, false);
        mBinding.recyclerReviews.setLayoutManager(layoutManager);
        mBinding.recyclerReviews.setHasFixedSize(true);
        mBinding.recyclerReviews.setNestedScrollingEnabled(false);
        mReviewAdapter = new ReviewAdapter(this);
        mBinding.recyclerReviews.setAdapter(mReviewAdapter);
    }

    @Override
    public void onTrailerItemClick(String id, String key) {
        if (getContext() != null)
            YoutubeUtil.watchYoutubeVideo(getContext(), key);
    }

    @Override
    public void onReviewItemClick(String id) {
        //ignore
    }
}
