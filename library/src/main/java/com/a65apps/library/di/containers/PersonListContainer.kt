package com.a65apps.library.di.containers

import com.a65apps.library.ui.fragments.PersonListFragment

interface PersonListContainer {
    fun inject(contactListFragment: PersonListFragment)
}