package com.tainin.composablepegboard.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.tainin.composablepegboard.utils.FloatPI
import com.tainin.composablepegboard.utils.rotate
import com.tainin.composablepegboard.utils.times


class Anchor(
    val origin: DpOffset,
    val direction: DpOffset,
) {
    val cross = direction.rotate(FloatPI / 2)
}

abstract class SegmentPath(
    val anchor: Anchor
) {
    abstract val nextAnchor: Anchor
}

class LineSegment(
    anchor: Anchor,
    length: Dp,
) : SegmentPath(anchor) {
    override val nextAnchor =
        Anchor(
            origin = with(anchor) { origin + direction * length },
            direction = anchor.direction
        )
}

class ArcSegment(
    anchor: Anchor,
    radius: Dp,
    angle: Float,
) : SegmentPath(anchor) {
    val center = with(anchor) { origin + cross * radius }

    override val nextAnchor =
        Anchor(
            origin = center + (anchor.origin - center).rotate(angle),
            direction = anchor.direction.rotate(angle),
        )
}