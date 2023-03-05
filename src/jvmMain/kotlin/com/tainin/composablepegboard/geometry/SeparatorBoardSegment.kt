package com.tainin.composablepegboard.geometry

import androidx.compose.ui.unit.Dp

class SeparatorBoardSegment(
    override val anchor: Anchor,
    thickness: Dp,
) : BoardSegment() {
    override val nextAnchor = anchor.getLinearNext(thickness)
    override val boundingRect = anchor.makeBoundingRect(nextAnchor)
}