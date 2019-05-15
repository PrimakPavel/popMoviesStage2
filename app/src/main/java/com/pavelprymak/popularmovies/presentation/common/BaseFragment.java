package com.pavelprymak.popularmovies.presentation.common;

import androidx.fragment.app.Fragment;

import butterknife.Unbinder;

public class BaseFragment extends Fragment {
    protected Unbinder mUnbinder;

    @Override
    public void onDestroyView() {
        if (this.mUnbinder != null) {
            this.mUnbinder.unbind();
        }

        super.onDestroyView();
    }
}
