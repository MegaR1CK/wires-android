package com.wires.app.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.wires.app.managers.BottomNavigationViewManager
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    open val showBottomNavigationView: Boolean
        get() = (parentFragment as? BaseFragment<*>)?.showBottomNavigationView ?: false

    private var bottomNavigationViewManager: BottomNavigationViewManager? = null

    protected var binding: ViewBinding? = null

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        if (context is BottomNavigationViewManager) {
            bottomNavigationViewManager = context
        }
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callOperations()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationViewManager?.setNavigationViewVisibility(showBottomNavigationView)
        onSetupLayout(savedInstanceState)
        onBindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    abstract fun callOperations()

    abstract fun onSetupLayout(savedInstanceState: Bundle?)

    abstract fun onBindViewModel()

    protected infix fun <T> LiveData<T>.observe(block: (T) -> Unit) {
        observe(this@BaseFragment.viewLifecycleOwner, { block.invoke(it) })
    }

    @MainThread
    inline fun <reified VM : ViewModel> Fragment.appViewModels() =
        createViewModelLazy(VM::class, { this.viewModelStore }, { viewModelFactory })
}
