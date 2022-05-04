package com.kl3jvi.animity.di


import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.kl3jvi.animity.data.network.anilist_service.AniListClient
import com.kl3jvi.animity.data.network.anilist_service.AniListService
import com.kl3jvi.animity.data.network.anime_service.AnimeApiClient
import com.kl3jvi.animity.data.network.anime_service.AnimeService
import com.kl3jvi.animity.data.network.interceptor.HeaderInterceptor
import com.kl3jvi.animity.data.repository.persistence_repository.LocalStorageImpl
import com.kl3jvi.animity.domain.repositories.activity_repositories.LoginRepository
import com.kl3jvi.animity.utils.Constants.Companion.ANILIST_API_URL
import com.kl3jvi.animity.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    /* Providing an OkHttpClient to the dagger. */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        localStorage: LocalStorageImpl,
        loginRepository: LoginRepository
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(loginRepository, localStorage))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    @Singleton
    @Named("RetrofitOkHttp")
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
        @Named("RetrofitOkHttp") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
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
        okHttpClient: OkHttpClient,
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
}


