package com.kl3jvi.animity.settings

import com.google.gson.Gson
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private class BooleanPreference(
    private val key: String,
    private val default: Boolean
) : ReadWriteProperty<PreferencesHolder, Boolean> {

    override fun getValue(thisRef: PreferencesHolder, property: KProperty<*>): Boolean =
        thisRef.preferences.getBoolean(key, default)

    override fun setValue(thisRef: PreferencesHolder, property: KProperty<*>, value: Boolean) =
        thisRef.preferences.edit().putBoolean(key, value).apply()
}

private class FloatPreference(
    private val key: String,
    private val default: Float
) : ReadWriteProperty<PreferencesHolder, Float> {

    override fun getValue(thisRef: PreferencesHolder, property: KProperty<*>): Float =
        thisRef.preferences.getFloat(key, default)

    override fun setValue(thisRef: PreferencesHolder, property: KProperty<*>, value: Float) =
        thisRef.preferences.edit().putFloat(key, value).apply()
}

private class IntPreference(
    private val key: String,
    private val default: Int
) : ReadWriteProperty<PreferencesHolder, Int> {

    override fun getValue(thisRef: PreferencesHolder, property: KProperty<*>): Int =
        thisRef.preferences.getInt(key, default)

    override fun setValue(thisRef: PreferencesHolder, property: KProperty<*>, value: Int) =
        thisRef.preferences.edit().putInt(key, value).apply()
}

private class LongPreference(
    private val key: String,
    private val default: Long
) : ReadWriteProperty<PreferencesHolder, Long> {

    override fun getValue(thisRef: PreferencesHolder, property: KProperty<*>): Long =
        thisRef.preferences.getLong(key, default)

    override fun setValue(thisRef: PreferencesHolder, property: KProperty<*>, value: Long) =
        thisRef.preferences.edit().putLong(key, value).apply()
}

private class StringPreference(
    private val key: String,
    private val default: String,
    private val persistDefaultIfNotExists: Boolean = false
) : ReadWriteProperty<PreferencesHolder, String> {

    override fun getValue(thisRef: PreferencesHolder, property: KProperty<*>): String {
        return thisRef.preferences.getString(key, null) ?: run {
            when (persistDefaultIfNotExists) {
                true -> {
                    thisRef.preferences.edit().putString(key, default).apply()
                    thisRef.preferences.getString(key, null)!!
                }

                false -> default
            }
        }
    }

    override fun setValue(thisRef: PreferencesHolder, property: KProperty<*>, value: String) =
        thisRef.preferences.edit().putString(key, value).apply()
}

private class StringSetPreference(
    private val key: String,
    private val default: Set<String>
) : ReadWriteProperty<PreferencesHolder, Set<String>> {

    override fun getValue(thisRef: PreferencesHolder, property: KProperty<*>): Set<String> =
        thisRef.preferences.getStringSet(key, default) ?: default

    override fun setValue(thisRef: PreferencesHolder, property: KProperty<*>, value: Set<String>) =
        thisRef.preferences.edit().putStringSet(key, value).apply()
}

private class EnumPreference<T : Enum<T>>(
    private val key: String,
    private val default: T
) : ReadWriteProperty<PreferencesHolder, T> {
    private val gson = Gson()
    private val enumClass = default.javaClass

    override fun getValue(thisRef: PreferencesHolder, property: KProperty<*>): T {
        return gson.fromJson(
            thisRef.preferences.getString(key, "GOGO_ANIME"),
            enumClass
        ) ?: default
    }

    override fun setValue(thisRef: PreferencesHolder, property: KProperty<*>, value: T) {
        val stringValue = gson.toJson(value)
        thisRef.preferences.edit().putString(key, stringValue).apply()
    }
}

fun booleanPreference(
    key: String,
    default: Boolean
): ReadWriteProperty<PreferencesHolder, Boolean> =
    BooleanPreference(key, default)

fun floatPreference(
    key: String,
    default: Float
): ReadWriteProperty<PreferencesHolder, Float> =
    FloatPreference(key, default)

fun intPreference(
    key: String,
    default: Int
): ReadWriteProperty<PreferencesHolder, Int> =
    IntPreference(key, default)

fun longPreference(
    key: String,
    default: Long
): ReadWriteProperty<PreferencesHolder, Long> =
    LongPreference(key, default)

fun stringPreference(
    key: String,
    default: String,
    persistDefaultIfNotExists: Boolean = false
): ReadWriteProperty<PreferencesHolder, String> =
    StringPreference(key, default, persistDefaultIfNotExists)

fun stringSetPreference(
    key: String,
    default: Set<String>
): ReadWriteProperty<PreferencesHolder, Set<String>> =
    StringSetPreference(key, default)

fun <T : Enum<T>> enumPreference(
    key: String,
    default: T
): ReadWriteProperty<PreferencesHolder, T> =
    EnumPreference(key, default)

inline fun <reified T> String?.fromJson(): T {
    return Gson().fromJson(this.orEmpty(), T::class.java)
}

inline fun <reified T> T?.toJson(): String? {
    return Gson().toJson(this)
}
