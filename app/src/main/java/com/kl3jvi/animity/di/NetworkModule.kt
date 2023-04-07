package com.kl3jvi.animity.di

import android.content.Context
import android.net.ConnectivityManager
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.kl3jvi.animity.data.network.anilist_service.AniListAuthService
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.data.network.anime_service.base.ApiServiceSingleton
import com.kl3jvi.animity.data.network.anime_service.base.BaseClient
import com.kl3jvi.animity.data.network.anime_service.enime.EnimeClient
import com.kl3jvi.animity.data.network.anime_service.gogo.GogoAnimeApiClient
import com.kl3jvi.animity.data.network.interceptor.HeaderInterceptor
import com.kl3jvi.animity.domain.repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.parsers.BaseParser
import com.kl3jvi.animity.parsers.EnimeParser
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.utils.Apollo
import com.kl3jvi.animity.utils.Constants.Companion.ANILIST_API_URL
import com.kl3jvi.animity.utils.Constants.Companion.GOGO_BASE_URL
import com.kl3jvi.animity.utils.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    @Apollo
    fun provideOkHttpClient(
        localStorage: PersistenceRepository,
        loginRepository: LoginRepository
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(loginRepository, localStorage))
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
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
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            )
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @RetrofitClient okHttpClient: OkHttpClient,
        @Named("base-url") url: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Named("base-url")
    fun provideBaseUrl(): String {
        return GOGO_BASE_URL
    }

    @Provides
    @Singleton
    fun provideApolloClient(
        @Apollo okHttpClient: OkHttpClient
    ): ApolloClient = ApolloClient.Builder()
        .serverUrl(ANILIST_API_URL)
        .okHttpClient(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiServiceSingleton(
        @Named("base-url") baseUrlProvider: Provider<String>,
        @RetrofitClient okHttpClient: OkHttpClient
    ) = ApiServiceSingleton(baseUrlProvider, okHttpClient)

    @Provides
    @Singleton
    fun provideAniListGraphQlClient(
        apolloClient: ApolloClient
    ): AniListGraphQlClient = AniListGraphQlClient(apolloClient)

    @Singleton
    @Provides
    fun provideAniListAuthService(retrofit: Retrofit): AniListAuthService {
        return retrofit.create(AniListAuthService::class.java)
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

    @Singleton
    @Provides
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Singleton
    @Provides
    fun provideFirebaseInstance(): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance()
    }

    @Provides
    @IntoMap
    @StringKey("GOGO_ANIME")
    fun provideGogoAnimeApiClient(client: GogoAnimeApiClient): BaseClient = client

    @Provides
    @IntoMap
    @StringKey("ENIME")
    fun provideEnimeClient(client: EnimeClient): BaseClient = client

    @Provides
    @IntoMap
    @StringKey("ENIME_PARSER")
    fun provideEnimeParser(parser: EnimeParser): BaseParser = parser

    @Provides
    @IntoMap
    @StringKey("GOGO_ANIME_PARSER")
    fun provideGoGoAnimeParser(parser: GoGoParser): BaseParser = parser
}

