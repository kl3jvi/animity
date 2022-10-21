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
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeService
import com.kl3jvi.animity.data.network.anime_service.YugenApiClient
import com.kl3jvi.animity.data.network.anime_service.YugenService
import com.kl3jvi.animity.data.network.interceptor.HeaderInterceptor
import com.kl3jvi.animity.domain.repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.utils.Apollo
import com.kl3jvi.animity.utils.Constants.Companion.ANILIST_API_URL
import com.kl3jvi.animity.utils.Constants.Companion.GOGO_BASE_URL
import com.kl3jvi.animity.utils.RetrofitClient
import com.kl3jvi.animity.utils.addChuckerOnDebug
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
        localStorage: PersistenceRepository,
        loginRepository: LoginRepository,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addChuckerOnDebug(chuckerInterceptor)
            .addInterceptor(HeaderInterceptor(loginRepository, localStorage))
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
        @RetrofitClient okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(GOGO_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApolloClient(
        @Apollo okHttpClient: OkHttpClient
    ): ApolloClient = ApolloClient.Builder()
        .serverUrl(ANILIST_API_URL)
        .okHttpClient(okHttpClient)
        .build()

    /**
     * > It takes a Retrofit object as an argument and returns an AnimeService object
     *
     * @param retrofit Retrofit - The Retrofit instance that will be used to create the service.
     * @return A Retrofit object.
     */
    @Singleton
    @Provides
    fun provideGogoAnimeService(retrofit: Retrofit): GogoAnimeService {
        return retrofit.create(GogoAnimeService::class.java)
    }

    @Provides
    @Singleton
    fun provideGogoAnimeApiClient(
        gogoAnimeService: GogoAnimeService
    ): GogoAnimeApiClient {
        return GogoAnimeApiClient(gogoAnimeService)
    }

    @Singleton
    @Provides
    fun provideNineAnimeService(retrofit: Retrofit): YugenService {
        return retrofit.create(YugenService::class.java)
    }

    @Provides
    @Singleton
    fun provideNineAnimeApiClient(
        yugenService: YugenService
    ): YugenApiClient = YugenApiClient(yugenService)

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
}
