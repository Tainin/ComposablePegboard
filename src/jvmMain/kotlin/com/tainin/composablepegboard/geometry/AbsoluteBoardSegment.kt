package com.tainin.composablepegboard.geometry

import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.DpSize

class AbsoluteBoardSegment(
    override val anchor: Anchor
) : BoardSegment() {
    override val nextAnchor get() = anchor
    override val boundingRect = DpRect(anchor.origin, DpSize.Zero)
}