package com.a65apps.application.personlocation;

import androidx.annotation.NonNull;

import com.a65apps.application.scopes.PersonLocationScope;
import com.a65apps.core.interactors.locations.PersonLocationInteractor;
import com.a65apps.core.interactors.locations.PersonLocationModel;
import com.a65apps.core.interactors.locations.PersonLocationRepository;
import com.a65apps.core.interactors.organizations.LocationOrganizationInteractor;
import com.a65apps.library.mapper.LocationModelMapper;
import com.a65apps.library.presenters.PersonMapPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PersonLocationModule {
    @Provides
    @PersonLocationScope
    @NonNull
    public PersonMapPresenter providePersonMapPresenter(
            @NonNull PersonLocationInteractor personLocationInteractor,
            @NonNull LocationOrganizationInteractor organizationInteractor,
            @NonNull LocationModelMapper mapper) {
        return new PersonMapPresenter(personLocationInteractor, mapper);
    }

    @Provides
    @PersonLocationScope
    @NonNull
    public PersonLocationInteractor providePersonLocationModel(
            @NonNull PersonLocationRepository repository) {
        return new PersonLocationModel(repository);
    }

    @Provides
    @PersonLocationScope
    @NonNull
    public LocationModelMapper provideLocationModelMapper() {
        return new LocationModelMapper();
    }
}
