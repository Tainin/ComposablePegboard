package com.tainin.composablepegboard.geometry.segments

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import com.tainin.composablepegboard.utils.Bounds

abstract class Segment {
    abstract val angles: Bounds<Float>
    abstract val positions: Bounds<DpOffset>
    abstract val size: DpSize
}

abstract class ScoringSegment : Segment()