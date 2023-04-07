package com.kl3jvi.animity.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.kl3jvi.animity.persistence.AppDatabase
import com.kl3jvi.animity.persistence.EpisodeDao
import com.kl3jvi.animity.settings.Settings
import com.kl3jvi.animity.utils.Constants.Companion.DATABASE_NAME
import com.kl3jvi.animity.utils.Constants.Companion.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PersistenceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        application: Application
    ): AppDatabase = Room
        .databaseBuilder(application, AppDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
//            .addTypeConverter(typeResponseConverter)
        .build()

    @Provides
    @Singleton
    fun providesEpisodeDao(appDatabase: AppDatabase): EpisodeDao = appDatabase.episodeDao()

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideSettings(
        @ApplicationContext context: Context,
        preferences: SharedPreferences
    ) = Settings(context, preferences)
}
