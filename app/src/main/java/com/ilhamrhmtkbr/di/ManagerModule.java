package com.ilhamrhmtkbr.di;

import android.content.Context;

import com.google.mlkit.common.sdkinternal.SharedPrefManager;
import com.ilhamrhmtkbr.core.network.NetworkManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ManagerModule {

    @Provides
    @Singleton
    public SharedPrefManager provideSharedPrefManager(@ApplicationContext Context context) {
        return new SharedPrefManager(context);
    }

    @Provides
    @Singleton
    public NetworkManager provideNetworkManager(@ApplicationContext Context context) {
        return new NetworkManager(context);
    }
}
