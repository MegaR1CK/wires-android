package com.wires.app.extensions

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Устанавливает два листенера, отвечающих за сброс ошибки в текстовом инпуте
 * 1) Сбрасывает, если пользователь устанавливает фокус на поле
 * 2) Если пользователь изменил текст поля
 */
fun TextInputEditText.setErrorResetHandler(inputLayout: TextInputLayout) {
    setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            inputLayout.error = null
        }
    }
    doAfterTextChanged { inputLayout.error = null }
}

fun EditText.getInputText() = text.toString()
