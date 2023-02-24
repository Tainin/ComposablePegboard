package com.tainin.composablepegboard.utils

import kotlin.math.absoluteValue

private const val SEGMENT_LENGTH = 5

fun Int.toSegmentOffset(segmentIndex: Int) = segmentOffset(minus(1), segmentIndex)
fun Int.toStartOffset() = takeIf { it <= 0 }?.coerceAtLeast(-1)?.absoluteValue
private fun segmentOffset(boardOffset: Int, segmentIndex: Int) = when {
    boardOffset !in (0 until 120) -> null
    boardOffset.div(SEGMENT_LENGTH) != segmentIndex -> null
    else -> boardOffset.rem(SEGMENT_LENGTH)
}