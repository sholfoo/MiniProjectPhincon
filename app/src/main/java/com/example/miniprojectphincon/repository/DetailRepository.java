package com.example.miniprojectphincon.repository;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.miniprojectphincon.PokeApp;
import com.example.miniprojectphincon.database.PokemonInfoDAO;
import com.example.miniprojectphincon.model.pokemoninfo.PokemonInfoAPI;
import com.example.miniprojectphincon.model.pokemoninfo.TypesResponse;
import com.example.miniprojectphincon.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailRepository {

    @Inject ApiService apiService;
    @Inject PokemonInfoDAO pokemonInfoDAO;
    private final List<TypesResponse> typesData = new ArrayList<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final static MutableLiveData<PokemonInfoAPI> pokemonInfoLiveData = new MutableLiveData<>();
    private final static MutableLiveData<Boolean> progressBarLiveData = new MutableLiveData<>();
    private final static MutableLiveData<String> toastLiveData = new MutableLiveData<>();

    public DetailRepository() {
        PokeApp.getmComponent().inject(this);
    }

    public void fetchPokemonInfo(String namePoke) {
        progressBarLiveData.setValue(true);

        Observable<PokemonInfoAPI> pokemonInfoAPIObservable = apiService.fetchPokemonInfo(namePoke);
        DisposableObserver<PokemonInfoAPI> pokemonInfoAPIObserver = getPokemonInfoAPIObserver(namePoke);

        Disposable disposableFetchData = pokemonInfoAPIObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(pokemonInfoAPIObserver);

        compositeDisposable.add(disposableFetchData);
    }

    private DisposableObserver<PokemonInfoAPI> getPokemonInfoAPIObserver(String namePoke) {
        return new DisposableObserver<PokemonInfoAPI>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onNext(@NonNull PokemonInfoAPI pokemonInfoAPI) {
                onResponseSuccess(pokemonInfoAPI, namePoke);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                onResponseFail(e, namePoke);
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onResponseSuccess(PokemonInfoAPI pokemonInfoAPI, String namePoke) {
        //Get these info: weight, height
        @SuppressLint("DefaultLocale") String heightFormatted = String.format("%.1f M", (float) pokemonInfoAPI.getHeight() / 10);
        @SuppressLint("DefaultLocale") String weightFormatted = String.format("%.1f KG", (float) pokemonInfoAPI.getWeight() / 10);

        //Get name of types Pokemon and Color Types
        List<TypesResponse> typesList = pokemonInfoAPI.getTypes();

        for (int i = 0; i < typesList.size(); i++) {
            TypesResponse type = typesList.get(i);

            String nameType = type.getType().getName();

            typesData.add(new TypesResponse(nameType));
        }

        PokemonInfoAPI pokemonInfo = new PokemonInfoAPI(namePoke, typesData, heightFormatted, weightFormatted);

        progressBarLiveData.setValue(false);
        pokemonInfoLiveData.setValue(pokemonInfo);
        onInsertPokemonInfoIntoDatabase(pokemonInfo);
    }

    private void onResponseFail(Throwable e, String namePoke) {
        progressBarLiveData.setValue(false);

        if (pokemonInfoDAO.getPokemonInfo(namePoke) == null) {
            toastLiveData.setValue(e.getMessage());
        } else {
            pokemonInfoLiveData.setValue(pokemonInfoDAO.getPokemonInfo(namePoke));
            toastLiveData.setValue("");
        }
    }

    private void onInsertPokemonInfoIntoDatabase(PokemonInfoAPI pokemonInfo) {
        Observable<PokemonInfoAPI> observable = Observable.just(pokemonInfo);

        Disposable disposableInsertData = observable
                .doOnNext(pokemonInfoAPI -> pokemonInfoDAO.insertPokemonInfo(pokemonInfoAPI))
                .subscribeOn(Schedulers.io())
                .subscribe();

        compositeDisposable.add(disposableInsertData);
    }

    public static LiveData<PokemonInfoAPI> getPokemonInfoLiveData() {
        return pokemonInfoLiveData;
    }

    public static LiveData<Boolean> getProgressBarLiveData() {
        return progressBarLiveData;
    }

    public static LiveData<String> getToastLiveData() {
        return toastLiveData;
    }

    public void resetValuesLiveData() {
        pokemonInfoLiveData.postValue(null);
        progressBarLiveData.postValue(null);
        toastLiveData.postValue(null);
    }

    public void getDisposableToUnsubscribe() {
        compositeDisposable.dispose();
    }
}
