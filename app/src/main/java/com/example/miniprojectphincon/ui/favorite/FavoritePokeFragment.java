package com.example.miniprojectphincon.ui.favorite;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.miniprojectphincon.R;
import com.example.miniprojectphincon.adapter.FavPokemonRecyclerViewAdapter;
import com.example.miniprojectphincon.base.BaseFragment;
import com.example.miniprojectphincon.databinding.FramgmentFavoriteBinding;
import com.example.miniprojectphincon.model.pokemonfavlist.PokemonFavListAPI;
import com.example.miniprojectphincon.repository.FavoriteRepository;
import com.example.miniprojectphincon.viewmodel.FavoriteViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

public class FavoritePokeFragment extends BaseFragment implements FavPokemonRecyclerViewAdapter.OnItemClickListener {
    private FavoriteRepository favoriteRepository;
    private FavoriteViewModel favoriteViewModel;
    private FramgmentFavoriteBinding framgmentFavoriteBinding;
    private FavPokemonRecyclerViewAdapter favPokemonRecyclerViewAdapter;

    private boolean checking0, checking1, checking2;
    private boolean loading = true;

    public FavoritePokeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteRepository = new FavoriteRepository();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        framgmentFavoriteBinding = FramgmentFavoriteBinding.inflate(inflater, container, false);
        setUpRecyclerView();
        favoriteViewModel = new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);

        return framgmentFavoriteBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pullToRefresh();
        updateDataForUI();
        updateProgressBarForUI();
        updateSwipeRefreshLayoutForUI();
        updateToastForUI();
        setArrowButton();
        handleOnBackPressed();

        if (savedInstanceState == null) {
            checking0 = true;
            checking1 = true;
            checking2 = true;
            favoriteRepository.fetchPokemonList();
        }
    }

    private void updateDataForUI() {
        favoriteViewModel.getPokemonFavoriteListLiveData().observe(getViewLifecycleOwner(), pokemonList -> {
            if (pokemonList != null) {
                favPokemonRecyclerViewAdapter.refreshPokemonList(pokemonList);
            }
        });
    }

    private void updateProgressBarForUI() {
        favoriteViewModel.getProgressBarLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if (checking0 && aBoolean != null) {
                if (aBoolean) {
                    framgmentFavoriteBinding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    framgmentFavoriteBinding.progressBar.setVisibility(View.GONE);
                }
            } else {
                checking0 = true;
            }
        });
    }

    private void updateSwipeRefreshLayoutForUI() {
        favoriteViewModel.getSwipeRefreshLayoutLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null) {
                if (checking1 && aBoolean) {
                    loading = true;
                    framgmentFavoriteBinding.swipeRefreshLayout.setRefreshing(false);
                } else {
                    checking1 = true;
                }
            }
        });
    }

    private void updateToastForUI() {
        favoriteViewModel.getToastLiveData().observe(getViewLifecycleOwner(), string -> {
            if (string != null) {
                if (checking2) {
                    if (string.isEmpty()) {
                        Toast.makeText(requireActivity(), getResources().getString(R.string.ToastForOfflineMode), Toast.LENGTH_SHORT).show();
                    } else {
                        loading = true;
                        framgmentFavoriteBinding.swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(requireActivity(), string, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    checking2 = true;
                }
            }
        });
    }

    private void pullToRefresh() {
        framgmentFavoriteBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            favPokemonRecyclerViewAdapter.clearAllOldData();
            favoriteRepository.fetchPokemonList();
        });
    }

    private void setUpRecyclerView() {
        framgmentFavoriteBinding.pokemonList.setHasFixedSize(true);
        favPokemonRecyclerViewAdapter = new FavPokemonRecyclerViewAdapter(requireActivity(), this, new FavPokemonRecyclerViewAdapter.PokemonDiff());
        framgmentFavoriteBinding.pokemonList.setAdapter(favPokemonRecyclerViewAdapter);
    }

    private void setArrowButton() {
        framgmentFavoriteBinding.actionHome.setOnClickListener(view -> {
            favoriteRepository.resetValuesLiveData();
            requireActivity().finish();
        });
    }

    private void handleOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                favoriteRepository.resetValuesLiveData();
                requireActivity().finish();
            }
        });
    }

    private void releasePokemon(ProgressDialog pd, BottomSheetDialog bottomSheetDialog,String namePoke) {
        new Handler().postDelayed(() -> {
            favoriteRepository.onReleasePokemon(namePoke);
            pd.dismiss();
            bottomSheetDialog.dismiss();
            Toast.makeText(requireActivity(), getResources().getString(R.string.ToastReleasePokemon), Toast.LENGTH_SHORT).show();
            updateDataForUI();
        }, 2000);

    }

    private void renamePokemon(ProgressDialog pd, BottomSheetDialog bottomSheetDialog, PokemonFavListAPI pokemonFavListAPI) {
        new Handler().postDelayed(() -> {
            favoriteRepository.onRenameFavoritePoke(pokemonFavListAPI);
            pd.dismiss();
            bottomSheetDialog.dismiss();
            Toast.makeText(requireActivity(), getResources().getString(R.string.ToastRenamePokemon), Toast.LENGTH_SHORT).show();
            updateDataForUI();
        }, 2000);

    }

    @Override
    public void onClick(PokemonFavListAPI pokemonFavListAPI) {
        //show dialog to rename and release
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
        LinearLayout actionRename = bottomSheetDialog.findViewById(R.id.actionRename);
        LinearLayout actionRelease = bottomSheetDialog.findViewById(R.id.actionRelease);
        bottomSheetDialog.show();

        actionRelease.setOnClickListener(view -> {
            ProgressDialog progressdialog = new ProgressDialog(mContext);
            progressdialog.setMessage("Releasing Pokemon...");
            progressdialog.show();
            releasePokemon(progressdialog, bottomSheetDialog, pokemonFavListAPI.getPokeName());
        });

        actionRename.setOnClickListener(view -> {
            String newName;
            MaterialDialog.Builder inputDialog = new MaterialDialog.Builder(mContext);
            inputDialog.negativeText(android.R.string.cancel)
                    .positiveText(android.R.string.ok)
                    .onPositive((dialog, which) -> {
                        // rename pokemon
                        pokemonFavListAPI.setPokeName(dialog.getInputEditText().getText().toString());
                        ProgressDialog progressdialog = new ProgressDialog(mContext);
                        progressdialog.setMessage("Renaming Pokemon...");
                        progressdialog.show();
                        renamePokemon(progressdialog, bottomSheetDialog, pokemonFavListAPI);

                    })
                    .content("Rename Pokemons")
                    .input("Rename Your Poke", "", (dialog, input) -> {

                    })
                    .show();
        });
    }
}
