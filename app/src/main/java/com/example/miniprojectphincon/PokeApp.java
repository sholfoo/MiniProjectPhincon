package com.example.miniprojectphincon;

public class PokeApp extends BaseApplication {
    private static PokeComponent mComponent;
    private static PokeApp instance;


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        mComponent = PokeComponent
                .initializer
                .init(this);
    }

    public static PokeApp getInstance() {
        return instance;
    }

    public static PokeComponent getmComponent() {
        return mComponent;
    }
}
