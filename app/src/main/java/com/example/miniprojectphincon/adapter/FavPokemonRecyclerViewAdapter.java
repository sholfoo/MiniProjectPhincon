package com.example.miniprojectphincon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.miniprojectphincon.R;
import com.example.miniprojectphincon.databinding.ListPokemonItemBinding;
import com.example.miniprojectphincon.model.pokemonfavlist.PokemonFavListAPI;
import com.example.miniprojectphincon.model.pokemonlist.ResultsResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FavPokemonRecyclerViewAdapter extends ListAdapter<PokemonFavListAPI, FavPokemonRecyclerViewAdapter.RecyclerViewViewHolder> {

    private final Context context;
    private final OnItemClickListener onItemClickListener;
    private final List<PokemonFavListAPI> pokemonList = new ArrayList<>();

    public FavPokemonRecyclerViewAdapter(Context context, OnItemClickListener onItemClickListener, @NonNull DiffUtil.ItemCallback<PokemonFavListAPI> diffItemCallback) {
        super(diffItemCallback);
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public FavPokemonRecyclerViewAdapter(@NonNull AsyncDifferConfig<PokemonFavListAPI> config, Context context, OnItemClickListener onItemClickListener) {
        super(config);
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListPokemonItemBinding pokemonItemBinding = ListPokemonItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new RecyclerViewViewHolder(pokemonItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        PokemonFavListAPI item = pokemonList.get(position);

        String namePoke = item.getPokeName();
        String imagePoke = item.getPokeImage();

        holder.pokemonItemBinding.namePoke.setText(namePoke);
        Glide.with(context).load(imagePoke).placeholder(R.drawable.placeholder).error(R.drawable.error).into(holder.pokemonItemBinding.imagePoke);

        Glide.with(context).load(imagePoke)
//                .listener(
//                        GlidePalette.with(imagePoke).use(BitmapPalette.Profile.MUTED_LIGHT)
//                                .intoCallBack(palette -> {
//                                    if (palette != null && palette.getDominantSwatch() != null) {
//                                        int rgbHexCode = palette.getDominantSwatch().getRgb();
//                                        holder.pokemonItemBinding.cardView.setCardBackgroundColor(rgbHexCode);
//                                    }
//                                }).crossfade(true))
                .into(holder.pokemonItemBinding.imagePoke);

        holder.itemView.setOnClickListener(view -> onItemClickListener.onClick(item));
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public void refreshPokemonList(List<PokemonFavListAPI> data) {
        pokemonList.addAll(data);
        notifyDataSetChanged();
    }

    public void clearAllOldData() {
        pokemonList.clear();
        notifyDataSetChanged();
    }

    public static class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

        private final ListPokemonItemBinding pokemonItemBinding;

        public RecyclerViewViewHolder(ListPokemonItemBinding pokemonItemBinding) {
            super(pokemonItemBinding.getRoot());
            this.pokemonItemBinding = pokemonItemBinding;
        }
    }

    public static class PokemonDiff extends DiffUtil.ItemCallback<PokemonFavListAPI> {

        @Override
        public boolean areItemsTheSame(@NonNull @NotNull PokemonFavListAPI oldItem, @NonNull @NotNull PokemonFavListAPI newItem) {
            return oldItem.getPokeName().equals(newItem.getPokeName());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull @NotNull PokemonFavListAPI oldItem, @NonNull @NotNull PokemonFavListAPI newItem) {
            return oldItem == newItem;
        }
    }

    public interface OnItemClickListener {
        void onClick(PokemonFavListAPI pokemonFavListAPIItem);
    }
}
