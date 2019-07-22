package com.add.yassin.popularmoviespart1.data;

import com.add.yassin.popularmoviespart1.data.api.MovieApiService;
import com.add.yassin.popularmoviespart1.data.api.NetworkState;
import com.add.yassin.popularmoviespart1.data.model.Movie;
import com.add.yassin.popularmoviespart1.data.model.RepoMovieDetailsResult;
import com.add.yassin.popularmoviespart1.data.model.RepoMoviesResult;
import com.add.yassin.popularmoviespart1.data.paging.MovieDataSourceFactory;
import com.add.yassin.popularmoviespart1.data.paging.MoviePageKeyedDataSource;
import com.add.yassin.popularmoviespart1.ui.movieslist.MoviesFilterType;
import com.add.yassin.popularmoviespart1.utils.AppExecutors;

import java.io.IOException;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import retrofit2.Response;


public class MovieRepository implements DataSource {

    private static final int PAGE_SIZE = 21;
    private final MovieApiService mMovieApiService;
    private final AppExecutors mExecutors;
    public MovieRepository(MovieApiService movieApiService,
                           AppExecutors executors) {
        mMovieApiService = movieApiService;
        mExecutors = executors;
    }

    @Override
    public RepoMovieDetailsResult getMovie(final long movieId) {
        final MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
        final MutableLiveData<Movie> movieLiveData = new MutableLiveData<>();
        networkState.setValue(NetworkState.LOADING);
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Movie> response = mMovieApiService.getMovieDetails(movieId).execute();
                    networkState.postValue(NetworkState.LOADED);
                    movieLiveData.postValue(response.body());
                } catch (IOException e) {
                    NetworkState error = NetworkState.error(e.getMessage());
                    networkState.postValue(error);
                }
            }
        });
        return new RepoMovieDetailsResult(movieLiveData, networkState);
    }

    @Override
    public RepoMoviesResult getFilteredMoviesBy(MoviesFilterType sortBy) {
        MovieDataSourceFactory sourceFactory =
                new MovieDataSourceFactory(mMovieApiService, mExecutors.networkIO(), sortBy);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build();

        LiveData<PagedList<Movie>> moviesPagedList = new LivePagedListBuilder<>(sourceFactory, config)
                .setFetchExecutor(mExecutors.networkIO())
                .build();

        LiveData<NetworkState> networkState = Transformations.switchMap(sourceFactory.sourceLiveData,
                new Function<MoviePageKeyedDataSource, LiveData<NetworkState>>() {
                    @Override
                    public LiveData<NetworkState> apply(MoviePageKeyedDataSource input) {
                        return input.networkState;
                    }
                });

        return new RepoMoviesResult(
                moviesPagedList,
                networkState,
                sourceFactory.sourceLiveData
        );
    }
}
