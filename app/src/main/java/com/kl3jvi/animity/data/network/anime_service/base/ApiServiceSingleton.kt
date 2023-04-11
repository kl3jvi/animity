package com.kl3jvi.animity.data.network.anime_service.base

import com.kl3jvi.animity.settings.Settings
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * A singleton class that provides an instance of the API service using Retrofit library.
 * @param baseUrlProvider Provider for the base URL of the API.
 * @param okHttpClient Instance of OkHttpClient to be used for the API requests.
 */
@Singleton
class ApiServiceSingleton @Inject constructor(
    baseUrlProvider: Provider<String>,
    private val okHttpClient: OkHttpClient,
    private val settings: Settings
) {

    private var retrofit: Retrofit = createRetrofit(baseUrlProvider.get())

    /**
     * Returns an instance of the API service interface for the given class type.
     * @param clazz Class of the API service interface.
     * @return An instance of the API service interface.
     */
    fun <T : Any> getApiService(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

    /**
     * Updates the base URL of the API and creates a new instance of Retrofit with the updated URL.
     * @param newBaseUrl The new base URL of the API.
     */
    fun updateBaseUrl(newBaseUrl: String) {
        retrofit = createRetrofit(newBaseUrl)
    }

    /**
     * Creates a new instance of Retrofit with the given base URL and OkHttpClient.
     * @param baseUrl The base URL of the API.
     * @return A new instance of Retrofit.
     */
    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
