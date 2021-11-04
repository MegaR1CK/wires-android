package com.wires.app.presentation.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.button.MaterialButton
import com.google.android.material.color.MaterialColors
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.extensions.getColorCompat
import kotlin.math.max

/**
 * MaterialButton, которая умеет обрабатывать LoadableResult и показывать крутилку прогресса во время загрузки
 * По дефолту цвет прогресса белый. Поменять его можно в xml через параметр app:progressColor или методом setProgressColor()
 */
private const val DEFAULT_PROGRESS_COLOR = R.color.white
private const val DEFAULT_BACKGROUND_COLOR = R.color.blue

class StatefulMaterialButton : MaterialButton, ResultAwareView {

    private val progressDrawable = CircularProgressDrawable(context).apply {
        setStyle(CircularProgressDrawable.DEFAULT)
        setColorSchemeColors(context.getColorCompat(DEFAULT_PROGRESS_COLOR))
    }

    var isLoading: Boolean = false
        set(isLoading) {
            if (field == isLoading) {
                return
            }
            field = isLoading
            handleStateChange(isLoading)
        }

    /** Изображение иконки, которое необходимо отображать, если она в наличии */
    private var iconRes: Drawable? = null

    private var defaultText: CharSequence = ""

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet? = null) {
        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.StatefulMaterialButton,
                0,
                0
            )

            try {
                // цвет прогресса
                val themeProgressColor = MaterialColors.getColor(
                    context, R.attr.colorOnPrimary,
                    context.getColorCompat(
                        DEFAULT_PROGRESS_COLOR
                    )
                )
                val styleProgressColor = typedArray.getColor(
                    R.styleable.StatefulMaterialButton_progressColor,
                    -1
                )
                setProgressColor(
                    if (styleProgressColor != -1) styleProgressColor else themeProgressColor
                )

                // цвет фона
                val themeBackgroundColor = MaterialColors.getColor(
                    context, R.attr.colorPrimary,
                    context.getColorCompat(
                        DEFAULT_BACKGROUND_COLOR
                    )
                )
                val styleBackgroundColor = typedArray.getColor(
                    R.styleable.StatefulMaterialButton_backgroundColor,
                    -1
                )
                setBackgroundColor(
                    if (styleBackgroundColor != -1) styleBackgroundColor else themeBackgroundColor
                )
            } finally {
                typedArray.recycle()
            }
        }
        iconRes = icon
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        defaultText = text
    }

    override fun handleResult(result: LoadableResult<*>) {
        val isLoading = result is LoadableResult.Loading
        this.isLoading = isLoading
    }

    /** Устанавливает цвет крутилки прогресса */
    fun setProgressColor(@ColorInt color: Int) {
        progressDrawable.setColorSchemeColors(color)
    }

    private fun handleStateChange(isLoading: Boolean) {
        icon = if (isLoading) null else iconRes

        if (isLoading) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    private fun showProgress() {
        isEnabled = false
        val drawableSpan = DrawableSpan(progressDrawable)
        val spannableStringProgress = SpannableString(" ").apply {
            setSpan(drawableSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        // устанавливаем вместо текста крутилку прогресса
        text = spannableStringProgress

        setupDrawableCallback()
        progressDrawable.start()
    }

    private fun hideProgress() {
        isEnabled = true
        progressDrawable.stop()
        text = defaultText
    }

    private fun setupDrawableCallback() {
        val callback = object : Drawable.Callback {
            override fun unscheduleDrawable(drawable: Drawable, runnable: Runnable) = Unit

            override fun invalidateDrawable(drawable: Drawable) {
                invalidate()
            }

            override fun scheduleDrawable(drawable: Drawable, p1: Runnable, p2: Long) = Unit
        }
        progressDrawable.callback = callback
    }
}

class DrawableSpan(drawable: Drawable) :
    ImageSpan(drawable) {

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fontMetricsInt: Paint.FontMetricsInt?): Int {
        val drawable = drawable
        val rect = drawable.bounds
        fontMetricsInt?.let {
            val fontMetrics = paint.fontMetricsInt
            val lineHeight = fontMetrics.bottom - fontMetrics.top
            val drHeight = max(lineHeight, rect.bottom - rect.top)
            val centerY = fontMetrics.top + lineHeight / 2
            fontMetricsInt.apply {
                ascent = centerY - drHeight / 2
                descent = centerY + drHeight / 2
                top = ascent
                bottom = descent
            }
        }
        return rect.width()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val drawable = drawable
        canvas.save()
        val fontMetrics = paint.fontMetricsInt
        val lineHeight = fontMetrics.descent - fontMetrics.ascent
        val centerY = y + fontMetrics.descent - lineHeight / 2
        val transY = centerY - drawable.bounds.height() / 2

        canvas.translate(x, transY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
    }
}
