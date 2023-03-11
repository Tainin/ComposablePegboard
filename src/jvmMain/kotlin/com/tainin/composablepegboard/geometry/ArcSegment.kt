package com.tainin.composablepegboard.geometry

import androidx.compose.ui.unit.*
import com.tainin.composablepegboard.utils.*
import kotlin.math.round
import kotlin.math.withSign

class ArcSegment(
    inAngle: Float,
    arcAngle: Float,
    val radius: Dp,
) : Segment() {
    override val angles = Bounds(inAngle) { it + arcAngle }
    override val positions: Bounds<DpOffset>
    override val size: DpSize

    private val arcAngles: Bounds<Float>
    private val center: DpOffset

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
}

private fun ClosedFloatingPointRange<Float>.getCardinalAnglesIn() =
    generateSequence(round(start / FloatTAU)) { it + 0.25f }
        .take(4)
        .map { step -> step * FloatTAU }
        .filter { angle -> contains(angle) }