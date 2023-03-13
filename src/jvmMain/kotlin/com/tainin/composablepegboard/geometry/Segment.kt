package com.tainin.composablepegboard.geometry

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize

abstract class Segment {
    abstract val angles: Bounds<Float>
    abstract val positions: Bounds<DpOffset>
    abstract val size: DpSize

    data class Bounds<T>(val start: T, val end: T) {
        constructor(both: T) : this(both, both)
        constructor(start: T, startToEnd: (start: T) -> T) : this(start, startToEnd(start))

        fun <R> transform(transform: (T) -> R) = Bounds(transform(start), transform(end))
    }

    fun <T> Bounds(start: T, end: T, reversed: Boolean) =
        if (reversed) Bounds(end, start) else Bounds(start, end)
}