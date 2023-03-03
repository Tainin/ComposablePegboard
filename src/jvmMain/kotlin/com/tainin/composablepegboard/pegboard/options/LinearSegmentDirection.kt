package com.tainin.composablepegboard.pegboard.options

import androidx.compose.ui.geometry.Offset
import com.tainin.composablepegboard.model.LineOrder

enum class LinearSegmentDirection {
    North,
    South,
    East,
    West,
    ;

    val peggingAxis
        get() = when (this) {
            North, South -> Offset(0f, 1f)
            East, West -> Offset(1f, 0f)
        }
    val crossAxis
        get() = when (this) {
            North, South -> Offset(1f, 0f)
            East, West -> Offset(0f, 1f)
        }

    val lineOrder
        get() = when (this) {
            North -> LineOrder.Forward
            South -> LineOrder.Reverse
            East -> LineOrder.Forward
            West -> LineOrder.Reverse
        }
}