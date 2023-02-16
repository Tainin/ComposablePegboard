package com.tainin.composablepegboard.utils

import androidx.compose.ui.unit.Dp

fun Dp.interpolate(other: Dp, t: Float) = times(1f - t) + other.times(t)