package com.myth.arch.mvvm3

class MythViewProvider {

    private val varMap = HashMap<String, Any>()

    fun putVar(name: String, obj: Any) {
        varMap[name] = obj
    }

    fun removeVar(name: String) {
        varMap.remove(name)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getVar(name: String): T? {
        return varMap[name] as T?
    }
}