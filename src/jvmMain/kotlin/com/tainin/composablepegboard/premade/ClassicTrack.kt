package com.tainin.composablepegboard.premade

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.tainin.composablepegboard.geometry.path.SegmentPath
import com.tainin.composablepegboard.utils.FloatHalfPI
import com.tainin.composablepegboard.utils.FloatPI

fun classicTrack(
    separatorWidth: Dp,
    segmentLength: Dp,
    radius: Dp,
    startAngle: Float = 0.0f,
) = SegmentPath.make(separatorWidth) {
    val bigRadius = (radius * 2f) - (separatorWidth / 2f)
    startNewPath(DpOffset.Zero, startAngle)
    addLineSegment(segmentLength, 7)
    addArcSegment(FloatHalfPI, bigRadius, 2)
    addLineSegment(segmentLength, 7)
    addArcSegment(FloatPI, radius, 1)
    addLineSegment(segmentLength, 7)
    shiftToOrigin()
}