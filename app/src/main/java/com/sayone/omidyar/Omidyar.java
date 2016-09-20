package com.sayone.omidyar;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sayone on 15/9/16.
 */
public class Omidyar extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        // The Realm file will be located in Context.getFilesDir() with name "default.realm"
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        mAppComponent = DaggerAppComponent.create();
        mAppComponent.inject(this);
    }
}
