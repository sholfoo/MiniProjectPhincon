package com.example.miniprojectphincon.model.pokemonlist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PokemonListAPI {

    @SerializedName("results")
    public List<ResultsResponse> results;

    public List<ResultsResponse> getResults() {
        return results;
    }
}
