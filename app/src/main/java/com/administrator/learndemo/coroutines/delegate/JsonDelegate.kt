package com.administrator.learndemo.coroutines.delegate

import android.util.Log
import org.json.JSONObject
import kotlin.reflect.KProperty

class JsonDelegate(jsonStr: String) {
    val jsonObject: JSONObject = JSONObject(jsonStr)

    public fun <T> delegate(defaultValue: T, key: String = ""): Delegate<T> {
        return Delegate(jsonObject, key, defaultValue)
    }

    public override fun toString(): String {
        return jsonObject.toString()
    }

    class Delegate<T>(
            var jsonObject: JSONObject,
            var key: String,
            val defaultValue: T
    ) {

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            val key = getKey(property)

            Log.i("KOTLIN_DELEGATE", "setValue $key")
            jsonObject.put(key, value)
        }

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            val key = getKey(property)
            if (jsonObject.has(key)) {
                val value = jsonObject.get(getKey(property))

                Log.i("KOTLIN_DELEGATE", "getValue $key  defaultValue  $defaultValue")
                return value as T
            } else {
                return defaultValue
            }
        }

        protected fun getKey(property: KProperty<*>): String {
            if (!key.isEmpty()) {
                return key
            }

            return property.name
        }
    }
}