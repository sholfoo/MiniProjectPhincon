package com.example.miniprojectphincon.ui.detail;

import static com.example.miniprojectphincon.ui.main.MainFragment.EXTRA_IMAGE_PARAM;
import static com.example.miniprojectphincon.ui.main.MainFragment.EXTRA_NAME_PARAM;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.miniprojectphincon.R;
import com.example.miniprojectphincon.adapter.TypeRecyclerViewListAdapter;
import com.example.miniprojectphincon.base.BaseFragment;
import com.example.miniprojectphincon.databinding.FragmentDetailBinding;
import com.example.miniprojectphincon.model.pokemonfavlist.PokemonFavListAPI;
import com.example.miniprojectphincon.model.pokemoninfo.TypesResponse;
import com.example.miniprojectphincon.repository.DetailRepository;
import com.example.miniprojectphincon.repository.FavoriteRepository;
import com.example.miniprojectphincon.viewmodel.DetailViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DetailPokeFragment extends BaseFragment {

    private FragmentDetailBinding fragmentDetailBinding;
    private TypeRecyclerViewListAdapter typeRecyclerViewListAdapter;
    private DetailRepository detailRepository;
    private FavoriteRepository favoriteRepository;
    private DetailViewModel detailViewModel;
    private String namePoke;
    private String imagePoke;

    private List<TypesResponse> types;
    private String weight;
    private String height;
    private PokemonFavListAPI pokemonFavListAPI;
    private boolean checking0, checking1;
    private final String STATE_NAME = "Current Name";
    private final String STATE_IMAGE = "Current Image";

    public DetailPokeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailRepository = new DetailRepository();
        favoriteRepository = new FavoriteRepository();
        pokemonFavListAPI = new PokemonFavListAPI();

        if (savedInstanceState == null) {
            namePoke = requireActivity().getIntent().getExtras().getString(EXTRA_NAME_PARAM);
            imagePoke = requireActivity().getIntent().getExtras().getString(EXTRA_IMAGE_PARAM);
        } else {
            namePoke = savedInstanceState.getString(STATE_NAME);
            imagePoke = savedInstanceState.getString(STATE_IMAGE);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentDetailBinding = FragmentDetailBinding.inflate(inflater, container, false);
        setUpRecyclerView();
        detailViewModel = new ViewModelProvider(requireActivity()).get(DetailViewModel.class);

        return fragmentDetailBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpDataGetFromMainFragment();
        setArrowButton();
        setCatchButton();
        updateDataForUI();
        updateProgressBarForUI();
        updateToastForUI();
        handleOnBackPressed();

        if (savedInstanceState == null) {
            checking0 = true;
            checking1 = true;
            fetchPokemonInfo(namePoke);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_NAME, namePoke);
        outState.putString(STATE_IMAGE, imagePoke);
    }

    private void setUpRecyclerView() {
        fragmentDetailBinding.typeList.setHasFixedSize(true);
        typeRecyclerViewListAdapter = new TypeRecyclerViewListAdapter(requireActivity(), new TypeRecyclerViewListAdapter.TypeDiff());
        fragmentDetailBinding.typeList.setAdapter(typeRecyclerViewListAdapter);
    }

    private void setUpDataGetFromMainFragment() {
        fragmentDetailBinding.namePoke.setText(namePoke);
        Glide.with(this).load(imagePoke).placeholder(R.drawable.placeholder).into(fragmentDetailBinding.imagePoke);

        Glide.with(this).load(imagePoke)
//                .listener(
//                        GlidePalette.with(imagePoke).use(BitmapPalette.Profile.MUTED_LIGHT)
//                                .intoCallBack(palette -> {
//                                    if (palette != null && palette.getDominantSwatch() != null) {
//                                        int rgbHexCode = palette.getDominantSwatch().getRgb();
//                                        fragmentDetailBinding.cardView.setCardBackgroundColor(rgbHexCode);
//                                    }
//                                }).crossfade(true))
                .into(fragmentDetailBinding.imagePoke);
    }

    private void setArrowButton() {
        fragmentDetailBinding.actionHome.setOnClickListener(view -> {
            detailRepository.resetValuesLiveData();
            requireActivity().finish();
        });
    }

    private void setCatchButton() {
        fragmentDetailBinding.catchPokemon.setOnClickListener(view -> {
            ProgressDialog progressdialog = new ProgressDialog(mContext);
            progressdialog.setMessage("Catching Pokemon...");
            progressdialog.show();
            catchPokemon(progressdialog);
        });
    }

    private void fetchPokemonInfo(String namePoke) {
        detailRepository.fetchPokemonInfo(namePoke);
    }

    private void catchPokemon(ProgressDialog pd) {

        pokemonFavListAPI.setPokeName(namePoke);
        pokemonFavListAPI.setPokeImage(imagePoke);
        pokemonFavListAPI.setPokeTypes(types);
        pokemonFavListAPI.setHeightFormatted(height);
        pokemonFavListAPI.setWeightFormatted(weight);
        new Handler().postDelayed(() -> {
            favoriteRepository.onInsertPokemonIntoFavorite(pokemonFavListAPI);
            pd.dismiss();
            Toast.makeText(requireActivity(), getResources().getString(R.string.ToastCatchPokemon), Toast.LENGTH_SHORT).show();
        }, 2000);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateDataForUI() {
        detailViewModel.getPokemonInfoLiveData().observe(getViewLifecycleOwner(), pokemonInfo -> {
            if (pokemonInfo != null && namePoke.equals(pokemonInfo.name)) {
                typeRecyclerViewListAdapter.refreshTypeList(pokemonInfo.types);
                fragmentDetailBinding.height.setText(pokemonInfo.heightFormatted);
                fragmentDetailBinding.weight.setText(pokemonInfo.weightFormatted);

                types = pokemonInfo.types;
                weight = pokemonInfo.weightFormatted;
                height = pokemonInfo.heightFormatted;
            }
        });
    }

    private void updateProgressBarForUI() {
        detailViewModel.getProgressBarLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null) {
                if (checking0 && aBoolean) {
                    fragmentDetailBinding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    fragmentDetailBinding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updateToastForUI() {
        detailViewModel.getToastLiveData().observe(getViewLifecycleOwner(), string -> {
            if (checking1 && string != null) {
                if (string.isEmpty()) {
                    Toast.makeText(requireActivity(), getResources().getString(R.string.ToastForOfflineMode), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireActivity(), string, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                detailRepository.resetValuesLiveData();
                requireActivity().finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        detailRepository.getDisposableToUnsubscribe();
    }
}