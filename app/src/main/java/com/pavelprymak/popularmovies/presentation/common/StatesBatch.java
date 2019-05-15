package com.pavelprymak.popularmovies.presentation.common;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class StatesBatch<T> {
    private LiveData<T> data = new MutableLiveData<>();
    private LiveData<Throwable> error = new ActionLiveData<>();
    private LiveData<Boolean> loading = new MutableLiveData<>();

    public void postLoading(boolean isLoading) {
        ((MutableLiveData<Boolean>) loading).postValue(isLoading);
    }

    public void postValue(T t) {
        postLoading(false);
        ((MutableLiveData<T>) data).postValue(t);
    }

    public void postError(Throwable throwable) {
        postLoading(false);
        ((ActionLiveData<Throwable>) error).postValue(throwable);
    }

    public LiveData<T> getData() {
        return data;
    }

    public LiveData<Throwable> getError() {
        return error;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void removeObservers(LifecycleOwner owner) {
        data.removeObservers(owner);
        error.removeObservers(owner);
        loading.removeObservers(owner);
    }
}
