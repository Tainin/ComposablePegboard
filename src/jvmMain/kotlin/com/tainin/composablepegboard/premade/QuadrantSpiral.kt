package com.tainin.composablepegboard.premade

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.tainin.composablepegboard.geometry.path.SegmentPath
import com.tainin.composablepegboard.geometry.segments.ArcSegment
import com.tainin.composablepegboard.utils.FloatHalfPI
import com.tainin.composablepegboard.utils.FloatQuarterPI

fun quadrantSpiral(
    separatorWidth: Dp,
    quarters: Sequence<Pair<Dp, Int>>,
    startAngle: Float = 0.0f,
) = SegmentPath.make(separatorWidth) {
    val firstQuarter = quarters.first()
    val startArcAngle = FloatQuarterPI / firstQuarter.second
    val startSegment = ArcSegment(
        inAngle = startAngle - startArcAngle,
        arcAngle = startArcAngle,
        radius = firstQuarter.first,
        holeCount = 2,
    )
    startPath(DpOffset.Zero, startSegment)
    quarters.forEach { (radius, count) ->
        addArcSegment(FloatHalfPI / count, radius, count)
    }
    shiftToOrigin()
}