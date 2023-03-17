package com.tainin.composablepegboard.geometry

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val singleLineStartStep = 0.0f to 0.5f

class SegmentDrawingOptions(
    val streetWidth: Dp,
    val lineThickness: Dp,
    val separatorThickness: Dp,
    val separatorLabelDistance: Dp,
    val colors: Sequence<Color>,
    val useHighlight: Boolean = true,
) {

    private val lineCount get() = colors.count()

    init {
        require(streetWidth > 0.dp) { "street width must be greater than 0." }
        require(lineCount > 0) { "lineCount must be greater than 0." }
    }

    fun calcLineSpacingStartStep() = when (lineCount) {
        1 -> singleLineStartStep
        else -> 0f to (1f / lineCount.dec())
    }

    fun getLineSpacing(firstLineIndex: Int) = run {
        val (fStart, fStep) = calcLineSpacingStartStep()
        generateSequence(firstLineIndex) { i -> i + 1 }
            .map { i -> fStart + (fStep * i) }
            .take(lineCount - firstLineIndex)
    }
}