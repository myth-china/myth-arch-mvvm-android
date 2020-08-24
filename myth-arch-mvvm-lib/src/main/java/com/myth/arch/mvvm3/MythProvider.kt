package com.myth.arch.mvvm3

open class MythProvider {

    private val objMap = HashMap<Int, HashMap<String, Any>>()

    fun <T> putObj(hashCode: Int, name: String, obj: T) {
        if (!objMap.containsKey(hashCode)) {
            objMap[hashCode] = HashMap()
        }
        objMap[hashCode]?.set(name, obj as Any)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getObj(hashCode: Int, name: String): T {
        return objMap[hashCode]?.get(name) as T
    }
}