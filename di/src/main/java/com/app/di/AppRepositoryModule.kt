package com.app.di

import com.app.data.repository.AppRepo
import com.app.data.repository.AppRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppRepositoryModule {
    @Binds
    abstract fun providesAppRepo(repo: AppRepoImpl): AppRepo
}