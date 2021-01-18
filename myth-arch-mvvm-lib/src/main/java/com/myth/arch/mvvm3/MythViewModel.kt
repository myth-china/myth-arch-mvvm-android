package com.myth.arch.mvvm3

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myth.arch.event.Event
import com.myth.arch.event.EventObserver
import com.myth.arch.logger.MythLogger
import java.io.Closeable

object MythViewModelProviderFactory : MythCommonProviderMaker<MythViewModel, MythViewModelProvider>() {

    override fun instance(): MythViewModelProvider {
        return MythViewModelProvider()
    }
}

interface MythViewModel : Closeable {

    val tag: String
        get() = "MythViewModel"

    fun getProvider(): MythViewModelProvider {
        val provider = MythViewModelProviderFactory.getProvider(this)
        MythLogger.d(tag, "getProvider ${provider.hashCode()}")
        return provider
    }

    fun setupProvider() {
        if (this is ViewModel) {
            /**
             * Commit this to [ViewModel.mBagOfTags] for recycle with [ViewModel.clear]
             */
            val method = ViewModel::class.java.getDeclaredMethod("setTagIfAbsent", String::class.java, Object::class.java)
            method.isAccessible = true
            method.invoke(this, this.javaClass.canonicalName, this)

            MythLogger.d(tag, "${this.javaClass.simpleName}.setupProvider()")
        }
    }

    fun internalConfig(view: MythView) {}

    fun getData(): Bundle {
        return getProvider().data
    }

    /**
     * For easy install ext.
     *
     * @param name The name of the ext.
     * @param data Data will be send.
     * @param post Use post mode?
     * @param action The action will be done.
     */
    fun <T> easyUseExt(
            name: String,
            data: T,
            post: Boolean = true,
            action: (MythView, T?) -> Unit
    ) {
        var liveData = getProvider().getExtData<Event<T>>(name)

        if (liveData == null) {

            MythLogger.d(tag, "easyUseExt $name|${data.toString()}|${action}")

            liveData = MutableLiveData()

            getProvider().installExt(name, liveData) { view, internalLiveData ->
                internalLiveData.observe(view.getLifeCycleOwner(), EventObserver {
                    action(view, it)
                })
            }
        }

        if (post) {
            liveData.postValue(Event(data))
        } else {
            liveData.value = Event(data)
        }
    }

    /**
     * Remove provider from [MythViewModelProviderFactory] by [ViewModel.mBagOfTags] & [ViewModel.clear].
     */
    override fun close() {
        MythLogger.d(tag, "${this.javaClass.simpleName}.close()")
        MythViewModelProviderFactory.removeProvider(this)
    }
}