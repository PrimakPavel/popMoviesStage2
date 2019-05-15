package com.pavelprymak.popularmovies.presentation.screens;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.flexbox.FlexboxLayout;
import com.pavelprymak.popularmovies.R;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.GenresItem;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.MovieDetailsResponse;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.ProductionCompaniesItem;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.ProductionCountriesItem;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.SpokenLanguagesItem;
import com.pavelprymak.popularmovies.presentation.common.BaseFragment;
import com.pavelprymak.popularmovies.presentation.viewModels.MovieDetailsViewModel;
import com.pavelprymak.popularmovies.utils.StringUtils;
import com.pavelprymak.popularmovies.utils.TimeConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pavelprymak.popularmovies.network.Constants.PICTURE_HUGE_BASE_URL;
import static com.pavelprymak.popularmovies.network.Constants.PICTURE_SMALL_BASE_URL;


public class MovieDetailsFragment extends BaseFragment {

    static final String ARG_MOVIE_ID = "argMovieId";
    private int mMovieId;
    private MovieDetailsViewModel detailsViewModel;
    @BindView(R.id.movie_poster_iv)
    ImageView mPosterIv;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.image_load_error_iv)
    ImageView mErrorLoadIv;
    @BindView(R.id.error_layout)
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
    @BindView(R.id.prod_companies_tv)
    TextView mCompaniesLabelTv;
    @BindView(R.id.companies_container)
    FlexboxLayout mCompaniesContainer;

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
        detailsViewModel.movieDetailsData.getData().observe(this, this::showAllMovieDetails);
        detailsViewModel.movieDetailsData.getError().observe(this, throwable -> mConnectionErrorLayout.setVisibility(View.VISIBLE));
        detailsViewModel.movieDetailsData.getLoading().observe(this, this::showProgressBar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detailsViewModel.movieDetailsData.removeObservers(this);
    }

    private void showAllMovieDetails(MovieDetailsResponse movieDetails) {
        showMoviePoster(movieDetails.getPosterPath());
        //Title
        if (movieDetails.getOriginalTitle() != null) {
            mTitleTv.setText(movieDetails.getOriginalTitle());
        }
        //Duration
        if (movieDetails.getRuntime() != null) {
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
        if (movieDetails.getBudget() != null) {
            mBudgetTv.setVisibility(View.VISIBLE);
            mBudgetLabelTv.setVisibility(View.VISIBLE);
            mBudgetTv.setText(String.format(Locale.ENGLISH, "%,d$", movieDetails.getBudget()).replace(',', ' '));
        }

        //Revenue
        if (movieDetails.getRevenue() != null) {
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
        //Companies
        mCompaniesContainer.removeAllViews();
        if (movieDetails.getProductionCompanies() != null && !movieDetails.getProductionCompanies().isEmpty()) {
            mCompaniesLabelTv.setVisibility(View.VISIBLE);
            for (ProductionCompaniesItem companiesItem : movieDetails.getProductionCompanies()) {
                View companyView = createCompanyView(companiesItem);
                if (companyView != null)
                    mCompaniesContainer.addView(companyView);

            }
        }

    }

    private void showProgressBar(Boolean isShow) {
        if (isShow != null && isShow) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void showMoviePoster(String posterPath) {
        if (posterPath != null)
            Picasso.get().load(PICTURE_HUGE_BASE_URL + posterPath).into(mPosterIv);
    }

    private void showCompanyImage(String posterPath, ImageView imageView) {
        if (posterPath != null)
            Picasso.get().load(PICTURE_SMALL_BASE_URL + posterPath).into(imageView);
    }

    private View createCompanyView(ProductionCompaniesItem company) {
        if (getActivity() != null && company.getLogoPath() != null) {
            LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
            View companyView = li.inflate(R.layout.item_view_company, null);
            ImageView companyImage = companyView.findViewById(R.id.image_company);
            showCompanyImage(company.getLogoPath(), companyImage);
            return companyView;
        }
        return null;
    }

}
