package com.sayone.omidyar;

import javax.inject.Singleton;

import dagger.Component;

@Component
@Singleton
public interface AppComponent {
    void inject(Omidyar omidyar);
//    User user();
//    void inject(ProductCollectionAdapter productCollectionAdapter);
}
