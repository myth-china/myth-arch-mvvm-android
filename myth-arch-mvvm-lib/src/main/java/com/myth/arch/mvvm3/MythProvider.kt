package com.myth.arch.mvvm3

open class MythProvider {

    private val objMap = HashMap<Int, HashMap<String, Any>>()

    fun hasObj(hashCode: Int): Boolean {
        return objMap.containsKey(hashCode)
    }

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

    private fun getCounter(hashCode: Int): Int {
        return getObj(hashCode, "counter") ?: 0
    }

    /**
     * 引用计数+1
     *
     * @return 是否首次+1
     */
    fun counterIncrease(hashCode: Int): Boolean {
        var counter = getCounter(hashCode)
        val flag = counter == 0
        putObj(hashCode, "counter", ++counter)
        return flag
    }

    /**
     * 引用计数-1，并判断是否需要回收内存
     */
    fun counterDecrease(hashCode: Int) {
        if (!hasObj(hashCode)) {
            return
        }

        var counter = getCounter(hashCode)
        counter -= 1

        if (counter < 1) {
            objMap.remove(hashCode)
        }
    }
}