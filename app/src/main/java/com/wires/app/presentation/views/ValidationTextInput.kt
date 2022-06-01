package com.wires.app.presentation.views

import android.content.Context
import android.graphics.Typeface
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.wires.app.R
import com.wires.app.databinding.ViewTextInputBinding

class ValidationTextInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_MIN_TEXT_LENGTH = 3
        private const val DEFAULT_MAX_TET_LENGTH = 20
        private const val DEFAULT_REGEX = "^[a-zA-Z0-9]*$"
    }

    private val binding = ViewTextInputBinding.inflate(LayoutInflater.from(context), this, true)

    var emptyFieldError = context.getString(R.string.error_empty_field)
    var lengthError: String? = null
    var patternError: String? = null
    var additionalValidationError: String? = null
    var validationRegex = DEFAULT_REGEX.toRegex()
    var minTextLength = DEFAULT_MIN_TEXT_LENGTH
    var maxTextLength = DEFAULT_MAX_TET_LENGTH
    var canBeEmpty = false
    var onFocusRemoved: () -> Unit = {}
    var additionalValidation: (String) -> Boolean = { true }

    var error: String?
        get() = binding.textInputLayout.error as? String?
        set(value) {
            binding.textInputLayout.error = value
        }

    var text: String?
        get() = binding.editText.text.toString()
        set(value) {
            binding.editText.setText(value)
        }

    val editText = binding.editText

    init {
        if (attrs != null) {
            context.withStyledAttributes(
                attrs,
                R.styleable.ValidationTextInput,
                defStyleAttr
            ) {
                lengthError = getString(R.styleable.ValidationTextInput_lengthErrorText)
                patternError = getString(R.styleable.ValidationTextInput_patternErrorText)
                additionalValidationError = getString(R.styleable.ValidationTextInput_additionalValidationErrorText)
                minTextLength = getInt(R.styleable.ValidationTextInput_minTextLength, DEFAULT_MIN_TEXT_LENGTH)
                maxTextLength = getInt(R.styleable.ValidationTextInput_maxTextLength, DEFAULT_MAX_TET_LENGTH)
                validationRegex =
                    getString(R.styleable.ValidationTextInput_validationPattern)?.toRegex() ?: DEFAULT_REGEX.toRegex()
                canBeEmpty = getBoolean(R.styleable.ValidationTextInput_canBeEmpty, false)
                with(editText) {
                    setCompoundDrawablesWithIntrinsicBounds(
                        getDrawable(R.styleable.ValidationTextInput_fieldStartIcon),
                        null,
                        getDrawable(R.styleable.ValidationTextInput_fieldEndIcon),
                        null
                    )
                    hint = getString(R.styleable.ValidationTextInput_fieldHint)
                    imeOptions = if (getInt(R.styleable.ValidationTextInput_fieldImeOptions, 0) == 0) {
                        EditorInfo.IME_ACTION_NEXT
                    } else {
                        EditorInfo.IME_ACTION_DONE
                    }
                    when (getInt(R.styleable.ValidationTextInput_fieldInputType, 0)) {
                        1 -> inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        2 -> {
                            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                            typeface = Typeface.DEFAULT
                        }
                        else -> inputType = InputType.TYPE_CLASS_TEXT
                    }
                    setOnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            validate()
                            onFocusRemoved()
                        }
                    }
                }
            }
        }
    }

    fun validate(): Boolean {
        val text = binding.editText.text.toString()
        error = when {
            text.isBlank() -> if (canBeEmpty) null else emptyFieldError
            text.length !in minTextLength..maxTextLength -> lengthError
            !text.matches(validationRegex) -> patternError
            !additionalValidation(text) -> additionalValidationError
            else -> null
        }
        return error == null
    }

    fun isEmpty() = text.isNullOrEmpty()

    fun setOnEditorActionListener(action: () -> Boolean) {
        binding.editText.setOnEditorActionListener { _, _, _ -> action() }
    }
}
