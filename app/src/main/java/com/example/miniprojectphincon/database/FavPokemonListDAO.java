package com.example.miniprojectphincon.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.miniprojectphincon.model.pokemonfavlist.PokemonFavListAPI;
import com.example.miniprojectphincon.model.pokemonlist.ResultsResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface FavPokemonListDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(PokemonFavListAPI pokemonFav);

    @Query("SELECT * FROM PokemonFavoriteList")
    List<PokemonFavListAPI> getFavoriteList();

    @Update
    public void renamePoke(PokemonFavListAPI pokemonFavListAPI);

    @Query("DELETE FROM PokemonFavoriteList  WHERE `name` = :name")
    void releasePokemon(String name);
}
