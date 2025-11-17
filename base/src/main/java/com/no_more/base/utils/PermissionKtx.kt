package com.no_more.base.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.isPermissionsGranted(permissions: Array<String>): Boolean {
    return permissions.takeIf { it.isNotEmpty() }?.all { this.isPermissionGranted(it) } == true
}

fun Context.isPermissionsGranted(permissions: List<String>): Boolean {
    return isPermissionsGranted(permissions.toTypedArray())
}