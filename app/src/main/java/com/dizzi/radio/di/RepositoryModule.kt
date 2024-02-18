package com.dizzi.radio.di

import com.dizzi.radio.data.repository.RadioRepository
import com.dizzi.radio.data.repository.RadioRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindUserRepository(youTubeRepositoryImpl: RadioRepositoryImpl): RadioRepository
}