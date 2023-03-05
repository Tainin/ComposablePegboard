package com.tainin.composablepegboard.geometry

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.DpSize
import com.tainin.composablepegboard.utils.include
import com.tainin.composablepegboard.utils.times
import com.tainin.composablepegboard.utils.unitFromAngle

class Anchor(
    val origin: DpOffset,
    val angle: Float,
) {
    fun getLinearNext(distance: Dp) = Anchor(
        origin = origin + DpOffset.unitFromAngle(angle) * distance,
        angle = angle,
    )

    fun makeBoundingRect(other: Anchor) = DpRect(origin, DpSize.Zero).include(other.origin)
}