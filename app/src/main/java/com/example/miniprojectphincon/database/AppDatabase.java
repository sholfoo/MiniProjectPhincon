package com.example.miniprojectphincon.database;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.miniprojectphincon.model.pokemonfavlist.PokemonFavListAPI;
import com.example.miniprojectphincon.model.pokemoninfo.PokemonInfoAPI;
import com.example.miniprojectphincon.model.pokemonlist.ResultsResponse;

@RequiresApi(api = Build.VERSION_CODES.N)
@Database(entities = {ResultsResponse.class, PokemonInfoAPI.class, PokemonFavListAPI.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract PokemonListDAO pokemonListDAO();

    public abstract PokemonInfoDAO pokemonInfoDAO();

    public abstract FavPokemonListDAO favPokemonListDAO();
}
