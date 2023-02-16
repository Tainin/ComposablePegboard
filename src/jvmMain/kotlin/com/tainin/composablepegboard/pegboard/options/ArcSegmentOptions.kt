package com.tainin.composablepegboard.pegboard.options

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.math.PI
import kotlin.math.min

private const val HALF_PI = PI.toFloat() / 2f

enum class ArcDirection {
    Clockwise,
    AntiClockwise,
}

enum class ArcFocus {
    Right,
    BottomRight,
    Bottom,
    BottomLeft,
    Left,
    TopLeft,
    Top,
    TopRight;

    fun getClockwiseAngles() = getClockwiseStartIndex() * HALF_PI to getClockwiseSweepCount() * HALF_PI

    fun getRadius(size: Size) = when (this) {
        Top, Bottom -> .5f to 1f
        Left, Right -> 1f to .5f
        else -> 1f to 1f
    }.run { min(first * size.width, second * size.height) }

    fun getCenterOffset(size: Size) = when (this) {
        Right -> Offset(size.width, size.height / 2f)
        BottomRight -> Offset(size.width, size.height)
        Bottom -> Offset(size.width / 2f, size.height)

        BottomLeft -> Offset.Zero.copy(y = size.height)
        Left -> Offset.Zero.copy(y = size.height / 2f)
        TopLeft -> Offset.Zero
        Top -> Offset.Zero.copy(x = size.width / 2f)
        TopRight -> Offset.Zero.copy(x = size.width)
    }

    private fun getClockwiseStartIndex() = when (this) {
        TopLeft, Top -> 0
        TopRight, Right -> 1
        BottomRight, Bottom -> 2
        BottomLeft, Left -> 3
    }

    private fun getClockwiseSweepCount() = when (this) {
        TopLeft, TopRight, BottomRight, BottomLeft -> 1
        Top, Right, Bottom, Left -> 2
    }
}

class ArcSegmentOptions(val focus: ArcFocus, val direction: ArcDirection) {
    fun getAngles() = focus.getClockwiseAngles().let { angles ->
        when (direction) {
            ArcDirection.Clockwise -> angles
            ArcDirection.AntiClockwise -> angles.first + angles.second to -angles.second
        }
    }
}