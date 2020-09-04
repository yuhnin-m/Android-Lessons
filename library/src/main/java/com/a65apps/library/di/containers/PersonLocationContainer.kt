package com.a65apps.library.di.containers

import com.a65apps.library.ui.fragments.PersonMapsFragment

interface PersonLocationContainer {
    fun inject(personMapsFragment: PersonMapsFragment)
}