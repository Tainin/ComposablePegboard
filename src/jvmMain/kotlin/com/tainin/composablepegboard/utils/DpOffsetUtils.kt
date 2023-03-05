package com.tainin.composablepegboard.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

fun DpOffset.Companion.unitFromAngle(angle: Float) = DpOffset(cos(angle).dp, sin(angle).dp)
fun DpOffset.rotate(angle: Float) = applyRotation(cos(angle), sin(angle))
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