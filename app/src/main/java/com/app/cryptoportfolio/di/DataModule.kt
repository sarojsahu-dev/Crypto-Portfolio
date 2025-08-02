package com.app.cryptoportfolio.di


import com.app.cryptoportfolio.data.repository.CryptoRepositoryImpl
import com.app.cryptoportfolio.data.source.MockDataSource
import com.app.cryptoportfolio.domain.repository.CryptoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindCryptoRepository(
        cryptoRepositoryImpl: CryptoRepositoryImpl
    ): CryptoRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMockDataSource(): MockDataSource {
        return MockDataSource()
    }

}