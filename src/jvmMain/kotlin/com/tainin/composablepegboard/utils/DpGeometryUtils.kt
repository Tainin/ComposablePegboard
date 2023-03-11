package com.tainin.composablepegboard.utils

import androidx.compose.ui.unit.*
import kotlin.math.cos
import kotlin.math.sin

fun DpRect.include(offset: DpOffset) = with(offset) {
    DpRect(
        left = min(left, x),
        top = min(top, y),
        right = max(right, x),
        bottom = max(bottom, y),
    )
}

fun DpOffset.Companion.polarOffset(angle: Float, radius: Dp) =
    DpOffset(
        x = radius * cos(angle),
        y = radius * sin(angle),
    )

val DpRect.topLeft get() = DpOffset(left, top)