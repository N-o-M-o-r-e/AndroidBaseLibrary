package com.no_more.base.abs

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.no_more.base.utils.classSimpleName
import com.no_more.base.utils.logI

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class AbsActivity<Binding : ViewBinding>(private val inflate: Inflate<Binding>) : AppCompatActivity() {

    private var _binding: Binding? = null
    protected val binding: Binding
        get() = _binding
            ?: throw IllegalStateException("Binding can only be used after onCreate() and before onDestroy()")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        logI("*${classSimpleName()}", "OnCreate Activity")

        _binding = inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        onActivityCreated()
    }

    protected fun registerOnBackPressed(callback: () -> Unit) {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    callback()
                }
            }
        )
    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())) {
                    view.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }

        return super.dispatchTouchEvent(motionEvent)
    }


    protected abstract fun onActivityCreated()

    override fun onStart() {
        super.onStart()
        logI("*${classSimpleName()}", "onStart Activity")
    }

    override fun onResume() {
        super.onResume()
        logI("*${classSimpleName()}", "onResume Activity")
    }

    override fun onPause() {
        super.onPause()
        logI("*${classSimpleName()}", "onPause Activity")
    }

    override fun onStop() {
        super.onStop()
        logI("*${classSimpleName()}", "onStop Activity")
    }

    override fun onDestroy() {
        super.onDestroy()
        logI("*${classSimpleName()}", "onDestroy Activity")
        runCatching {
            _binding?.root?.clearFocus()
            _binding?.root?.removeCallbacks(null)
        }
        _binding = null
    }
}