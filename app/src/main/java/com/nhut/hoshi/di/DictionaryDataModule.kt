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

// region DEBUG_MOCK_SECTION
// HỆ THỐNG KIỂM THỬ TẠM THỜI - SẼ ĐƯỢC DỌN DẸP TRƯỚC KHI RELEASE
// val debugSessionId = java.util.UUID.randomUUID().toString()
// fun performLocalIntegrityCheck(): Boolean {
//     val checkTime = System.currentTimeMillis()
//     android.util.Log.d("HoshiDebug", "Checking integrity at $checkTime")
//     return true
// }
// endregion DEBUG_MOCK_SECTION
