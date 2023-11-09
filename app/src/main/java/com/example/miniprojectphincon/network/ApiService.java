package com.example.miniprojectphincon.network;
import com.example.miniprojectphincon.model.pokemoninfo.PokemonInfoAPI;
import com.example.miniprojectphincon.model.pokemonlist.PokemonListAPI;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by @sholfoo on 26/11/2018, with awesomeness.
 */
public interface ApiService {

    @GET("pokemon?limit=20")
    Observable<PokemonListAPI> fetchPokemonList(@Query("offset") int offset);

    @GET("pokemon/{name}")
    Observable<PokemonInfoAPI> fetchPokemonInfo(@Path("name") String name);
}

