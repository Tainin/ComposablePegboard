package com.tainin.composablepegboard.geometry

import androidx.compose.ui.unit.*
import com.tainin.composablepegboard.utils.FloatHalfPI
import com.tainin.composablepegboard.utils.include
import com.tainin.composablepegboard.utils.polarOffset
import com.tainin.composablepegboard.utils.topLeft

class LineSegment(
    inAngle: Float,
    length: Dp,
) : Segment() {
    override val angles = Bounds(inAngle)
    override val positions: Bounds<DpOffset>
    override val size: DpSize

    init {
        val vector = DpOffset.polarOffset(inAngle, length)
        val rect = DpRect(DpOffset.Zero, DpSize.Zero).include(vector)

        size = rect.size
        positions = Bounds(DpOffset.Zero - rect.topLeft) { it + vector }
    }

    fun getLineEnds(
        segmentDrawingOptions: SegmentDrawingOptions,
        firstLineIndex: Int = 0,
    ) = run {
        val maxOffset = DpOffset.polarOffset(
            angle = angles.start + FloatHalfPI,
            radius = segmentDrawingOptions.streetWidth / 2f,
        )
        val bounds = Bounds(maxOffset, DpOffset.Zero - maxOffset)

        segmentDrawingOptions
            .getLineSpacing(firstLineIndex)
            .map { f ->
                val offset = bounds.run { lerp(start, end, f) }
                positions.transform { end -> end + offset }
            }
    }
}