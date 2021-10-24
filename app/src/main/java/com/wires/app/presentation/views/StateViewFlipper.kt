package com.wires.app.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ViewFlipper
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.wires.app.data.LoadableResult
import com.wires.app.data.remote.NetworkError
import com.wires.app.data.remote.ParsedError


class StateViewFlipper(context: Context, attrs: AttributeSet? = null) : ViewFlipper(context, attrs) {

    enum class State(val displayedChild: Int) {
        LOADING(0),
        ERROR(1),
        DATA(2),
        CUSTOM(3)
    }

    private var state = State.LOADING

//    private var loadingView: View
//    private val errorView: View

    private val disabledStates = mutableListOf<State>()

    init {
        val layoutInflater = LayoutInflater.from(context)
        val layoutResProvider = LayoutResProvider(context, attrs)

//        loadingView = layoutInflater.inflate(layoutResProvider.loadingRes, this, false)
//        addView(loadingView)

//        errorView = layoutInflater.inflate(layoutResProvider.errorRes, this, false)
//        addView(errorView)
    }

    fun <T> setStateFromResult(loadableResult: LoadableResult<T>) {
        when (loadableResult) {
            is LoadableResult.Loading -> setStateLoading()
            is LoadableResult.Success -> setStateData()
            is LoadableResult.Failure -> setStateError(loadableResult.error)
        }
    }

    fun setRetryMethod(retry: () -> Unit) {
//        errorView.buttonError.setOnClickListener { retry.invoke() }
    }

    fun setCustomState() {
        changeState(State.CUSTOM)
    }

    fun currentState() = state

    /** Метод деактивирует определенное состояние и не обрабатывает его в changeState() */
    fun disableState(vararg states: State) {
        for (state in states) {
            if (stateIsDisabled(state)) continue
            disabledStates.add(state)
            getChildAt(state.displayedChild)?.isVisible = false
        }
    }

    fun setLoadingView(@LayoutRes layout: Int) {
//        removeView(loadingView)
//        loadingView = LayoutInflater.from(context).inflate(layout, this, false)
//        addView(loadingView, 0)
        changeState(state)
    }

    private fun changeState(newState: State) {
        if (stateIsDisabled(newState)) return
        if (state != newState || displayedChild != newState.displayedChild) {
            state = newState
            displayedChild = newState.displayedChild
        }
    }

    private fun setStateLoading() {
        changeState(State.LOADING)
    }

    private fun setStateError(error: ParsedError) {
        changeState(State.ERROR)

        when (error) {
            is NetworkError -> setStateNetworkError()
            else -> setGeneralError()
        }
    }

    private fun setStateData() {
        changeState(State.DATA)
    }

    /**
     * Ошибка "Что-то с интернетом"
     */
    private fun setStateNetworkError() {
//        setErrorStateContent(
//            titleRes = R.string.error_no_network_title,
//            descriptionRes = R.string.error_no_network_description,
//            errorImageRes = R.drawable.ic_no_internet,
//        )
    }

    /**
     * Ошибка "Что-то не так"
     */
    private fun setGeneralError() {
//        setErrorStateContent(
//            titleRes = R.string.error_something_wrong_title,
//            descriptionRes = R.string.error_something_wrong_description,
//            errorImageRes = R.drawable.ic_something_wrong,
//        )
    }

    private fun setErrorStateContent(@StringRes titleRes: Int, @StringRes descriptionRes: Int, @DrawableRes errorImageRes: Int) {
//        errorView.textErrorTitle?.setText(titleRes)
//        errorView.textErrorComment?.setText(descriptionRes)
//        errorView.imageViewError?.setImageResource(errorImageRes)
    }

    fun setErrorImageVisibility(isVisible: Boolean) {
//        errorView.imageViewError.isVisible = isVisible
    }

    private fun stateIsDisabled(state: State): Boolean {
        return disabledStates.contains(state)
    }

    private class LayoutResProvider(context: Context, attrs: AttributeSet?) {

        companion object {
//            @LayoutRes
//            val DEFAULT_ERROR_LAYOUT = R.layout.view_state_error
//
//            @LayoutRes
//            val DEFAULT_LOADING_LAYOUT = R.layout.view_state_loading
        }

//        @LayoutRes val loadingRes: Int = DEFAULT_LOADING_LAYOUT
//        @LayoutRes val errorRes: Int = DEFAULT_ERROR_LAYOUT
    }
}
