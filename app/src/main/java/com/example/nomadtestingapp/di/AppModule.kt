package com.example.nomadtestingapp.di

import com.example.nomadtestingapp.adapters.GalleryAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideGalleryAdapter() = GalleryAdapter()

}