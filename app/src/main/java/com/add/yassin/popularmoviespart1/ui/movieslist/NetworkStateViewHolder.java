package com.add.yassin.popularmoviespart1.ui.movieslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.add.yassin.popularmoviespart1.data.api.NetworkState;
import com.add.yassin.popularmoviespart1.databinding.ItemNetworkStateBinding;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.add.yassin.popularmoviespart1.data.api.Status.FAILED;
import static com.add.yassin.popularmoviespart1.data.api.Status.RUNNING;


public class NetworkStateViewHolder extends RecyclerView.ViewHolder {

    private ItemNetworkStateBinding pindding;

    public NetworkStateViewHolder(@NonNull ItemNetworkStateBinding binding,
                                  final MoviesViewModel viewModel) {
        super(binding.getRoot());
        this.pindding = binding;

        binding.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.retry();
            }
        });
    }

    static NetworkStateViewHolder create(ViewGroup parent, MoviesViewModel viewModel) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemNetworkStateBinding binding =
                ItemNetworkStateBinding.inflate(layoutInflater, parent, false);
        return new NetworkStateViewHolder(binding, viewModel);
    }

    void bindTo(NetworkState networkState) {
        pindding.progressBar.setVisibility(
                isVisible(networkState.getStatus() == RUNNING));
        pindding.retryButton.setVisibility(
                isVisible(networkState.getStatus() == FAILED));
        pindding.errorMsg.setVisibility(
                isVisible(networkState.getMsg() != null));
        pindding.errorMsg.setText(networkState.getMsg());
    }

    private int isVisible(boolean condition) {
        if (condition)
            return View.VISIBLE;
        else
            return View.GONE;
    }
}
