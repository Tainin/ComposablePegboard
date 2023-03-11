package com.tainin.composablepegboard.utils

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

const val FloatPI = PI.toFloat()
const val FloatTAU = FloatPI * 2f
const val FloatHalfPI = FloatPI / 2f
const val FloatQuarterPI = FloatHalfPI / 2f

private const val RADIANS_TO_DEGREES = 180f / FloatPI
fun Float.radiansToDegrees() = times(RADIANS_TO_DEGREES)

fun Offset.Companion.unitFromAngle(angle: Float) = Offset(cos(angle), sin(angle))