package com.kl3jvi.animity.di

import com.kl3jvi.animity.data.network.interceptor.AuthenticationInterceptor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor


@InstallIn(SingletonComponent::class)
@Module
abstract class AuthInterceptorModule {

    /**
     * [StackOverflowError] https://stackoverflow.com/questions/63115678/how-to-add-retrofit-basic-auth-to-network-module-with-dagger-hilt
     */
    @Binds
    abstract fun bindAuthInterceptor(basicAuthInterceptor: AuthenticationInterceptor): Interceptor
}