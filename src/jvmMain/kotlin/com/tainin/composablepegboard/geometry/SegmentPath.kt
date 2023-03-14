package com.tainin.composablepegboard.geometry

import androidx.compose.ui.unit.*
import com.tainin.composablepegboard.utils.topLeft

class SegmentPath(
    private val separatorWidth: Dp,
) {
    val parts = mutableListOf<Part>()
    var anchor = DpOffset.Zero
        private set
    var bounds = DpRect(DpOffset.Zero, DpSize.Zero)
        private set

    private var currentAngle = 0f

    fun startNewPath(newAnchor: DpOffset, newAngle: Float) {
        anchor = newAnchor
        currentAngle = newAngle
    }

    fun shiftToOrigin() {
        parts.replaceAll { part ->
            Part(part.segment, part.topLeft - bounds.topLeft)
        }
        bounds = DpRect(DpOffset.Zero, bounds.size)
    }

    fun addArcSegment(arcAngle: Float, radius: Dp, count: Int = 1) =
        addSegment(count) { ArcSegment(currentAngle, arcAngle, radius) }

    fun addLineSegment(length: Dp, count: Int = 1) =
        addSegment(count) { LineSegment(currentAngle, length) }

    private fun addSegment(count: Int, initializer: () -> Segment) {
        require(count > 0) { "count must be greater than 0." }
        repeat(count) { addSegment(initializer()) }
    }

    private fun addSegment(segment: Segment, addSeparator: Boolean = true) {
        val segmentBounds = DpRect(anchor - segment.positions.start, segment.size)
        anchor = segmentBounds.topLeft + segment.positions.end
        currentAngle = segment.angles.end
        bounds = DpRect(
            left = min(bounds.left, segmentBounds.left),
            top = min(bounds.top, segmentBounds.top),
            right = max(bounds.right, segmentBounds.right),
            bottom = max(bounds.bottom, segmentBounds.bottom),
        )

        parts.add(Part(segment, segmentBounds.topLeft))

        if (!addSeparator) return

        addSegment(SeparatorSegment(currentAngle, separatorWidth), false)
    }

    class Part(val segment: Segment, val topLeft: DpOffset)
}