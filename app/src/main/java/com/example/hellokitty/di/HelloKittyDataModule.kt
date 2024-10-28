package com.example.hellokitty.di

import com.example.hellokitty.data.network.HelloKittyApiService
import com.example.hellokitty.domain.repository.BreedDataRepository
import com.example.hellokitty.domain.repository.BreedDataRepositoryImpl
import com.example.hellokitty.domain.repository.HelloKittyRepository
import com.example.hellokitty.domain.repository.HelloKittyRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HelloKittyDataModule {

    @Provides
    @Singleton
    fun provideHelloKittyRepository(api: HelloKittyApiService): HelloKittyRepository {
        return HelloKittyRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideBreedDataRepository(): BreedDataRepository {
        return BreedDataRepositoryImpl()
    }
}