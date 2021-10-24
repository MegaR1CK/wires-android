package com.wires.app.extensions

import android.content.res.Resources
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat

fun Resources.getColorCompat(@ColorRes color: Int) = ResourcesCompat.getColor(this, color, null)
