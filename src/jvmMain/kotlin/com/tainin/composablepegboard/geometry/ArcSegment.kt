package com.tainin.composablepegboard.geometry

import androidx.compose.ui.unit.*
import com.tainin.composablepegboard.utils.*
import kotlin.math.absoluteValue
import kotlin.math.round
import kotlin.math.withSign

private const val MIN_ARC_ANGLE = 0.01f

class ArcSegment(
    override val anchor: Anchor,
    radius: Dp,
    arcAngle: Float,
) : SegmentPath() {
    init {
        require(radius > 0.dp) { "radius must be greater than 0." }
        require(arcAngle.absoluteValue > MIN_ARC_ANGLE) { "angle must be greater than $MIN_ARC_ANGLE" }
    }

    val center = getArcCenter(anchor, arcAngle, radius)
    override val nextAnchor = getArcNextAnchor(anchor, center, arcAngle)
    override val boundingRect = getArcBoundingRect(anchor, center, arcAngle, radius)!!
}

private fun getArcCenter(anchor: Anchor, sign: Float, radius: Dp) =
    anchor.run {
        val crossAngle = angle + (FloatPI / 2).withSign(sign)
        origin + DpOffset.unitFromAngle(crossAngle).times(radius)
    }

private fun getArcNextAnchor(anchor: Anchor, center: DpOffset, arcAngle: Float) = Anchor(
    origin = center + (anchor.origin - center).rotate(anchor.angle),
    angle = anchor.angle + arcAngle
)

private fun getArcBoundingPoints(
    range: ClosedFloatingPointRange<Float>,
    center: DpOffset,
    radius: Dp,
) = generateSequence(round(range.start / FloatTAU)) { it + 0.25f }
    .take(4)
    .mapNotNull { step -> (step * FloatTAU).takeIf { it in range } }
    .plus(range.run { sequenceOf(start, endInclusive) })
    .map { angle -> center + DpOffset.unitFromAngle(angle).times(radius) }

private fun getArcBoundingRect(anchor: Anchor, center: DpOffset, arcAngle: Float, radius: Dp) =
    getArcBoundingPoints(
        range = (0f to arcAngle)
            .sort()
            .transform { it + anchor.angle }
            .run { first..second },
        center = center,
        radius = radius,
    )
        .fold(null) { rect: DpRect?, point ->
            rect?.include(point) ?: DpRect(origin = point, size = DpSize.Zero)
        }