package com.no_more.base.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.google.android.material.color.MaterialColors


@ColorInt
fun Context.colorAttr(
    @AttrRes colorAttributeResId: Int, @ColorInt defaultValue: Int = Color.BLACK,
): Int {
    return MaterialColors.getColor(this, colorAttributeResId, defaultValue)
}

@ColorInt
fun View.colorAttr(
    @AttrRes colorAttributeResId: Int, @ColorInt defaultValue: Int = Color.BLACK,
): Int {
    return MaterialColors.getColor(this, colorAttributeResId, defaultValue)
}

fun Context.colorAttrStateList(
    @AttrRes colorAttributeResId: Int,
    defaultValue: ColorStateList = ColorStateList.valueOf(Color.BLACK),
): ColorStateList {
    return MaterialColors.getColorStateList(this, colorAttributeResId, defaultValue)
}