@file:Suppress("DEPRECATION")

package com.no_more.base.utils

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.Window
import android.view.WindowInsets
import androidx.annotation.AttrRes
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IntDef
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.internal.EdgeToEdgeUtils
import com.no_more.base.utils.ThemeOption.Companion.DARK
import com.no_more.base.utils.ThemeOption.Companion.DEFAULT
import com.no_more.base.utils.ThemeOption.Companion.LIGHT
import kotlin.apply
import kotlin.let
import kotlin.run
import kotlin.runCatching

@IntDef(LIGHT, DARK, DEFAULT)
@Retention
annotation class ThemeOption {
    companion object {
        const val LIGHT = 1
        const val DARK = 2
        const val DEFAULT = 3
    }
}

fun Activity.setNavigationBarColorDefault() {
    this.setNavigationBarColorAttr(R.attr.navigationBarColor)
}

fun Activity.setNavigationBarColorAttr(@AttrRes attrColor: Int) {
    this.setNavigationBarColorInt(this@setNavigationBarColorAttr.colorAttr(attrColor))
}

fun Activity.setNavigationBarColorRes(@ColorRes colorRes: Int) {
    this.setNavigationBarColorInt(ContextCompat.getColor(this, colorRes))
}

fun Activity.setNavigationBarColorInt(@ColorInt colorInt: Int) {
    this.window?.runCatching {
        this.navigationBarColor = colorInt
    }
}

fun Activity.setStatusBarLightText(isLight: Boolean, rootView: View? = null) {
    this.window?.runCatching {
        setStatusBarLightTextOldApi(this, isLight)
        setStatusBarLightTextNewApi1(this, isLight)
        rootView?.let {
            setStatusBarLightTextNewApi2(this, rootView, isLight)
        }
    }
}

private fun setStatusBarLightTextOldApi(window: Window, isLight: Boolean) {
    runCatching {
        val decorView = window.decorView
        decorView.systemUiVisibility =
            if (isLight) {
                decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
    }
}

private fun setStatusBarLightTextNewApi1(window: Window, isLightText: Boolean) {
    runCatching {
        ViewCompat.getWindowInsetsController(window.decorView)?.apply {
            // Light text == dark status bar
            isAppearanceLightStatusBars = !isLightText
        }
    }
}

private fun setStatusBarLightTextNewApi2(window: Window, rootView: View, isLightText: Boolean) {
    runCatching {
        WindowCompat.getInsetsController(window, rootView).isAppearanceLightStatusBars =
            !isLightText
    }
}

fun Activity.applyTheme(@ThemeOption selectedOption: Int) {
    runCatching {
        val nightMode = when (selectedOption) {
            LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}

@ThemeOption
fun Activity.currentTheme(): Int {
    return when (AppCompatDelegate.getDefaultNightMode()) {
        AppCompatDelegate.MODE_NIGHT_NO -> LIGHT
        AppCompatDelegate.MODE_NIGHT_YES -> DARK
        else -> DEFAULT
    }
}

@SuppressLint("RestrictedApi", "WrongConstant")
fun Window.applyWindowInsetsListener(
    callback: ((v: View, landScape: Boolean, left: Int, top: Int, right: Int, bottom: Int) -> Unit)
) {
    ViewCompat.setOnApplyWindowInsetsListener(this.decorView) { v, insets ->
        val left: Int
        val top: Int
        val right: Int
        val bottom: Int
        if (VERSION.SDK_INT >= VERSION_CODES.R) {
            left = insets.getInsets(WindowInsets.Type.systemBars()).left
            top = insets.getInsets(WindowInsets.Type.systemBars()).top
            right = insets.getInsets(WindowInsets.Type.systemBars()).right
            bottom = insets.getInsets(WindowInsets.Type.systemBars()).bottom
        } else {
            left = insets.stableInsetLeft
            top = 0
            right = insets.stableInsetRight
            bottom = insets.stableInsetBottom
        }
        callback.invoke(
            v,
            v.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE,
            left,
            top,
            right,
            bottom,
        )
        insets
    }
}

@SuppressLint("RestrictedApi")
fun Activity.applyWindowInsetsListener(
    callback: ((v: View, landScape: Boolean, left: Int, top: Int, right: Int, bottom: Int) -> Unit),
) {
    this.window?.applyWindowInsetsListener(callback = callback)
}

@SuppressLint("RestrictedApi")
fun Window.applyEdgeToEdge(
    enable: Boolean,
    callback: ((v: View, landScape: Boolean, left: Int, top: Int, right: Int, bottom: Int) -> Unit)? = null,
) {
    EdgeToEdgeUtils.applyEdgeToEdge(this, enable)
    callback?.run {
        applyWindowInsetsListener(this)
    }
}

@SuppressLint("RestrictedApi")
fun Activity.applyEdgeToEdge(
    enable: Boolean,
    callback: ((v: View, landScape: Boolean, left: Int, top: Int, right: Int, bottom: Int) -> Unit)? = null,
) {
    this.window?.applyEdgeToEdge(enable = enable, callback = callback)
}

fun BottomSheetDialog.applyEdgeToEdge(
    enable: Boolean,
    callback: ((v: View, landScape: Boolean, left: Int, top: Int, right: Int, bottom: Int) -> Unit)? = null,
) {
    this.window?.applyEdgeToEdge(enable = enable, callback = callback)
}

@ChecksSdkIntAtLeast(api = VERSION_CODES.Q)
fun defaultEdgeToEdge(): Boolean {
    return VERSION.SDK_INT >= VERSION_CODES.Q
}

fun Activity.showOrHideStatusBar(show: Boolean) {
    runCatching {
        if (show) {
            if (VERSION.SDK_INT >= VERSION_CODES.R) {
                this.window.insetsController?.apply {
                    this.show(WindowInsets.Type.statusBars())
                }
            } else {
                this.window.decorView.apply {
                    // Sau đó, để hiển thị lại:
                    val currentUiOptions = this.systemUiVisibility
                    val newUiOptions = currentUiOptions and View.SYSTEM_UI_FLAG_FULLSCREEN.inv()
                    this.systemUiVisibility = newUiOptions
                }
            }
        } else {
            if (VERSION.SDK_INT >= VERSION_CODES.R) {
                this.window.insetsController?.apply {
                    this.hide(WindowInsets.Type.statusBars())
                }
            } else {
                this.window.decorView.apply {
                    systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                }
            }
        }
    }
}




