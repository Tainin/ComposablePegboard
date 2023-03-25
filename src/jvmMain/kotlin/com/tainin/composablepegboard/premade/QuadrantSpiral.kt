package com.tainin.composablepegboard.premade

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.tainin.composablepegboard.geometry.path.SegmentPath
import com.tainin.composablepegboard.utils.FloatHalfPI

fun quadrantSpiral(
    separatorWidth: Dp,
    quarters: Sequence<Pair<Dp, Int>>,
    startAngle: Float = 0.0f,
) = SegmentPath.make(separatorWidth) {
    startNewPath(DpOffset.Zero, startAngle)
    quarters.forEach { (radius, count) ->
        addArcSegment(FloatHalfPI / count, radius, count)
    }
    shiftToOrigin()
}