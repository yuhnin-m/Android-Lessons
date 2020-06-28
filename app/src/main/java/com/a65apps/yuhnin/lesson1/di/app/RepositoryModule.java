package com.a65apps.yuhnin.lesson1.di.app;

import com.a65apps.yuhnin.lesson1.repository.ContactRepository;
import com.a65apps.yuhnin.lesson1.repository.ContactRepositoryFromSystem;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    @Provides
    @Singleton
    public ContactRepository provideContactRepository() {
        return new ContactRepositoryFromSystem();

    }
}
