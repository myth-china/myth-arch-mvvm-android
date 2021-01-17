package com.myth.arch.mvvm3

import androidx.core.util.Pools
import java.io.Closeable

abstract class MythCommonProviderMaker<K, T> {

    private val providerMap = LinkedHashMap<K, T>()
    private val pool = Pools.SynchronizedPool<T>(10)

    fun getProvider(key: K): T {
        var provider = providerMap[key]
        if (provider == null) {
            provider = pool.acquire() ?: instance()
        }
        providerMap[key] = provider!!
        return provider
    }

    fun removeProvider(key: K) {
        val provider = providerMap.remove(key)
        if (provider is Closeable) {
            provider.close()
        }
        provider?.let { pool.release(it) }
    }

    abstract fun instance(): T
}