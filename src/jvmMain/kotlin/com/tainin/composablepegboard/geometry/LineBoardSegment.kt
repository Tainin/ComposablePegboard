package com.tainin.composablepegboard.geometry

import androidx.compose.ui.unit.Dp

class LineBoardSegment(
    override val anchor: Anchor,
    length: Dp,
) : BoardSegment() {
    override val nextAnchor = anchor.getLinearNext(length)
    override val boundingRect = anchor.makeBoundingRect(nextAnchor)
}