package com.add.yassin.popularmoviespart1.data;

import com.add.yassin.popularmoviespart1.data.model.RepoMovieDetailsResult;
import com.add.yassin.popularmoviespart1.data.model.RepoMoviesResult;
import com.add.yassin.popularmoviespart1.ui.movieslist.MoviesFilterType;

public interface DataSource {

    RepoMovieDetailsResult getMovie(long movieId);

    RepoMoviesResult getFilteredMoviesBy(MoviesFilterType sortBy);
}
