package com.example.miniprojectphincon;

import com.example.miniprojectphincon.dagger.PerApp;
import com.example.miniprojectphincon.database.DatabaseModule;
import com.example.miniprojectphincon.network.NetworkModule;
import com.example.miniprojectphincon.repository.DetailRepository;
import com.example.miniprojectphincon.repository.FavoriteRepository;
import com.example.miniprojectphincon.repository.MainRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        NetworkModule.class,
        DatabaseModule.class
})
public interface PokeComponent {

    void inject(DetailRepository detailRepository);
    void inject(MainRepository mainRepository);
    void inject(FavoriteRepository favoriteRepository);

    final class initializer {
        public static PokeComponent init(PokeApp PokeApp) {
            return DaggerPokeComponent.builder().networkModule(new NetworkModule()).databaseModule(new DatabaseModule(PokeApp)).build();
        }
    }

}
