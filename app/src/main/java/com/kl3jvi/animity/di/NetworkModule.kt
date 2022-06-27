package com.kl3jvi.animity.di


import android.content.Context
import android.content.SharedPreferences
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.kl3jvi.animity.BuildConfig
import com.kl3jvi.animity.data.network.anilist_service.AniListClient
import com.kl3jvi.animity.data.network.anilist_service.AniListService
import com.kl3jvi.animity.data.network.anime_service.AnimeApiClient
import com.kl3jvi.animity.data.network.anime_service.AnimeService
import com.kl3jvi.animity.data.network.interceptor.HeaderInterceptor
import com.kl3jvi.animity.data.repository.persistence_repository.LocalStorageImpl.Companion.SELECTED_PROVIDER
import com.kl3jvi.animity.domain.repositories.activity_repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.persistence_repositories.LocalStorage
import com.kl3jvi.animity.parsers.Providers
import com.kl3jvi.animity.parsers.Providers.GOGOANIME
import com.kl3jvi.animity.parsers.Providers.NINEANIME
import com.kl3jvi.animity.utils.Apollo
import com.kl3jvi.animity.utils.Constants.Companion.ANILIST_API_URL
import com.kl3jvi.animity.utils.Constants.Companion.GOGO_BASE_URL
import com.kl3jvi.animity.utils.Constants.Companion.NINEANIME_BASE_URL
import com.kl3jvi.animity.utils.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    @Apollo
    fun provideOkHttpClient(
        localStorage: LocalStorage,
        loginRepository: LoginRepository,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addChuckerOnDebug(chuckerInterceptor)
            .addInterceptor(HeaderInterceptor(loginRepository, localStorage))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    @Singleton
    @RetrofitClient
    fun provideRetrofitOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(
        @RetrofitClient okHttpClient: OkHttpClient,
        preferences: SharedPreferences
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GOGO_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provide an ApolloClient instance with the given OkHttpClient instance.
     *
     * @param okHttpClient The OkHttpClient instance that will be used to make the network requests.
     * @return ApolloClient
     */
    @Provides
    @Singleton
    fun provideApolloClient(
        @Apollo okHttpClient: OkHttpClient,
    ): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(ANILIST_API_URL)
            .okHttpClient(okHttpClient)
            .build()
    }

    /**
     * > It takes a Retrofit object as an argument and returns an AnimeService object
     *
     * @param retrofit Retrofit - The Retrofit instance that will be used to create the service.
     * @return A Retrofit object.
     */
    @Singleton
    @Provides
    fun provideAnimeService(retrofit: Retrofit): AnimeService {
        return retrofit.create(AnimeService::class.java)
    }

    @Provides
    @Singleton
    fun provideAnimeApiClient(
        animeService: AnimeService,
        apolloClient: ApolloClient
    ): AnimeApiClient {
        return AnimeApiClient(animeService, apolloClient)
    }


    /**
     * Create an AniListService object using the Retrofit object.
     *
     * @param retrofit Retrofit - The Retrofit object that will be used to create the service.
     * @return AniListService
     */
    @Singleton
    @Provides
    fun provideAniListService(retrofit: Retrofit): AniListService {
        return retrofit.create(AniListService::class.java)
    }

    @Provides
    @Singleton
    fun provideAniListClient(aniListService: AniListService): AniListClient {
        return AniListClient(aniListService)
    }


    @Provides
    @Singleton
    fun provideChucker(
        @ApplicationContext context: Context
    ): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
    }
}

private fun Retrofit.Builder.selectedUrl(selectedProvider: Providers) = apply {
    when (selectedProvider) {
        GOGOANIME -> baseUrl(GOGO_BASE_URL)
        NINEANIME -> baseUrl(NINEANIME_BASE_URL)
    }
}

private fun OkHttpClient.Builder.addChuckerOnDebug(
    chuckerInterceptor: ChuckerInterceptor
) = apply {
    if (BuildConfig.DEBUG) {
        addInterceptor(chuckerInterceptor)
    }
}



