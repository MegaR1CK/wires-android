package com.wires.app.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.wires.app.R

/**
 * Модифицированная версия функции для загрузки картинки с помощью Glide
 * Позволяет задавать трансформации, ресурсы для плейсхолдера или ошибки и др. параметры
 */
fun ImageView.load(
    imageUrl: String?,
    @DrawableRes placeHolderRes: Int? = context.resolveAttribute(R.attr.colorImagePlaceholder),
    @DrawableRes errorRes: Int? = placeHolderRes,
    @DrawableRes fallbackRes: Int? = placeHolderRes,
    isCircle: Boolean = false,
    transformations: List<Transformation<Bitmap>> = emptyList(),
    doOnFailure: () -> Unit = {},
    doOnSuccess: (Drawable?) -> Unit = { }
) {
    Glide.with(context)
        .load(imageUrl)
        .apply { placeHolderRes?.let(::placeholder) }
        .apply { errorRes?.let(::error) }
        .apply { fallbackRes?.let(::fallback) }
        .apply {
            if (isCircle) {
                apply(RequestOptions.circleCropTransform())
            }
        }
        .addListener(
            object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    doOnFailure.invoke()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    doOnSuccess.invoke(resource)
                    return false
                }
            }
        )
        .apply { transform(*transformations.toTypedArray()) }
        .into(this)
}

fun ImageView.countViewHeight(imageWidth: Int, imageHeight: Int) {
    val imageViewWidth = getScreenWidth() - 2 * resources.getDimensionPixelSize(R.dimen.padding_24)
    val imageViewHeight = imageViewWidth * imageHeight / imageWidth
    layoutParams.height = imageViewHeight
}
