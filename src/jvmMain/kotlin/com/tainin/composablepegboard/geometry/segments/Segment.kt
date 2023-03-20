package com.tainin.composablepegboard.geometry.segments

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import com.tainin.composablepegboard.geometry.drawing.ScoringSegmentMultiDrawable
import com.tainin.composablepegboard.geometry.drawing.SegmentDrawable
import com.tainin.composablepegboard.geometry.drawing.SegmentDrawingOptions
import com.tainin.composablepegboard.utils.Bounds

abstract class Segment<out Drawable : SegmentDrawable> {
    abstract val angles: Bounds<Float>
    abstract val positions: Bounds<DpOffset>
    abstract val size: DpSize

    abstract fun getDrawable(
        segmentDrawingOptions: SegmentDrawingOptions,
        density: Density,
    ): Drawable
}

typealias ScoringSegment = Segment<ScoringSegmentMultiDrawable>