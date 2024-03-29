package com.wires.app.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.data.remote.NetworkError
import com.wires.app.data.remote.ParsedError

class StateViewFlipper(context: Context, attrs: AttributeSet? = null) : ViewFlipper(context, attrs) {

    enum class State(val displayedChild: Int) {
        LOADING(0),
        ERROR(1),
        DATA(2)
    }

    private var state = State.LOADING

    private var loadingView: View
    private lateinit var errorView: View

    private val buttonError: Button by lazy { errorView.findViewById(R.id.buttonError) }
    private val textErrorTitle: TextView? by lazy { errorView.findViewById(R.id.textErrorTitle) }
    private val textErrorDescription: TextView? by lazy { errorView.findViewById(R.id.textErrorComment) }
    private val imageViewError: ImageView? by lazy { errorView.findViewById(R.id.imageViewError) }

    private val disabledStates = mutableListOf<State>()

    init {
        val layoutInflater = LayoutInflater.from(context)
        val layoutResProvider = LayoutResProvider()

        loadingView = layoutInflater.inflate(layoutResProvider.loadingRes, this, false)
        addView(loadingView)

        errorView = layoutInflater.inflate(layoutResProvider.errorRes, this, false)
        addView(errorView)
    }

    fun <T> setStateFromResult(loadableResult: LoadableResult<T>) {
        when (loadableResult) {
            is LoadableResult.Loading -> setStateLoading()
            is LoadableResult.Success -> setStateData()
            is LoadableResult.Failure -> setStateError(loadableResult.error)
        }
    }

    fun setRetryMethod(retry: () -> Unit) {
        buttonError.setOnClickListener { retry.invoke() }
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
        setErrorStateContent(
            titleRes = R.string.error_no_network_title,
            descriptionRes = R.string.error_no_network_description,
            errorImageRes = R.drawable.ic_no_internet,
        )
    }

    /**
     * Ошибка "Что-то не так"
     */
    private fun setGeneralError() {
        setErrorStateContent(
            titleRes = R.string.error_something_wrong_title,
            descriptionRes = R.string.error_something_wrong_description,
            errorImageRes = R.drawable.ic_something_wrong,
        )
    }

    private fun setErrorStateContent(@StringRes titleRes: Int, @StringRes descriptionRes: Int, @DrawableRes errorImageRes: Int) {
        textErrorTitle?.setText(titleRes)
        textErrorDescription?.setText(descriptionRes)
        imageViewError?.setImageResource(errorImageRes)
    }

    private fun stateIsDisabled(state: State): Boolean {
        return disabledStates.contains(state)
    }

    private class LayoutResProvider {

        companion object {
            @LayoutRes
            val DEFAULT_ERROR_LAYOUT = R.layout.view_state_error

            @LayoutRes
            val DEFAULT_LOADING_LAYOUT = R.layout.view_state_loading
        }

        @LayoutRes val loadingRes: Int = DEFAULT_LOADING_LAYOUT
        @LayoutRes val errorRes: Int = DEFAULT_ERROR_LAYOUT
    }
}
