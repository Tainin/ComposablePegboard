package com.tainin.composablepegboard.utils

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min

fun DpRect.include(point: DpOffset) = with(point) {
    DpRect(
        left = min(left, x),
        top = min(top, y),
        right = max(right, x),
        bottom = max(bottom, y)
    )
}