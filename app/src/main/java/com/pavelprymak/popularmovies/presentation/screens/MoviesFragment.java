package com.pavelprymak.popularmovies.presentation.screens;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.popularmovies.R;
import com.pavelprymak.popularmovies.presentation.adapter.MovieAdapter;
import com.pavelprymak.popularmovies.presentation.adapter.MoviesListItemClickListener;
import com.pavelprymak.popularmovies.presentation.common.BaseFragment;
import com.pavelprymak.popularmovies.presentation.viewModels.MoviesListViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pavelprymak.popularmovies.presentation.paging.MoviesDataSource.POPULAR_MOVIES;
import static com.pavelprymak.popularmovies.presentation.paging.MoviesDataSource.TOP_RATED_MOVIES;
import static com.pavelprymak.popularmovies.presentation.screens.MovieDetailsFragment.ARG_MOVIE_ID;


public class MoviesFragment extends BaseFragment implements MoviesListItemClickListener {
    private NavController mNavController;
    private Menu mMenu;

    private MovieAdapter mAdapter;
    private MoviesListViewModel mMoviesViewModel;

    @BindView(R.id.recycler_view_movies)
    RecyclerView mMoviesRecycler;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    @BindView(R.id.error_layout)
    LinearLayout mConnectionErrorLayout;

    @OnClick(R.id.retry_btn)
    void onRetryBtnClick() {
        mConnectionErrorLayout.setVisibility(View.GONE);
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
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        mMoviesViewModel = ViewModelProviders.of(this).get(MoviesListViewModel.class);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);

        initMoviesRecyclerView();
        mMoviesViewModel.getMoviesData().observe(this, resultsItems -> mAdapter.submitList(resultsItems));
        mMoviesViewModel.getLoadingData().observe(this, this::showProgressBar);
        mMoviesViewModel.getErrorData().observe(this, throwable -> {
            mAdapter.submitList(null);
            mConnectionErrorLayout.setVisibility(View.VISIBLE);
        });
        mMoviesViewModel.prepareMoviePagedList();
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
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void initMoviesRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.list_colons_num), RecyclerView.VERTICAL, false);
        mMoviesRecycler.setLayoutManager(layoutManager);
        mAdapter = new MovieAdapter(this);
        mMoviesRecycler.setAdapter(mAdapter);
    }

    private void uncheckAllMenuItems() {
        if (mMenu != null) {
            for (int i = 0; i < mMenu.size(); i++) {
                mMenu.getItem(i).setChecked(false);
            }
        }
    }
}
