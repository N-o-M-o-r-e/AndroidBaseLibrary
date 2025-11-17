package com.no_more.lib

import android.graphics.Color
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding
import com.no_more.base.abs.AbsActivity
import com.no_more.base.abs.Inflate
import com.no_more.base.abs.InflateBinding

abstract class BaseActivity<Binding : ViewBinding>(inflate: Inflate<Binding>) :
    AbsActivity<Binding>(inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated() {

    }
}
