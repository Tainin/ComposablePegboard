package com.tainin.composablepegboard.pegboard.options

import androidx.compose.ui.unit.Dp
import com.tainin.composablepegboard.utils.interpolate

data class StreetOptions(val streetWidth: Dp, val lineThickness: Dp) {
    fun getLineInsets(lineCount: Int) = sequence {
        if (lineCount == 1) yield(streetWidth / 2)

        val subCount = lineCount - 1
        if (subCount <= 0) return@sequence

        val low = lineThickness / 2
        val high = streetWidth - (lineThickness / 2)
        (0..subCount).forEach { i ->
            yield(low.interpolate(high, i.toFloat() / subCount))
        }
    }
}