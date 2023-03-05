package com.tainin.composablepegboard.geometry

import androidx.compose.ui.unit.Dp

class LineSegment(
    override val anchor: Anchor,
    length: Dp,
) : SegmentPath() {
    override val nextAnchor = anchor.getLinearNext(length)
    override val boundingRect = anchor.makeBoundingRect(nextAnchor)
}