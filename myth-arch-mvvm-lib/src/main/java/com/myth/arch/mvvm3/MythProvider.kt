package com.myth.arch.mvvm3

import com.myth.arch.log.MythLogger
import java.lang.ref.PhantomReference
import java.lang.ref.ReferenceQueue

open class MythProvider {

    private val varMap = HashMap<Int, HashMap<String, Any>>()
    private val objWeakRef = HashMap<Int, PhantomReference<Any>>()
    private val objNameMap = HashMap<Int, String>()
    private val referenceQueue = ReferenceQueue<Any>()

    fun addObj(obj: Any) {
        objWeakRef[obj.hashCode()] = PhantomReference(obj, referenceQueue)
        objNameMap[obj.hashCode()] = obj::class.java.name
    }

    fun checkObjGC() {
        objWeakRef.forEach {
            MythLogger.d(
                "MythProvider",
                "Object ${objNameMap[it.key]} with hashcode(${it.key}) ${if (it.value.isEnqueued) "has been gc" else "is using"}"
            )

            if (it.value.isEnqueued) {
                varMap.remove(it.key)
                objWeakRef.remove(it.key)
                objNameMap.remove(it.key)
            }
        }
    }

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
}