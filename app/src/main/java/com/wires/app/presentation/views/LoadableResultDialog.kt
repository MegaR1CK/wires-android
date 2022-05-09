package com.wires.app.presentation.views

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.wires.app.R
import com.wires.app.data.Failure
import com.wires.app.data.LoadableResult
import com.wires.app.data.Loading
import com.wires.app.data.Success

class LoadableResultDialog(
    context: Context,
    message: String? = null,
) {
    private val dialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.dialog_progress)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null, false)
        val textViewTitle = view.findViewById<TextView>(R.id.textViewMessage)
        if (message != null) textViewTitle.text = message
        builder.setView(view)
        builder.setCancelable(false)
        dialog = builder.create()
    }

    fun <T> setState(loadableResult: LoadableResult<T>) {
        when (loadableResult) {
            is Loading -> dialog.show()
            is Failure,
            is Success -> dialog.dismiss()
        }
    }
}
