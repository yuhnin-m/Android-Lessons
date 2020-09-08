package com.a65apps.application.personlocation;

import com.a65apps.application.scopes.PersonLocationScope;
import com.a65apps.core.interactors.organizations.LocationOrganizationInteractor;
import com.a65apps.core.interactors.organizations.LocationOrganizationModel;
import com.a65apps.core.interactors.organizations.LocationOrganizationRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationOrganizationModule {
    @Provides
    @PersonLocationScope
    LocationOrganizationInteractor provideLocationOrganizationInteractor(LocationOrganizationRepository repository) {
        return new LocationOrganizationModel(repository);
    }
}