package com.wires.app.domain.repository

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import javax.inject.Inject

class ResourcesRepository @Inject constructor(
    private val context: Context
) {
    fun getString(@StringRes id: Int, vararg params: Any) = context.getString(id, *params)

    fun getQuantityString(@PluralsRes id: Int, quantity: Int, vararg params: Any) =
        context.resources.getQuantityString(id, quantity, *params)
}
