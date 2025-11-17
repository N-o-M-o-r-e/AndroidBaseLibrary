package com.no_more.base.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * Enhanced Logging Extensions
 */

/**
 * Show Log with enhanced formatting
 */

object BuildType {
    var isDebug: Boolean = true
}

fun logI(tag: String, data: String) {
    if (BuildType.isDebug) {
        Log.i(tag, "ℹ️: $data")
    }
}

fun logD(tag: String, data: String) {
    if (BuildType.isDebug) {
        Log.d(tag, "✅: $data")
    }
}

fun logV(tag: String, data: String) {
    if (BuildType.isDebug) {
        Log.v(tag, "🔍: $data")
    }
}

fun logW(tag: String, data: String) {
    if (BuildType.isDebug) {
        Log.w(tag, "⚠️: $data")
    }
}

fun logE(tag: String, data: String) {
    if (BuildType.isDebug) {
        Log.e(tag, "❌: $data")
    }
}

/**
 * Enhanced logging with throwable support
 */
fun logE(tag: String, data: String, throwable: Throwable?) {
    if (BuildType.isDebug) {
        Log.e(tag, "❌: $data", throwable)
    }
}

fun logW(tag: String, data: String, throwable: Throwable?) {
    if (BuildType.isDebug) {
        Log.w(tag, "⚠️: $data", throwable)
    }
}


fun Context.classSimpleName(): String {
    return this.javaClass.simpleName
}

/**
 * Logging with automatic tag based on class name
 */
inline fun <reified T> T.logI(data: String) {
    logI(T::class.java.simpleName, data)
}

inline fun <reified T> T.logD(data: String) {
    logD(T::class.java.simpleName, data)
}

inline fun <reified T> T.logV(data: String) {
    logV(T::class.java.simpleName, data)
}

inline fun <reified T> T.logW(data: String) {
    logW(T::class.java.simpleName, data)
}

inline fun <reified T> T.logE(data: String) {
    logE(T::class.java.simpleName, data)
}

inline fun <reified T> T.logE(data: String, throwable: Throwable?) {
    logE(T::class.java.simpleName, data, throwable)
}

/**
 * Performance logging
 */
inline fun <T> measureTimeAndLog(tag: String, operation: String, block: () -> T): T {
    val startTime = System.currentTimeMillis()
    val result = block()
    val endTime = System.currentTimeMillis()
    logD(tag, "$operation took ${endTime - startTime}ms")
    return result
}

/**
 * Extension function to show a toast message.
 * Can accept either a String or a String resource ID.
 */
fun Context.toastMessage(message: Any, duration: Int = Toast.LENGTH_SHORT) {
    when (message) {
        is String -> Toast.makeText(this, message, duration).show()
        is Int -> Toast.makeText(this, message, duration).show()
    }
}




