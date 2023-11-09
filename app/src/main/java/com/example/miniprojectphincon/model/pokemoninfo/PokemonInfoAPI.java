package com.example.miniprojectphincon.model.pokemoninfo;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.miniprojectphincon.database.Converters;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.SplittableRandom;

@RequiresApi(api = Build.VERSION_CODES.N)
@Entity(tableName = "PokemonInfo")
public class PokemonInfoAPI {

    @PrimaryKey
    @NonNull
    public String name;

    @TypeConverters(Converters.class)
    @SerializedName("types")
    public List<TypesResponse> types;

    @SerializedName("height")
    @Ignore
    public Integer height;
    public String heightFormatted;

    @SerializedName("weight")
    @Ignore
    public Integer weight;
    public String weightFormatted;

    public List<TypesResponse> getTypes() {
        return types;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }

    public PokemonInfoAPI(@NonNull String name, List<TypesResponse> types, String heightFormatted, String weightFormatted) {
        this.name = name;
        this.types = types;
        this.heightFormatted = heightFormatted;
        this.weightFormatted = weightFormatted;
    }
}
