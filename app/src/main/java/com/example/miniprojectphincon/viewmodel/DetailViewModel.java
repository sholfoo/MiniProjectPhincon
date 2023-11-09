package com.example.miniprojectphincon.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.miniprojectphincon.model.pokemoninfo.PokemonInfoAPI;
import com.example.miniprojectphincon.repository.DetailRepository;

public class DetailViewModel extends ViewModel {

    private final LiveData<PokemonInfoAPI> pokemonInfoLiveData;

    private final LiveData<Boolean> progressBarLiveData;

    private final LiveData<String> toastLiveData;

    public DetailViewModel() {
        pokemonInfoLiveData = DetailRepository.getPokemonInfoLiveData();
        progressBarLiveData = DetailRepository.getProgressBarLiveData();
        toastLiveData = DetailRepository.getToastLiveData();
    }

    public LiveData<PokemonInfoAPI> getPokemonInfoLiveData() {
        return pokemonInfoLiveData;
    }

    public LiveData<Boolean> getProgressBarLiveData() {
        return progressBarLiveData;
    }

    public LiveData<String> getToastLiveData() {
        return toastLiveData;
    }
}
