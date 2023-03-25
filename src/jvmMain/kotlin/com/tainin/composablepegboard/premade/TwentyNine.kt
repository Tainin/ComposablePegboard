package com.tainin.composablepegboard.premade

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.tainin.composablepegboard.geometry.path.SegmentPath
import com.tainin.composablepegboard.utils.FloatTAU

fun twentyNine(
    separatorWidth: Dp,
    lineLength: Dp,
    radii: Triple<Dp, Dp, Dp>
) = SegmentPath.make(separatorWidth) {
    startNewPath(DpOffset.Zero, 88 * FloatTAU / 24)
    addArcSegment(17 * FloatTAU / 144, radii.third, 6)
    addLineSegment(lineLength, 2)
    addArcSegment(-9 * FloatTAU / 48, radii.second, 2)
    addLineSegment(lineLength, 4)
    addArcSegment(-6 * FloatTAU / 24, radii.second)
    addLineSegment(lineLength, 4)
    addArcSegment(-6 * FloatTAU / 24, radii.first)
    addArcSegment(-12 * FloatTAU / 96, radii.third, 4)
    shiftToOrigin()
}