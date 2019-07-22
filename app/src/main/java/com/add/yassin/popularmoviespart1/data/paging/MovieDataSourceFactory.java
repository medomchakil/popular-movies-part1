package com.add.yassin.popularmoviespart1.data.paging;

import com.add.yassin.popularmoviespart1.data.api.MovieApiService;
import com.add.yassin.popularmoviespart1.data.model.Movie;
import com.add.yassin.popularmoviespart1.ui.movieslist.MoviesFilterType;

import java.util.concurrent.Executor;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;


public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    public MutableLiveData<MoviePageKeyedDataSource> sourceLiveData = new MutableLiveData<>();

    private final MovieApiService movieApiService;
    private final Executor networkExecutor;
    private final MoviesFilterType sortBy;

    public MovieDataSourceFactory(MovieApiService movieApiService,
                                  Executor networkExecutor, MoviesFilterType sortBy) {
        this.movieApiService = movieApiService;
        this.sortBy = sortBy;
        this.networkExecutor = networkExecutor;
    }

    @Override
    public DataSource<Integer, Movie> create() {
        MoviePageKeyedDataSource movieDataSource =
                new MoviePageKeyedDataSource(movieApiService, networkExecutor, sortBy);
        sourceLiveData.postValue(movieDataSource);
        return movieDataSource;
    }
}
