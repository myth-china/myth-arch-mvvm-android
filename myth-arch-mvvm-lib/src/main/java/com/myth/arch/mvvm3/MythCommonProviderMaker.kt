package com.myth.arch.mvvm3

import androidx.core.util.Pools
import com.myth.arch.logger.MythLogger
import java.io.Closeable

abstract class MythCommonProviderMaker<K, T> {

    private val tag = "MythCommonProviderMaker"

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
        provider?.let {
            if (!pool.release(it)) {
                MythLogger.d(tag, "Pool is full!")
            }
        }
    }

    abstract fun instance(): T
}