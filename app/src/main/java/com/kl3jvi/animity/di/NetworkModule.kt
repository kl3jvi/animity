package com.kl3jvi.animity.di


import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.kl3jvi.animity.data.network.anilist_service.AniListClient
import com.kl3jvi.animity.data.network.anilist_service.AniListService
import com.kl3jvi.animity.data.network.anime_service.AnimeApiClient
import com.kl3jvi.animity.data.network.anime_service.AnimeService
import com.kl3jvi.animity.data.network.interceptor.HeaderInterceptor
import com.kl3jvi.animity.data.repository.persistence_repository.LocalStorageImpl
import com.kl3jvi.animity.utils.Constants.Companion.ANILIST_API_URL
import com.kl3jvi.animity.utils.Constants.Companion.BASE_URL
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
    fun provideOkHttpClient(
        localStorage: LocalStorageImpl
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor( localStorage))
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
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())

            .build()
    }

    @Provides
    @Singleton
    fun provideApolloClient(
        okHttpClient: OkHttpClient,
    ): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(ANILIST_API_URL)
            .okHttpClient(okHttpClient)
            .build()
    }


    @Singleton
    @Provides
    fun provideAnimeService(retrofit: Retrofit): AnimeService {
        return retrofit.create(AnimeService::class.java)
    }

    @Provides
    @Singleton
    fun provideAnimeApiClient(animeService: AnimeService): AnimeApiClient {
        return AnimeApiClient(animeService)
    }


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
}


