package com.ilhamrhmtkbr.di;

import android.content.Context;

import com.ilhamrhmtkbr.core.network.CookieManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AuthModule {

    @Provides
    @Singleton
    public CookieManager provideCookieManager(@ApplicationContext Context context) {
        return new CookieManager(context);
    }
}
