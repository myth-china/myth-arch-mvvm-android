/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.myth.arch.event

import androidx.lifecycle.Observer


/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class Event<out T>(private val content: T) {

    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

open class Event2<out T, P>(private val content: T?, private val content2: P?) {

    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set // Allow external read but not write
    var hasBeenHandled2 = false
        private set // Allow external read but not write

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun getContent2IfNotHandled(): P? {
        return if (hasBeenHandled2) {
            null
        } else {
            hasBeenHandled2 = true
            content2
        }
    }

    fun peekContent(): T? = content

    fun peekContent2(): P? = content2
}


open class Event3<out T, P, O>(private val content: T?, private val content2: P?, private val content3: O?) {

    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set // Allow external read but not write
    var hasBeenHandled2 = false
        private set // Allow external read but not write
    var hasBeenHandled3 = false
        private set // Allow external read but not write

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun getContent2IfNotHandled(): P? {
        return if (hasBeenHandled2) {
            null
        } else {
            hasBeenHandled2 = true
            content2
        }
    }

    fun getContent3IfNotHandled(): O? {
        return if (hasBeenHandled3) {
            null
        } else {
            hasBeenHandled3 = true
            content3
        }
    }

    fun peekContent(): T? = content
    fun peekContent2(): P? = content2
    fun peekContent3(): O? = content3
}

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>?> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}

class EventObserver2<T, P>(private val onEventUnhandledContent: (T?, P?) -> Unit) : Observer<Event2<T?, P?>?> {
    override fun onChanged(event: Event2<T?, P?>?) {
        val content = event?.getContentIfNotHandled()
        val content2 = event?.getContent2IfNotHandled()
        onEventUnhandledContent(content, content2)
    }
}

class EventObserver3<T, P, O>(private val onEventUnhandledContent: (T?, P?, O?) -> Unit) : Observer<Event3<T?, P?, O?>?> {
    override fun onChanged(event: Event3<T?, P?, O?>?) {
        val content = event?.getContentIfNotHandled()
        val content2 = event?.getContent2IfNotHandled()
        val content3 = event?.getContent3IfNotHandled()
        onEventUnhandledContent(content, content2, content3)
    }
}
