package com.tainin.composablepegboard.geometry.drawing

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tainin.composablepegboard.model.Player

private val singleLineStartStep = 0.5f to 0.0f

class SegmentDrawingOptions(
    val streetWidth: Dp,
    val lineThickness: Dp,
    val separatorThickness: Dp,
    val separatorLabelDistance: Dp,
    val players: Sequence<Player>,
    val useHighlight: Boolean = true,
) {

    private val lineCount = players.count()

    init {
        require(streetWidth > 0.dp) { "street width must be greater than 0." }
        require(lineCount > 0) { "lineCount must be greater than 0." }
    }

    fun calcLineSpacingStartStep() = when (lineCount) {
        1 -> singleLineStartStep
        else -> 0f to (1f / lineCount.dec())
    }
}