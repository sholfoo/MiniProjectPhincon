package com.example.miniprojectphincon.repository;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.miniprojectphincon.PokeApp;
import com.example.miniprojectphincon.database.FavPokemonListDAO;
import com.example.miniprojectphincon.model.pokemonfavlist.PokemonFavListAPI;
import com.example.miniprojectphincon.network.ApiService;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteRepository {

    @Inject FavPokemonListDAO favPokemonListDAO;
    @Inject ApiService apiService;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final static MutableLiveData<List<PokemonFavListAPI>> pokemonFavoriteLiveData = new MutableLiveData<>();
    private final static MutableLiveData<Boolean> swipeRefreshLayoutLiveData = new MutableLiveData<>();
    private final static MutableLiveData<Boolean> progressBarLiveData = new MutableLiveData<>();
    private final static MutableLiveData<String> toastLiveData = new MutableLiveData<>();

    public FavoriteRepository() {
        PokeApp.getmComponent().inject(this);
    }

    public void fetchPokemonList() {
        progressBarLiveData.setValue(true);
        getFavoritePokemonObserver();
    }

    public void getFavoritePokemonObserver() {
        List<PokemonFavListAPI> pokemonFavoriteList = favPokemonListDAO.getFavoriteList();
        progressBarLiveData.setValue(false);
        swipeRefreshLayoutLiveData.setValue(true);
        pokemonFavoriteLiveData.setValue(pokemonFavoriteList);
    }

    public void onInsertPokemonIntoFavorite(PokemonFavListAPI pokemonFavListAPI) {
        Observable<PokemonFavListAPI> observable = Observable.just(pokemonFavListAPI);

        Disposable disposableInsertData = observable
                .doOnNext(pokemonInfoAPI -> favPokemonListDAO.insertFavorite(pokemonInfoAPI))
                .subscribeOn(Schedulers.io())
                .subscribe();

        compositeDisposable.add(disposableInsertData);

    }

    public void onRenameFavoritePoke(PokemonFavListAPI pokemonFavListAPI) {
        Observable<PokemonFavListAPI> observable = Observable.just(pokemonFavListAPI);

        Disposable disposableUpdateData = observable
                .doOnNext(pokemonInfoAPI -> favPokemonListDAO.renamePoke(pokemonFavListAPI))
                .subscribeOn(Schedulers.io())
                .subscribe();

        compositeDisposable.add(disposableUpdateData);
    }

    public void onReleasePokemon(String pokemonName) {
        Observable<String> observable = Observable.just(pokemonName);

        Disposable disposableReleaseData = observable
                .doOnNext(pokemonInfoAPI -> favPokemonListDAO.releasePokemon(pokemonName))
                .subscribeOn(Schedulers.io())
                .subscribe();

        compositeDisposable.add(disposableReleaseData);
    }

    public static LiveData<List<PokemonFavListAPI>> getPokemonListLiveData() {
        return pokemonFavoriteLiveData;
    }


    public static LiveData<Boolean> getProgressBarLiveData() {
        return progressBarLiveData;
    }

    public static LiveData<Boolean> getSwipeRefreshLayoutLiveData() {
        return swipeRefreshLayoutLiveData;
    }

    public static LiveData<String> getToastLiveData() {
        return toastLiveData;
    }

    public void resetValuesLiveData() {
        pokemonFavoriteLiveData.postValue(null);
        progressBarLiveData.postValue(null);
        swipeRefreshLayoutLiveData.postValue(null);
        toastLiveData.postValue(null);
    }

    public void getDisposableToUnsubscribe() {
        compositeDisposable.dispose();
    }
}
