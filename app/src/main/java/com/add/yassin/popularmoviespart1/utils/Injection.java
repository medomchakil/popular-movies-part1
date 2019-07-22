package com.add.yassin.popularmoviespart1.utils;

import com.add.yassin.popularmoviespart1.data.MovieRepository;
import com.add.yassin.popularmoviespart1.data.api.ApiClient;

public class Injection {
    public static MovieRepository provideMovieRepository() {
        return new MovieRepository(ApiClient.getInstance(), AppExecutors.getInstance());
    }
}
