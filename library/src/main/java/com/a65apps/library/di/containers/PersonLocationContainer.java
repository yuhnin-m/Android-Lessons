package com.a65apps.library.di.containers;

import com.a65apps.library.ui.fragments.PersonListFragment;
import com.a65apps.library.ui.fragments.PersonMapsFragment;

public interface PersonLocationContainer {
    void inject(PersonMapsFragment personMapsFragment);
}
