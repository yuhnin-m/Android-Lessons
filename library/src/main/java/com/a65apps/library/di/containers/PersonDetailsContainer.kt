package com.a65apps.library.di.containers

import com.a65apps.library.ui.fragments.PersonDetailsFragment

interface PersonDetailsContainer {
    fun inject(contactDetailsFragment: PersonDetailsFragment)
}
