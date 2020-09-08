package com.a65apps.application.personlist;

import com.a65apps.application.scopes.PersonListScope;
import com.a65apps.core.interactors.persons.PersonListInteractor;
import com.a65apps.core.interactors.persons.PersonListModel;
import com.a65apps.core.interactors.persons.PersonListRepository;
import com.a65apps.library.mapper.PersonModelCompactDataMapper;
import com.a65apps.library.presenters.PersonListPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PersonListModule {
    @Provides
    @PersonListScope
    public PersonListPresenter providePersonListPresenter(PersonListInteractor interactor,
                                                          PersonModelCompactDataMapper mapper) {
        return new PersonListPresenter(interactor, mapper);
    }

    @Provides
    @PersonListScope
    public PersonListInteractor providePersonListInteractor(PersonListRepository personListRepository) {
        return new PersonListModel(personListRepository);
    }

    @Provides
    @PersonListScope
    public PersonModelCompactDataMapper providePersonModelCompactDataMapper() {
        return new PersonModelCompactDataMapper();
    }
}
