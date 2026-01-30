package com.ilhamrhmtkbr.di;

import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.core.network.CookieManager;
import com.ilhamrhmtkbr.core.network.interceptor.FormDataInterceptor;
import com.ilhamrhmtkbr.data.remote.api.ForumApi;
import com.ilhamrhmtkbr.data.remote.api.InstructorApi;
import com.ilhamrhmtkbr.data.remote.api.PublicApi;
import com.ilhamrhmtkbr.data.remote.api.StudentApi;
import com.ilhamrhmtkbr.data.remote.api.UserApi;
import com.ilhamrhmtkbr.core.network.interceptor.TokenRefreshInterceptor;
import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.core.utils.tools.LangUtil;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;
import javax.inject.Qualifier;
import javax.inject.Singleton;
import javax.inject.Named;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Qualifier
    @interface InstructorRetrofit {}

    @Qualifier
    @interface UserRetrofit {}

    @Qualifier
    @interface PublicRetrofit {}

    @Qualifier
    @interface StudentRetrofit {}

    @Qualifier
    @interface ForumRetrofit {}

    // ============================================
    // INTERCEPTORS
    // ============================================

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return interceptor;
    }

    @Provides
    @Singleton
    public FormDataInterceptor provideAuthInterceptor(
            CookieManager cookieManager,
            LangUtil langUtil
    ) {
        return new FormDataInterceptor(cookieManager, langUtil);
    }

    @Provides
    @Singleton
    public TokenRefreshInterceptor provideTokenRefreshInterceptor(
            AuthStateManager authStateManager,
            @Named("TokenRefreshClient") OkHttpClient tokenRefreshClient,
            CookieManager cookieManager
    ) {
        Retrofit tokenRefreshRetrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.USER_API_URL)
                .client(tokenRefreshClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi userApi = tokenRefreshRetrofit.create(UserApi.class);
        return new TokenRefreshInterceptor(authStateManager, userApi, cookieManager);
    }

    // ============================================
    // OKHTTP CLIENTS
    // ============================================

    @Provides
    @Singleton
    @Named("TokenRefreshClient")
    public OkHttpClient provideTokenRefreshClient(
            CookieManager cookieManager,
            FormDataInterceptor formDataInterceptor,
            HttpLoggingInterceptor loggingInterceptor
    ) {
        return new OkHttpClient.Builder()
                .cookieJar(cookieManager.getCookieJar())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(formDataInterceptor)
                .addInterceptor(loggingInterceptor) // TAMBAHKAN INI
                .build();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(
            CookieManager cookieManager,
            FormDataInterceptor formDataInterceptor,
            TokenRefreshInterceptor tokenRefreshInterceptor,
            HttpLoggingInterceptor loggingInterceptor
    ) {
        return new OkHttpClient.Builder()
                .cookieJar(cookieManager.getCookieJar())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(formDataInterceptor)
                .addInterceptor(tokenRefreshInterceptor)
                .addInterceptor(loggingInterceptor) // TAMBAHKAN INI
                .build();
    }

    // ============================================
    // RETROFIT INSTANCES
    // ============================================

    @Provides
    @Singleton
    @InstructorRetrofit
    public Retrofit provideInstructorRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.INSTRUCTOR_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @UserRetrofit
    public Retrofit provideUserRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.USER_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @PublicRetrofit
    public Retrofit providePublicRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.PUBLIC_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @StudentRetrofit
    public Retrofit provideStudentRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.STUDENT_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @ForumRetrofit
    public Retrofit provideForumRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.FORUM_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // ============================================
    // API SERVICES
    // ============================================

    @Provides
    @Singleton
    public InstructorApi provideInstructorApi(
            @InstructorRetrofit Retrofit retrofit
    ) {
        return retrofit.create(InstructorApi.class);
    }

    @Provides
    @Singleton
    public UserApi provideUserApi(
            @UserRetrofit Retrofit retrofit
    ) {
        return retrofit.create(UserApi.class);
    }

    @Provides
    @Singleton
    public PublicApi providePublicApi(
            @PublicRetrofit Retrofit retrofit
    ) {
        return retrofit.create(PublicApi.class);
    }

    @Provides
    @Singleton
    public StudentApi provideStudentApi(
            @StudentRetrofit Retrofit retrofit
    ) {
        return retrofit.create(StudentApi.class);
    }

    @Provides
    @Singleton
    public ForumApi provideForumApi(
            @ForumRetrofit Retrofit retrofit
    ) {
        return retrofit.create(ForumApi.class);
    }
}