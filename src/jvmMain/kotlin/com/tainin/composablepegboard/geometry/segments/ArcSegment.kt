package com.tainin.composablepegboard.geometry.segments

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.*
import com.tainin.composablepegboard.geometry.drawing.MultiDrawable
import com.tainin.composablepegboard.geometry.drawing.ScoringSegmentDrawable
import com.tainin.composablepegboard.geometry.drawing.ScoringSegmentMultiDrawable
import com.tainin.composablepegboard.geometry.drawing.SegmentDrawingOptions
import com.tainin.composablepegboard.model.Player
import com.tainin.composablepegboard.utils.*
import kotlin.math.round
import kotlin.math.withSign

class ArcSegment(
    inAngle: Float,
    arcAngle: Float,
    private val radius: Dp,
) : ScoringSegment() {
    override val angles = Bounds(inAngle) { it + arcAngle }
    override val positions: Bounds<DpOffset>
    override val size: DpSize

    val arcAngles: Bounds<Float>
    val center: DpOffset

    init {
        arcAngles = Bounds(
            inAngle - FloatHalfPI.withSign(arcAngle)
        ) { it + arcAngle }

        val arcPoints = arcAngles.transform { angle -> DpOffset.polarOffset(angle, radius) }

        val endsRect = DpRect(arcPoints.start, DpSize.Zero).include(arcPoints.end)

        val bounds = arcAngles
            .run {
                if (arcAngle > 0) arcAngles.start..arcAngles.end
                else arcAngles.end..arcAngles.start
            }
            .getCardinalAnglesIn()
            .map { angle ->
                DpOffset.polarOffset(angle, radius)
            }
            .fold(endsRect) { rect, offset -> rect.include(offset) }

        center = DpOffset.Zero - bounds.topLeft
        size = bounds.size
        positions = arcPoints.transform { it - bounds.topLeft }
    }

    override fun getDrawable(
        segmentDrawingOptions: SegmentDrawingOptions,
        density: Density
    ): ScoringSegmentMultiDrawable =
        run {
            val (baseRadius, halfWidth, lineThickness) =
                with(density) {
                    Triple(
                        radius.toPx(),
                        segmentDrawingOptions.run { streetWidth / 2f }.toPx(),
                        segmentDrawingOptions.lineThickness.toPx(),
                    )
                }

            val radiiBounds = Bounds(
                start = baseRadius - halfWidth,
                end = baseRadius + halfWidth,
                reversed = angles.run { end > start }
            )

            val center = center.toOffset(density)

            segmentDrawingOptions.game.playerSequence.mapIndexed { i, player ->
                val (fStart, fStep) = segmentDrawingOptions.calcLineSpacingStartStep()
                val lineRadius = radiiBounds.run {
                    val f = fStart + (fStep * i)
                    ((1 - f) * start) + (f * end)
                }
                Drawable(
                    player = player,
                    lineThickness = lineThickness,
                    usePlayerHighlight = segmentDrawingOptions.useHighlight,
                    center = center,
                    radius = lineRadius,
                    startAngle = arcAngles.start,
                    sweepAngle = arcAngles.run { end - start },
                )
            }.let { drawables -> MultiDrawable(drawables) }
        }

    private class Drawable(
        player: Player,
        lineThickness: Float,
        usePlayerHighlight: Boolean,
        val center: Offset,
        val radius: Float,
        val startAngle: Float,
        val sweepAngle: Float,
    ) : ScoringSegmentDrawable(player, lineThickness, usePlayerHighlight) {

        val rect = Rect(
            left = center.x - radius,
            top = center.y - radius,
            right = center.x + radius,
            bottom = center.y + radius,
        )

        val stroke = Stroke(lineThickness)

        override fun DrawScope.drawHighlight() =
            drawArc(
                color = player.color.highlightColor,
                startAngle = startAngle.radiansToDegrees(),
                sweepAngle = sweepAngle.radiansToDegrees(),
                useCenter = false,
                topLeft = rect.topLeft,
                size = rect.size,
                style = stroke,
            )

        override fun getHoleOffset(fraction: Float) =
            center + Offset.polarOffset(startAngle + (fraction * sweepAngle), radius)
    }
}

private fun ClosedFloatingPointRange<Float>.getCardinalAnglesIn() =
    generateSequence(round(start / FloatTAU)) { it + 0.25f }
        .take(5)
        .map { step -> step * FloatTAU }
        .filter { angle -> contains(angle) }