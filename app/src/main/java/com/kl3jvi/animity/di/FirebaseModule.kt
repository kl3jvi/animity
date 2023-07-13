package com.kl3jvi.animity.di

import com.google.firebase.firestore.FirebaseFirestore
import com.kl3jvi.animity.data.network.anilist_service.ChatManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideChatManager(
        firestore: FirebaseFirestore
    ): ChatManager = ChatManager(firestore)

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}
