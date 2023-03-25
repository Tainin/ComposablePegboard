package com.tainin.composablepegboard.geometry.segments

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.*
import com.tainin.composablepegboard.geometry.drawing.MultiDrawable
import com.tainin.composablepegboard.geometry.drawing.PeggingLineDrawable
import com.tainin.composablepegboard.geometry.drawing.SegmentDrawingOptions
import com.tainin.composablepegboard.model.Player
import com.tainin.composablepegboard.utils.*

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
    ): MultiDrawable<PeggingLineDrawable> = run {
        val (streetWidth, lineThickness) =
            with(density) {
                segmentDrawingOptions.run {
                    streetWidth.toPx() to lineThickness.toPx()
                }
            }

        val offsetBounds = Offset.polarOffset(
            angle = angles.start + FloatHalfPI,
            radius = streetWidth / 2f,
        ).let { max -> Bounds(-max, max) }

        val centeredEnds = positions.transform { end -> end.toOffset(density) }

        segmentDrawingOptions.game.playerSequence.mapIndexed { i, player ->
            val (fStart, fStep) = segmentDrawingOptions.calcLineSpacingStartStep()
            val offset = offsetBounds.run { lerp(start, end, fStart + (fStep * i)) }
            val ends = centeredEnds.transform { end -> end + offset }
            Drawable(
                player = player,
                lineThickness = lineThickness,
                usePlayerHighlight = segmentDrawingOptions.useHighlight,
                ends = ends
            )
        }.let { drawables -> MultiDrawable(drawables) }
    }

    private class Drawable(
        player: Player,
        lineThickness: Float,
        usePlayerHighlight: Boolean,
        private val ends: Bounds<Offset>,
    ) : PeggingLineDrawable(player, lineThickness, usePlayerHighlight) {
        override fun DrawScope.drawHighlight() =
            drawLine(
                color = player.color.highlightColor,
                start = ends.start,
                end = ends.end,
                strokeWidth = lineThickness,
            )

        override fun getHoleOffset(fraction: Float) = ends.run { lerp(start, end, fraction) }
    }
}