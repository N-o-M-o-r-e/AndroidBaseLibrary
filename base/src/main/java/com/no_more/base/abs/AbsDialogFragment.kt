package com.no_more.base.abs

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.no_more.base.utils.classSimpleName
import com.no_more.base.utils.logI

abstract class AbsDialogFragment<BD : ViewBinding>(
    layoutId: Int,
    private val bindViewBinding: BindViewBinding<BD>
) : DialogFragment(layoutId) {

    private var _binding: BD? = null

    protected val binding: BD
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logI("*${requireActivity().classSimpleName()}", "OnCreate DialogFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logI("*${requireActivity().classSimpleName()}", "OnCreateView DialogFragment")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logI("*${requireActivity().classSimpleName()}", "OnViewCreated DialogFragment")
        _binding = bindViewBinding(view)
        fragmentViewOnReady()
    }

    protected abstract fun fragmentViewOnReady()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logI("*${requireActivity().classSimpleName()}", "OnAttach DialogFragment")
    }

    override fun onStart() {
        super.onStart();
        logI("*${requireActivity().classSimpleName()}", "OnStart DialogFragment")
    }

    override fun onResume() {
        super.onResume();
        logI("*${requireActivity().classSimpleName()}", "OnResume DialogFragment")
    }

    override fun onPause() {
        super.onPause();
        logI("*${requireActivity().classSimpleName()}", "OnPause DialogFragment")
    }

    override fun onStop() {
        super.onStop();
        logI("*${requireActivity().classSimpleName()}", "OnStop DialogFragment")
    }

    override fun onDetach() {
        super.onDetach();
        logI("*${requireActivity().classSimpleName()}", "OnDetach DialogFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView();
        logI("*${requireActivity().classSimpleName()}", "OnDestroyView DialogFragment")
        runCatching {
            _binding?.root?.clearFocus()
            _binding?.root?.removeCallbacks(null)
        }
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy();
        logI("*${requireActivity().classSimpleName()}", "OnDestroy DialogFragment")
    }

    override fun onCancel(dialog: DialogInterface) {
        logI("*${requireActivity().classSimpleName()}", "OnCancel DialogFragment")
        onClearInstance()
        super.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        logI("*${requireActivity().classSimpleName()}", "OnDismiss DialogFragment")
        onClearInstance()
        super.onDismiss(dialog)
    }

    protected open fun onClearInstance() {

    }
}
