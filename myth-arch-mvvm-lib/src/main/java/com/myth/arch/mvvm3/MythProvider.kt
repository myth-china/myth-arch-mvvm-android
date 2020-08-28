package com.myth.arch.mvvm3

open class MythProvider {

    private val varMap = HashMap<Int, HashMap<String, Any>>()

    /**
     * Has root object in the map.
     */
    private fun hasMemberVar(hashCode: Int): Boolean {
        return varMap.containsKey(hashCode)
    }

    /**
     * Has member object in root object.
     */
    fun hasMemberVar(hashCode: Int, name: String): Boolean {
        return hasMemberVar(hashCode) && varMap[hashCode]?.containsKey(name) == true
    }

    /**
     * Put a member object into root object.
     */
    fun <T> putMemberVar(hashCode: Int, name: String, obj: T) {
        if (!varMap.containsKey(hashCode)) {
            varMap[hashCode] = HashMap()
        }
        varMap[hashCode]?.set(name, obj as Any)
    }

    /**
     * Get the member object in root object.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getMemberVar(hashCode: Int, name: String): T {
        return varMap[hashCode]?.get(name) as T
    }

    private fun getCounter(hashCode: Int): Int {
        return getMemberVar(hashCode, "counter") ?: 0
    }

    /**
     * Reference counter +1.
     *
     * @return Is first time +1.
     */
    fun counterIncrease(hashCode: Int): Boolean {
        var counter = getCounter(hashCode)
        val flag = counter == 0
        putMemberVar(hashCode, "counter", ++counter)
        return flag
    }

    /**
     * Reference counter -1，determine if we can release the object.
     */
    fun counterDecrease(hashCode: Int) {
        if (!hasMemberVar(hashCode)) {
            return
        }

        var counter = getCounter(hashCode)
        counter -= 1

        if (counter < 1) {
            varMap.remove(hashCode)
        }
    }
}