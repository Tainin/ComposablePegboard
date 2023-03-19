package com.tainin.composablepegboard.geometry.segments

import androidx.compose.ui.unit.*
import com.tainin.composablepegboard.geometry.drawing.MultiScoringSegmentDrawable
import com.tainin.composablepegboard.geometry.drawing.SegmentDrawingOptions
import com.tainin.composablepegboard.utils.Bounds
import com.tainin.composablepegboard.utils.include
import com.tainin.composablepegboard.utils.polarOffset
import com.tainin.composablepegboard.utils.topLeft

class LineSegment(
    inAngle: Float,
    length: Dp,
) : ScoringSegment() {
    override val angles = Bounds(inAngle)
    override val positions: Bounds<DpOffset>
    override val size: DpSize

    init {
        val vector = DpOffset.polarOffset(inAngle, length)
        val rect = DpRect(DpOffset.Zero, DpSize.Zero).include(vector)

        size = rect.size
        positions = Bounds(DpOffset.Zero - rect.topLeft) { it + vector }
    }

    override fun getDrawable(
        segmentDrawingOptions: SegmentDrawingOptions,
        density: Density,
    ): MultiScoringSegmentDrawable {
        TODO("Not yet implemented")
    }
}