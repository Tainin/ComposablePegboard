package com.tainin.composablepegboard.pegboard.options

import com.tainin.composablepegboard.model.LineOrder

enum class LinearSegmentDirection {
    LeftToRight,
    RightToLeft;

    val lineOrder get() = when(this) {
        LeftToRight -> LineOrder.Forward
        RightToLeft -> LineOrder.Reverse
    }
}