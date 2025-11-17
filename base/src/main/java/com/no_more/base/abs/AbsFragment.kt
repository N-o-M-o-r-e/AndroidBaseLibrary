package com.no_more.base.abs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.no_more.base.utils.classSimpleName
import com.no_more.base.utils.launchInStarted
import com.no_more.base.utils.logI

typealias InflateBinding<BD> = (LayoutInflater) -> BD

typealias BindViewBinding<BD> = (View) -> BD

abstract class AbsFragment<BD : ViewBinding>(
    layoutId: Int,
    private val bindViewBinding: BindViewBinding<BD>
) : Fragment(layoutId) {

    private var _binding: BD? = null

    protected val binding: BD
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logI("*${requireActivity().classSimpleName()}", "OnCreate Fragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logI("*${requireActivity().classSimpleName()}", "onCreateView Fragment")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = bindViewBinding(view)
        logI("*${requireActivity().classSimpleName()}", "onViewCreated Fragment")
        fragmentViewOnReady()
    }

    protected abstract fun fragmentViewOnReady()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logI("*${requireActivity().classSimpleName()}", "onAttach Fragment")
    }

    override fun onStart() {
        super.onStart()
        logI("*${requireActivity().classSimpleName()}", "onStart Fragment")
    }

    override fun onResume() {
        super.onResume()
        logI("*${requireActivity().classSimpleName()}", "onResume Fragment")
    }

    override fun onPause() {
        super.onPause()
        logI("*${requireActivity().classSimpleName()}", "onPause Fragment")
    }

    override fun onStop() {
        super.onStop()
        logI("*${requireActivity().classSimpleName()}", "onStop Fragment")
    }

    override fun onDetach() {
        super.onDetach()
        logI("*${requireActivity().classSimpleName()}", "onDetach Fragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logI("*${requireActivity().classSimpleName()}", "onDestroyView Fragment")
        runCatching {
            _binding?.root?.clearFocus()
            _binding?.root?.removeCallbacks(null)
        }
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        logI("*${requireActivity().classSimpleName()}", "onDestroy Fragment")
    }


    protected fun registerOnBackPressed(callback: () -> Unit) {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    this@AbsFragment.launchInStarted { callback() }
                }
            })
    }


}