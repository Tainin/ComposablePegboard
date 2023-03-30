package com.tainin.composablepegboard.geometry.segments

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.*
import com.tainin.composablepegboard.geometry.drawing.SegmentDrawable
import com.tainin.composablepegboard.geometry.drawing.SegmentDrawingOptions
import com.tainin.composablepegboard.utils.*

class SeparatorSegment(
    inAngle: Float,
    width: Dp,
) : DrawableSegment<SegmentDrawable>() {
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

    override fun getDrawable(
        segmentDrawingOptions: SegmentDrawingOptions,
        density: Density
    ): SegmentDrawable = run {
        val (streetWidth, lineThickness, separatorThickness) =
            with(density) {
                segmentDrawingOptions.run {
                    Triple(
                        streetWidth.toPx(),
                        lineThickness.toPx(),
                        separatorThickness.toPx(),
                    )
                }
            }

        val midPoint = positions.run { lerp(start, end, 0.5f) }.toOffset(density)

        val maxOffset = Offset.polarOffset(
            angle = angles.start + FloatHalfPI,
            radius = (streetWidth + lineThickness) / 2f
        )

        val ends = Bounds(midPoint - maxOffset, midPoint + maxOffset)

        Drawable(ends, separatorThickness)
    }

    private class Drawable(
        private val ends: Bounds<Offset>,
        private val separatorThickness: Float,
    ) : SegmentDrawable() {
        override fun DrawScope.draw() =
            drawLine(
                color = Color.Black,
                start = ends.start,
                end = ends.end,
                strokeWidth = separatorThickness,
            )
    }
}