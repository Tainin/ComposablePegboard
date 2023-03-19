package com.tainin.composablepegboard.geometry.path

import androidx.compose.ui.unit.*
import com.tainin.composablepegboard.geometry.segments.*
import com.tainin.composablepegboard.utils.topLeft

class SegmentPath(
    private val separatorWidth: Dp,
) {
    private val _scoringParts = mutableListOf<Part<ScoringSegment>>()
    val scoringParts: List<Part<ScoringSegment>> get() = _scoringParts
    private val _separatorParts = mutableListOf<Part<SeparatorSegment>>()
    val separatorParts: List<Part<SeparatorSegment>> get() = _separatorParts



    var bounds = DpRect(DpOffset.Zero, DpSize.Zero)
        private set

    private var currentAngle = 0f
    private var anchor = DpOffset.Zero

    fun startNewPath(newAnchor: DpOffset, newAngle: Float) {
        anchor = newAnchor
        currentAngle = newAngle
    }

    fun shiftToOrigin() {
        _scoringParts.replaceAll { part ->
            Part(part.segment, part.topLeft - bounds.topLeft)
        }
        _separatorParts.replaceAll { part ->
            Part(part.segment, part.topLeft - bounds.topLeft)
        }
        bounds = DpRect(DpOffset.Zero, bounds.size)
    }

    fun addArcSegment(arcAngle: Float, radius: Dp, count: Int = 1) =
        addScoringSegment(count) { ArcSegment(currentAngle, arcAngle, radius) }

    fun addLineSegment(length: Dp, count: Int = 1) =
        addScoringSegment(count) { LineSegment(currentAngle, length) }

    fun addSeparatorSegment() {
        val separator = SeparatorSegment(currentAngle, separatorWidth)
        _separatorParts.add(Part(separator, fitSegment(separator)))
    }

    private fun addScoringSegment(count: Int, initializer: () -> ScoringSegment) {
        require(count > 0) { "count must be greater than 0." }
        repeat(count) { addScoringPart(initializer(), true) }
    }

    private fun addScoringPart(segment: ScoringSegment, addSeparator: Boolean) {
        _scoringParts.add(Part(segment, fitSegment(segment)))
        if (!addSeparator) return
        addSeparatorSegment()
    }

    private fun fitSegment(segment: AnySegment) =
        DpRect(anchor - segment.positions.start, segment.size)
            .also { segmentBounds ->
                anchor = segmentBounds.topLeft + segment.positions.end
                currentAngle = segment.angles.end
                bounds = DpRect(
                    left = min(bounds.left, segmentBounds.left),
                    top = min(bounds.top, segmentBounds.top),
                    right = max(bounds.right, segmentBounds.right),
                    bottom = max(bounds.bottom, segmentBounds.bottom),
                )
            }.topLeft

    class Part<T : AnySegment>(val segment: T, val topLeft: DpOffset)
}