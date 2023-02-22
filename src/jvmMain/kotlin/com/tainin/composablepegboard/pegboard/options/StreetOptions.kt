package com.tainin.composablepegboard.pegboard.options

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class StreetOptions(val streetWidth: Dp, val lineThickness: Dp) {
    fun lineSpacing(lineCount: Int) =
        require(lineCount > 0) {
            "lineCount must be greater than 0."
        }.let {
            if (lineCount == 1) streetWidth / 2 to 0.dp
            else lineThickness / 2 to (streetWidth - lineThickness) / lineCount.dec().coerceAtLeast(1)
        }
}