package com.tainin.composablepegboard.pegboard.options

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.tainin.composablepegboard.model.LineOrder
import com.tainin.composablepegboard.utils.FloatHalfPI
import com.tainin.composablepegboard.utils.swap
import com.tainin.composablepegboard.utils.transform
import kotlin.math.min

enum class ArcDirection {
    Clockwise,
    AntiClockwise;

    val lineOrder get() = when(this) {
        Clockwise -> LineOrder.Forward
        AntiClockwise -> LineOrder.Reverse
    }
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

    fun getClockwiseAngles() = getClockwiseIndices().transform { it * FloatHalfPI }

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

    private fun getClockwiseIndices() = when (this) {
        TopLeft -> 0 to 1
        TopRight -> 1 to 2
        BottomRight -> 2 to 3
        BottomLeft -> 3 to 4

        Top -> 0 to 2
        Right -> 1 to 3
        Bottom -> 2 to 4
        Left -> 3 to 5
    }
}

data class ArcSegmentOptions(val focus: ArcFocus, val direction: ArcDirection) {
    fun getAngles() = focus.getClockwiseAngles().let { angles ->
        when (direction) {
            ArcDirection.Clockwise -> angles
            ArcDirection.AntiClockwise -> angles.swap()
        }
    }
}