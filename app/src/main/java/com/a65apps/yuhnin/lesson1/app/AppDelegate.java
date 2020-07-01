package com.a65apps.yuhnin.lesson1.app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a65apps.yuhnin.lesson1.di.app.AppComponent;
import com.a65apps.yuhnin.lesson1.di.app.AppModule;
import com.a65apps.yuhnin.lesson1.di.app.DaggerAppComponent;
import com.a65apps.yuhnin.lesson1.di.app.RepositoryModule;

public class AppDelegate extends Application {
    @Nullable
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDependencies();
    }

    private void initDependencies() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .repositoryModule(new RepositoryModule())
                .build();
    }

    @NonNull
    public AppComponent getAppComponent() {
        if (appComponent == null) {
            initDependencies();
        }
        return appComponent;
    }
}
