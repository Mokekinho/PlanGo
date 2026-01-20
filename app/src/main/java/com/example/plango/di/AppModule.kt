package com.example.plango.di

import android.content.Context
import androidx.room.Room
import com.example.plango.database.AppDatabase
import com.example.plango.database.TravelDao
import com.example.plango.database.TravelRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val AppModule.applicationContext: Context

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRepository(
        appDataBase: AppDatabase
    ): TravelRepository {
        return TravelRepository(appDataBase.travelDao())
    }

    @Singleton
    @Provides
    fun provideAppDataBase(): AppDatabase{
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "travel_database"
        ).build()
    }
}