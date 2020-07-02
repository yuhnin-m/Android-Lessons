package com.a65apps.yuhnin.lesson1.di.app;

import android.content.Context;

import com.a65apps.yuhnin.lesson1.repository.ContactRepository;
import com.a65apps.yuhnin.lesson1.repository.ContactRepositoryFromSystem;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    Context context;

    public RepositoryModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public ContactRepository provideContactRepository() {
        return new ContactRepositoryFromSystem(context);
    }
}
