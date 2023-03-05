package com.tainin.composablepegboard.geometry

import androidx.compose.ui.unit.Dp

class SeparatorSegment(
    override val anchor: Anchor,
    thickness: Dp,
) : SegmentPath() {
    override val nextAnchor = anchor.getLinearNext(thickness)
    override val boundingRect = anchor.makeBoundingRect(nextAnchor)
}