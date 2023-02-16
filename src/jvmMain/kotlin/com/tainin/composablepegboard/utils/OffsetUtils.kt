package com.tainin.composablepegboard.utils

import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin
fun Offset.Companion.unitFromAngle(angle: Float) = Offset(cos(angle), sin(angle))