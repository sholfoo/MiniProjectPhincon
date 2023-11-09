package com.example.miniprojectphincon.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.miniprojectphincon.model.pokemonfavlist.PokemonFavListAPI;
import com.example.miniprojectphincon.repository.FavoriteRepository;

import java.util.List;

public class FavoriteViewModel extends ViewModel {

    private final LiveData<List<PokemonFavListAPI>> pokemonFavoriteListLiveData;

    private final LiveData<Boolean> progressBarLiveData;

    private final LiveData<Boolean> swipeRefreshLayoutLiveData;

    private final LiveData<String> toastLiveData;

    public FavoriteViewModel() {
        this.pokemonFavoriteListLiveData = FavoriteRepository.getPokemonListLiveData();
        this.progressBarLiveData = FavoriteRepository.getProgressBarLiveData();
        this.swipeRefreshLayoutLiveData = FavoriteRepository.getSwipeRefreshLayoutLiveData();
        this.toastLiveData = FavoriteRepository.getToastLiveData();
    }

    public LiveData<List<PokemonFavListAPI>> getPokemonFavoriteListLiveData() {
        return pokemonFavoriteListLiveData;
    }

    public LiveData<Boolean> getProgressBarLiveData() {
        return progressBarLiveData;
    }

    public LiveData<Boolean> getSwipeRefreshLayoutLiveData() {
        return swipeRefreshLayoutLiveData;
    }

    public LiveData<String> getToastLiveData() {
        return toastLiveData;
    }
}
