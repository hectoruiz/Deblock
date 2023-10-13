package com.hectoruiz.deblock.di

import com.hectoruiz.data.repositories.CryptoRepositoryImpl
import com.hectoruiz.domain.repositories.CryptoRepository
import com.hectoruiz.domain.usecases.GetExchangeRateUseCase
import com.hectoruiz.domain.usecases.GetFeeUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CryptoModule {

    @Singleton
    @Binds
    abstract fun bindCryptoRepository(cryptoRepositoryImpl: CryptoRepositoryImpl): CryptoRepository

    companion object {

        @Provides
        fun provideGetExchangeRateUseCase(cryptoRepository: CryptoRepository): GetExchangeRateUseCase {
            return GetExchangeRateUseCase(cryptoRepository)
        }

        @Provides
        fun provideGetFeeUseCase(cryptoRepository: CryptoRepository): GetFeeUseCase {
            return GetFeeUseCase(cryptoRepository)
        }
    }
}
