package com.tainin.composablepegboard.geometry

import androidx.compose.ui.unit.*
import com.tainin.composablepegboard.utils.include
import com.tainin.composablepegboard.utils.polarOffset
import com.tainin.composablepegboard.utils.topLeft

class SeparatorSegment(
    inAngle: Float,
    width: Dp,
) : Segment() {
    override val angles = Bounds(inAngle)
    override val positions: Bounds<DpOffset>
    override val size: DpSize

    init {
        val vector = DpOffset.polarOffset(inAngle, width)
        val rect = DpRect(DpOffset.Zero, DpSize.Zero).include(vector)

        size = rect.size
        positions = Bounds(DpOffset.Zero - rect.topLeft) { it + vector }
    }
}