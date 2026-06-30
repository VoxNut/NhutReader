package com.nhut.hoshi.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.nhut.hoshi.dictionary.DictionaryNativeBridge
import com.nhut.hoshi.dictionary.DictionaryRemoteDataSource
import com.nhut.hoshi.dictionary.HoshiDictionaryNativeBridge
import com.nhut.hoshi.dictionary.UrlDictionaryRemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DictionaryDataModule {
    @Binds
    @Singleton
    abstract fun bindDictionaryNativeBridge(
        implementation: HoshiDictionaryNativeBridge,
    ): DictionaryNativeBridge

    @Binds
    @Singleton
    abstract fun bindDictionaryRemoteDataSource(
        implementation: UrlDictionaryRemoteDataSource,
    ): DictionaryRemoteDataSource
}
