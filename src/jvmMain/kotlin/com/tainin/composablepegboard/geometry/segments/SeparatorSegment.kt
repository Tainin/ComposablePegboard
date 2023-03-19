package com.tainin.composablepegboard.geometry.segments

import androidx.compose.ui.unit.*
import com.tainin.composablepegboard.geometry.drawing.SegmentDrawingOptions
import com.tainin.composablepegboard.utils.*

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

    fun getSeparatorLabelOffset(
        segmentDrawingOptions: SegmentDrawingOptions
    ) = DpOffset.polarOffset(
        angle = angles.start + FloatHalfPI,
        radius = segmentDrawingOptions.separatorLabelDistance
    )
}