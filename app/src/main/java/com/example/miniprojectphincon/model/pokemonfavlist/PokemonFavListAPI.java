package com.example.miniprojectphincon.model.pokemonfavlist;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.miniprojectphincon.database.Converters;
import com.example.miniprojectphincon.model.pokemoninfo.TypesResponse;

import java.util.List;

@Entity(tableName = "PokemonFavoriteList")
public class PokemonFavListAPI {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    public String pokeName;

    @ColumnInfo(name = "image")
    public String pokeImage;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "pokeTypes")
    public List<TypesResponse> pokeTypes;

    @ColumnInfo(name = "pokeHeight")
    @Ignore
    public Integer height;
    public String heightFormatted;

    @ColumnInfo(name = "pokeWeight")
    @Ignore
    public Integer weight;
    public String weightFormatted;

    public String getPokeName() {
        return pokeName;
    }

    public String getPokeImage() {
        return pokeImage;
    }

    public List<TypesResponse> getPokeTypes() {
        return pokeTypes;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setPokeName(@NonNull String pokeName) {
        this.pokeName = pokeName;
    }

    public void setPokeImage(String pokeImage) {
        this.pokeImage = pokeImage;
    }

    public void setPokeTypes(List<TypesResponse> pokeTypes) {
        this.pokeTypes = pokeTypes;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setHeightFormatted(String heightFormatted) {
        this.heightFormatted = heightFormatted;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setWeightFormatted(String weightFormatted) {
        this.weightFormatted = weightFormatted;
    }

    public PokemonFavListAPI() {
    }

    public PokemonFavListAPI(@NonNull String pokeName, String pokeImage, List<TypesResponse> pokeTypes, Integer height, String heightFormatted, Integer weight, String weightFormatted) {
        this.pokeName = pokeName;
        this.pokeImage = pokeImage;
        this.pokeTypes = pokeTypes;
        this.height = height;
        this.heightFormatted = heightFormatted;
        this.weight = weight;
        this.weightFormatted = weightFormatted;
    }
}
