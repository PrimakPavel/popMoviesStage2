package com.pavelprymak.popularmovies.presentation.screens;


import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.pavelprymak.popularmovies.App;
import com.pavelprymak.popularmovies.R;
import com.pavelprymak.popularmovies.db.FavoriteMovieEntry;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.GenresItem;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.MovieDetailsResponse;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.ProductionCountriesItem;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.SpokenLanguagesItem;
import com.pavelprymak.popularmovies.network.pojo.movieReviews.MovieReviewsResponse;
import com.pavelprymak.popularmovies.network.pojo.movieReviews.ReviewsResultsItem;
import com.pavelprymak.popularmovies.network.pojo.movieVideos.MovieVideosResponse;
import com.pavelprymak.popularmovies.network.pojo.movieVideos.TrailersResultsItem;
import com.pavelprymak.popularmovies.presentation.common.BaseFragment;
import com.pavelprymak.popularmovies.presentation.viewModels.MovieDetailsViewModel;
import com.pavelprymak.popularmovies.utils.StringUtils;
import com.pavelprymak.popularmovies.utils.YoutubeUtil;
import com.pavelprymak.popularmovies.utils.convertors.TimeConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pavelprymak.popularmovies.network.Constants.PICTURE_HUGE_BASE_URL;


public class MovieDetailsFragment extends BaseFragment {

    static final String ARG_MOVIE_ID = "argMovieId";
    private int mMovieId;
    private MovieDetailsResponse mMovieDetails;
    private MovieDetailsViewModel detailsViewModel;
    @BindView(R.id.movie_poster_iv)
    ImageView mPosterIv;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.image_load_error_iv)
    ImageView mErrorLoadIv;
    @BindView(R.id.network_error_layout)
    LinearLayout mConnectionErrorLayout;
    @BindView(R.id.movie_title_tv)
    TextView mTitleTv;
    @BindView(R.id.duration_tv)
    TextView mDurationTv;
    @BindView(R.id.duration_label_tv)
    TextView mDurationLabelTv;
    @BindView(R.id.country_tv)
    TextView mCountryTv;
    @BindView(R.id.country_label_tv)
    TextView mCountryLabelTv;
    @BindView(R.id.release_date_tv)
    TextView mReleaseDateTv;
    @BindView(R.id.release_date_label_tv)
    TextView mReleaseDateLabelTv;
    @BindView(R.id.genre_tv)
    TextView mGenreTv;
    @BindView(R.id.genre_label_tv)
    TextView mGenreLabelTv;
    @BindView(R.id.audio_tv)
    TextView mAudioLanguageTv;
    @BindView(R.id.audio_label_tv)
    TextView mAudioLanguageLabelTv;
    @BindView(R.id.vote_tv)
    TextView mRatingTv;
    @BindView(R.id.vote_label_tv)
    TextView mRatingLabelTv;
    @BindView(R.id.budget_tv)
    TextView mBudgetTv;
    @BindView(R.id.budget_label_tv)
    TextView mBudgetLabelTv;
    @BindView(R.id.revenue_tv)
    TextView mRevenueTv;
    @BindView(R.id.revenue_label_tv)
    TextView mRevenueLabelTv;
    @BindView(R.id.homepage_tv)
    TextView mHomepageTv;
    @BindView(R.id.homepage_label_tv)
    TextView mHomepageLabelTv;
    @BindView(R.id.movie_description)
    TextView mDescriptionTv;
    @BindView(R.id.user_reviews_label)
    TextView mReviewsLabelTv;
    @BindView(R.id.trailers_label_tv)
    TextView mTrailersLabel;
    @BindView(R.id.reviews_container)
    LinearLayout mReviewsContainer;
    @BindView(R.id.trailer_container)
    LinearLayout mTrailerContainer;

    @BindView(R.id.favorite_btn)
    Button mFavoriteBtn;

    @OnClick(R.id.retry_btn)
    void onRetryBtnClick() {
        mConnectionErrorLayout.setVisibility(View.GONE);
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
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
        detailsViewModel.getMovieDetailsById(mMovieId, false);
        detailsViewModel.getMovieVideosById(mMovieId, false);
        detailsViewModel.getMovieReviewsById(mMovieId, false);
        detailsViewModel.movieDetailsData.getData().observe(this, this::showAllMovieDetails);
        detailsViewModel.movieDetailsData.getError().observe(this, throwable -> mConnectionErrorLayout.setVisibility(View.VISIBLE));
        detailsViewModel.movieDetailsData.getLoading().observe(this, this::showProgressBar);
        detailsViewModel.movieVideosData.getData().observe(this, movieVideosResponse -> {
            if (movieVideosResponse != null) {
                prepareTrailers(movieVideosResponse);
            }
        });
        detailsViewModel.movieReviewsData.getData().observe(this, movieReviewsResponse -> {
            if (movieReviewsResponse != null) {
                prepareReviews(movieReviewsResponse);
            }
        });
        prepareFavoriteBtn();
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
            mTitleTv.setText(movieDetails.getOriginalTitle());
        }
        //Duration
        if (movieDetails.getRuntime() != null && movieDetails.getRuntime() > 0) {
            mDurationTv.setVisibility(View.VISIBLE);
            mDurationLabelTv.setVisibility(View.VISIBLE);
            mDurationTv.setText(TimeConverter.convertMinToHHMM(movieDetails.getRuntime()));
        }

        //Countries
        if (movieDetails.getProductionCountries() != null) {
            ArrayList<String> countryNames = new ArrayList<>();
            for (ProductionCountriesItem country : movieDetails.getProductionCountries()) {
                if (country.getName() != null)
                    countryNames.add(country.getName());
            }
            if (!countryNames.isEmpty()) {
                mCountryTv.setVisibility(View.VISIBLE);
                mCountryLabelTv.setVisibility(View.VISIBLE);
                mCountryTv.setText(StringUtils.buildStringWithComaSeparator(countryNames));
            }
        }
        //Release Date
        if (movieDetails.getReleaseDate() != null) {
            mReleaseDateTv.setVisibility(View.VISIBLE);
            mReleaseDateLabelTv.setVisibility(View.VISIBLE);
            mReleaseDateTv.setText(movieDetails.getReleaseDate());
        }

        //Genre
        if (movieDetails.getGenres() != null) {
            ArrayList<String> genreNames = new ArrayList<>();
            for (GenresItem genre : movieDetails.getGenres()) {
                if (genre.getName() != null)
                    genreNames.add(genre.getName());
            }
            if (!genreNames.isEmpty()) {
                mGenreTv.setVisibility(View.VISIBLE);
                mGenreLabelTv.setVisibility(View.VISIBLE);
                mGenreTv.setText(StringUtils.buildStringWithComaSeparator(genreNames));
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
                mAudioLanguageTv.setVisibility(View.VISIBLE);
                mAudioLanguageLabelTv.setVisibility(View.VISIBLE);
                mAudioLanguageTv.setText(StringUtils.buildStringWithComaSeparator(languagesNames));
            }
        }
        //Rating
        if (movieDetails.getVoteAverage() > 0.0) {
            mRatingTv.setVisibility(View.VISIBLE);
            mRatingLabelTv.setVisibility(View.VISIBLE);
            mRatingTv.setText(String.valueOf(movieDetails.getVoteAverage()));
        }

        //Budget
        if (movieDetails.getBudget() != null && movieDetails.getBudget() > 0) {
            mBudgetTv.setVisibility(View.VISIBLE);
            mBudgetLabelTv.setVisibility(View.VISIBLE);
            mBudgetTv.setText(String.format(Locale.ENGLISH, "%,d$", movieDetails.getBudget()).replace(',', ' '));
        }

        //Revenue
        if (movieDetails.getRevenue() != null && movieDetails.getRevenue() > 0) {
            mRevenueTv.setVisibility(View.VISIBLE);
            mRevenueLabelTv.setVisibility(View.VISIBLE);
            mRevenueTv.setText(String.format(Locale.ENGLISH, "%,d$", movieDetails.getRevenue()).replace(',', ' '));
        }

        //Homepage
        if (movieDetails.getHomepage() != null) {
            mHomepageTv.setVisibility(View.VISIBLE);
            mHomepageLabelTv.setVisibility(View.VISIBLE);
            mHomepageTv.setText(movieDetails.getHomepage());
        }

        //Description
        if (movieDetails.getOverview() != null) {
            mDescriptionTv.setText(movieDetails.getOverview());
        }

    }

    private void showProgressBar(Boolean isShow) {
        if (isShow != null && isShow) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void prepareFavoriteBtn() {
        App.dbRepo.loadFavoriteMovieById(mMovieId).observe(this, favoriteMovieEntry -> {
            if (favoriteMovieEntry == null) {
                mFavoriteBtn.setText(R.string.add_to_favorite);
                mFavoriteBtn.setOnClickListener(v -> {
                    addToFavorite();
                });
            } else {
                mFavoriteBtn.setText(R.string.delete_from_favorite);
                mFavoriteBtn.setOnClickListener(v -> {
                    deleteFromFavorite();
                });
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
            Picasso.get().load(PICTURE_HUGE_BASE_URL + posterPath).into(mPosterIv);
    }

    private void prepareReviews(MovieReviewsResponse movieDetails) {
        mReviewsContainer.removeAllViews();
        if (movieDetails.getResults() != null && !movieDetails.getResults().isEmpty()) {
            mReviewsLabelTv.setVisibility(View.VISIBLE);
            for (ReviewsResultsItem reviewItem : movieDetails.getResults()) {
                View reviewView = createReviewView(reviewItem);
                if (reviewView != null)
                    mReviewsContainer.addView(reviewView);
            }
        }
    }

    private void prepareTrailers(MovieVideosResponse movieVideos) {
        mTrailerContainer.removeAllViews();
        if (movieVideos.getResults() != null && !movieVideos.getResults().isEmpty()) {
            mTrailersLabel.setVisibility(View.VISIBLE);
            for (TrailersResultsItem trailerItem : movieVideos.getResults()) {
                View trailerView = createTrailerView(trailerItem);
                if (trailerView != null)
                    mTrailerContainer.addView(trailerView);
            }
        }
    }

    private View createReviewView(ReviewsResultsItem resultItem) {
        if (getActivity() != null && resultItem.getId() != null) {
            LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
            View reviewView = li.inflate(R.layout.item_view_review, null);
            TextView authorName = reviewView.findViewById(R.id.author_name_tv);
            TextView content = reviewView.findViewById(R.id.content_tv);
            String authorInfo = getString(R.string.movie_users_reviews_author) + resultItem.getAuthor();
            authorName.setText(authorInfo);
            content.setText(Html.fromHtml(resultItem.getContent()));
            return reviewView;
        }
        return null;
    }

    private View createTrailerView(TrailersResultsItem trailerInfo) {
        if (getActivity() != null && trailerInfo.getId() != null) {
            LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
            View trailerView = li.inflate(R.layout.item_view_trailer, null);
            ConstraintLayout containerItem = trailerView.findViewById(R.id.containerItem);
            TextView trailerTitle = trailerView.findViewById(R.id.trailerTitle);
            String trailerTitleStr = trailerInfo.getSite() + ":" + trailerInfo.getName();
            trailerTitle.setText(trailerTitleStr);
            containerItem.setOnClickListener(v -> {
                YoutubeUtil.watchYoutubeVideo(getContext(), trailerInfo.getKey());
            });
            return trailerView;
        }
        return null;
    }

}
