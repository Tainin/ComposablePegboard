package com.tainin.composablepegboard.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import kotlin.math.cos
import kotlin.math.sin

fun DpOffset.rotate(theta: Float) = applyRotation(cos(theta), sin(theta))
private fun DpOffset.applyRotation(cos: Float, sin: Float) =
    DpOffset(
        x = x * cos - y * sin,
        y = x * sin + y * cos,
    )

operator fun DpOffset.times(other: Dp) =
    DpOffset(
        x = x * other.value,
        y = y * other.value
    )