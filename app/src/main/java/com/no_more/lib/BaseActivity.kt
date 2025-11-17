package com.no_more.lib

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.no_more.base.abs.AbsActivity
import com.no_more.base.abs.Inflate

abstract class BaseActivity<Binding : ViewBinding>(inflate: Inflate<Binding>) :
    AbsActivity<Binding>(inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated() {

    }
}
