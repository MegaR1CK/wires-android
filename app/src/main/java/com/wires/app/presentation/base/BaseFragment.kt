package com.wires.app.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wires.app.managers.BottomNavigationViewManager
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    open val showBottomNavigationView: Boolean
        get() = (parentFragment as? BaseFragment)?.showBottomNavigationView ?: false

    private var bottomNavigationViewManager: BottomNavigationViewManager? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationViewManager?.setNavigationViewVisibility(showBottomNavigationView)
        onSetupLayout(savedInstanceState)
        onBindViewModel()
    }

    abstract fun callOperations()

    abstract fun onSetupLayout(savedInstanceState: Bundle?)

    abstract fun onBindViewModel()

    protected infix fun <T> LiveData<T>.observe(block: (T) -> Unit) {
        observe(this@BaseFragment.viewLifecycleOwner) { block.invoke(it) }
    }

    protected fun setBottomNavigationViewVisibility(isVisible: Boolean) {
        bottomNavigationViewManager?.setNavigationViewVisibility(isVisible)
    }

    @MainThread
    inline fun <reified VM : ViewModel> Fragment.appViewModels() =
        createViewModelLazy(VM::class, { this.viewModelStore }, { viewModelFactory })
}
