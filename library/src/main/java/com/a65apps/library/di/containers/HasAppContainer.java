package com.a65apps.library.di.containers;

import io.reactivex.rxjava3.annotations.NonNull;

public interface HasAppContainer {
    @NonNull
    AppContainer appContainer();
}
