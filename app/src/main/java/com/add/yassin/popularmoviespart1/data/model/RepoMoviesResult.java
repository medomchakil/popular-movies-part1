package com.add.yassin.popularmoviespart1.data.model;

import com.add.yassin.popularmoviespart1.data.paging.MoviePageKeyedDataSource;
import com.add.yassin.popularmoviespart1.data.api.NetworkState;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;


public class RepoMoviesResult {
    public LiveData<PagedList<Movie>> data;
    public LiveData<NetworkState> networkState;
    public MutableLiveData<MoviePageKeyedDataSource> sourceLiveData;

    public RepoMoviesResult(LiveData<PagedList<Movie>> data, LiveData<NetworkState> networkState,
                            MutableLiveData<MoviePageKeyedDataSource> sourceLiveData) {
        this.data = data;
        this.networkState = networkState;
        this.sourceLiveData = sourceLiveData;
    }
}
