package com.add.yassin.popularmoviespart1.ui.moviedetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.add.yassin.popularmoviespart1.R;
import com.add.yassin.popularmoviespart1.data.api.NetworkState;
import com.add.yassin.popularmoviespart1.data.model.Movie;
import com.add.yassin.popularmoviespart1.databinding.ActivityDetailsBinding;
import com.add.yassin.popularmoviespart1.utils.Constants;
import com.add.yassin.popularmoviespart1.utils.GlideApp;
import com.add.yassin.popularmoviespart1.utils.Injection;
import com.add.yassin.popularmoviespart1.utils.ViewModelFactory;
import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static com.add.yassin.popularmoviespart1.data.api.Status.FAILED;
import static com.add.yassin.popularmoviespart1.data.api.Status.RUNNING;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";

    private static final int DEFAULT_ID = -1;

    private ActivityDetailsBinding mBinding;

    private MovieDetailsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        final long movieId = intent.getLongExtra(EXTRA_MOVIE_ID, DEFAULT_ID);
        if (movieId == DEFAULT_ID) {
            closeOnError();
        }

        setupToolbar();
        mViewModel = obtainViewModel();
        if (savedInstanceState == null) {
            mViewModel.setMovieId(movieId);
        }
        mViewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                handleNetworkState(networkState);
            }
        });
        mViewModel.getMovieLiveData().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                updateUi(movie);
            }
        });
        mBinding.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.retry(movieId);
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = mBinding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            handleCollapsedToolbarTitle();
        }
    }

    private void updateUi(Movie movie) {
        GlideApp.with(this)
                .load(Constants.BACKDROP_URL + movie.getBackdrop())
                .into(mBinding.imageMovieBackdrop);
        GlideApp.with(this)
                .load(Constants.IMAGE_URL + movie.getImageUrl())
                .into(mBinding.imagePoster);
        mBinding.textTitle.setText(movie.getTitle());
        mBinding.textReleaseDate.setText(movie.getReleaseDate());
        mBinding.textVote.setText(String.valueOf(movie.getUserRating()));
        mBinding.textOverview.setText(movie.getOverview());
        mBinding.executePendingBindings();
    }

    private MovieDetailsViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance(Injection.provideMovieRepository());
        return ViewModelProviders.of(this, factory).get(MovieDetailsViewModel.class);
    }

    private void closeOnError() {
        throw new RuntimeException("Access denied.");
    }

    private void handleCollapsedToolbarTitle() {
        mBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mBinding.collapsingToolbar.setTitle(
                            mViewModel.getMovieLiveData().getValue().getTitle());
                    isShow = true;
                } else if (isShow) {
                    mBinding.collapsingToolbar.setTitle(" ");
                    isShow = false;
                }

            }
        });
    }

    private void handleNetworkState(NetworkState networkState) {
        boolean isLoaded = networkState == NetworkState.LOADED;
        mBinding.appbar.setVisibility(isVisible(isLoaded));
        mBinding.movieDetails.setVisibility(isVisible(isLoaded));
        mBinding.progressBar.setVisibility(
                isVisible(networkState.getStatus() == RUNNING));
        mBinding.retryButton.setVisibility(
                isVisible(networkState.getStatus() == FAILED));
        mBinding.errorMsg.setVisibility(
                isVisible(networkState.getMsg() != null));
        mBinding.errorMsg.setText(networkState.getMsg());
    }

    private int isVisible(boolean condition) {
        if (condition)
            return View.VISIBLE;
        else
            return View.GONE;
    }

}
