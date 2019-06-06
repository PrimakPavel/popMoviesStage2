package com.pavelprymak.popularmovies.presentation.screens;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.popularmovies.R;
import com.pavelprymak.popularmovies.databinding.FragmentMoviesBinding;
import com.pavelprymak.popularmovies.network.Constants;
import com.pavelprymak.popularmovies.presentation.adapter.MovieAdapter;
import com.pavelprymak.popularmovies.presentation.adapter.MoviesListItemClickListener;
import com.pavelprymak.popularmovies.presentation.viewModels.MoviesListViewModel;

import static com.pavelprymak.popularmovies.presentation.paging.MoviesDataSource.FAVORITE_MOVIES;
import static com.pavelprymak.popularmovies.presentation.paging.MoviesDataSource.POPULAR_MOVIES;
import static com.pavelprymak.popularmovies.presentation.paging.MoviesDataSource.TOP_RATED_MOVIES;
import static com.pavelprymak.popularmovies.presentation.screens.MovieDetailsFragment.ARG_MOVIE_ID;


public class MoviesFragment extends Fragment implements MoviesListItemClickListener {
    private NavController mNavController;
    private Menu mMenu;
    private FragmentMoviesBinding mBinding;

    private MovieAdapter mAdapter;
    private MoviesListViewModel mMoviesViewModel;

    private void onRetryBtnClick() {
        mMoviesViewModel.cleanData();
        mMoviesViewModel.prepareMoviePagedList();
    }

    @Override
    public void onMovieItemClick(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_MOVIE_ID, id);
        mNavController.navigate(R.id.movieDetailsFragment, bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        uncheckAllMenuItems();
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.popular_action:
                setMovieFilterType(POPULAR_MOVIES);
                return true;
            case R.id.top_rated_action:
                setMovieFilterType(TOP_RATED_MOVIES);
                return true;
            case R.id.favorite_action:
                setMovieFilterType(FAVORITE_MOVIES);
                return true;
        }
        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        mMenu = menu;
        switch (mMoviesViewModel.getSelectedFilterType()) {
            case POPULAR_MOVIES:
                mMenu.findItem(R.id.popular_action).setChecked(true);
                break;
            case TOP_RATED_MOVIES:
                mMenu.findItem(R.id.top_rated_action).setChecked(true);
                break;
            case FAVORITE_MOVIES:
                mMenu.findItem(R.id.favorite_action).setChecked(true);
                break;
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false);
        mMoviesViewModel = ViewModelProviders.of(this).get(MoviesListViewModel.class);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        initMoviesRecyclerView();
        mMoviesViewModel.getMoviesData().observe(this, resultsItems -> {
            mAdapter = new MovieAdapter(this);
            mBinding.recyclerMovies.setAdapter(mAdapter);
            mAdapter.submitList(resultsItems);
            if (resultsItems != null) {
                mBinding.errorLayout.setVisibility(View.GONE);
            }
        });
        mMoviesViewModel.getLoadingData().observe(this, this::showProgressBar);
        mMoviesViewModel.getErrorData().observe(this, throwable -> {
            mAdapter.submitList(null);
            if (throwable.getMessage().equals(Constants.HttpErrorCodes.HTTP_AUTH_ERROR)
                    || throwable.getMessage().equals(Constants.HttpErrorCodes.HTTP_NOT_FOUND_ERROR)
                    || throwable.getMessage().equals(Constants.HttpErrorCodes.CONNECTION_ERROR)
            ) {
                showError(R.string.auth_or_not_found_http_error, true);
            } else if (throwable.getMessage().equals(Constants.DbErrorCodes.DB_EMPTY_ERROR)) {
                showError(R.string.empty_favorite_movies_list_error, false);
            }
        });
        mMoviesViewModel.prepareMoviePagedList();
        mBinding.retryBtn.setOnClickListener(v -> onRetryBtnClick());
    }

    private void showError(int messageResId, boolean isShowRetryBtn) {
        mBinding.tvErrorMessage.setText(messageResId);
        if (isShowRetryBtn) {
            mBinding.retryBtn.setVisibility(View.VISIBLE);
        } else {
            mBinding.retryBtn.setVisibility(View.GONE);
        }
        Toast.makeText(getContext(), messageResId, Toast.LENGTH_LONG).show();
        mBinding.errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMoviesViewModel.getMoviesData().removeObservers(this);
        mMoviesViewModel.getLoadingData().removeObservers(this);
        mMoviesViewModel.getErrorData().removeObservers(this);
    }

    private void setMovieFilterType(int movieFilterType) {
        mMoviesViewModel.changeMoviePagedListType(movieFilterType);
    }

    private void showProgressBar(Boolean isShow) {
        if (isShow != null && isShow) {
            mBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
        } else {
            mBinding.pbLoadingIndicator.setVisibility(View.GONE);
        }
    }

    private void initMoviesRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.list_colons_num), RecyclerView.VERTICAL, false);
        mBinding.recyclerMovies.setLayoutManager(layoutManager);
    }

    private void uncheckAllMenuItems() {
        if (mMenu != null) {
            for (int i = 0; i < mMenu.size(); i++) {
                mMenu.getItem(i).setChecked(false);
            }
        }
    }
}
