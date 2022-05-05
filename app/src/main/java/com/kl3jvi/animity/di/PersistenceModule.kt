package com.kl3jvi.animity.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.kl3jvi.animity.persistence.AnimeDao
import com.kl3jvi.animity.persistence.AppDatabase
import com.kl3jvi.animity.persistence.EpisodeDao
import com.kl3jvi.animity.utils.Constants.Companion.DATABASE_NAME
import com.kl3jvi.animity.utils.Constants.Companion.SHARED_PREFERENCES_NAME
import com.kl3jvi.animity.utils.Converters
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
        application: Application,
        typeResponseConverter: Converters
    ): AppDatabase {
        return Room
            .databaseBuilder(application, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .addTypeConverter(typeResponseConverter)
            .build()
    }

    @Provides
    @Singleton
    fun provideAnimesDao(appDatabase: AppDatabase): AnimeDao = appDatabase.animeDao()

    @Provides
    @Singleton
    fun providesEpisodeDao(appDatabase: AppDatabase): EpisodeDao = appDatabase.episodeDao()

    /**
     * "Provide a SharedPreferences object for the application context."
     *
     * The @ApplicationContext annotation tells Dagger that the context parameter is the application
     * context
     *
     * @param context Context - The context of the application.
     * @return SharedPreferences
     */
    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}
