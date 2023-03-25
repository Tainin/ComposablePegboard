package com.tainin.composablepegboard.ui.pegboard

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tainin.composablepegboard.geometry.drawing.SegmentDrawingOptions
import com.tainin.composablepegboard.geometry.path.SegmentPath

@Composable
fun PathGameBoard(
    modifier: Modifier = Modifier,
    segmentDrawingOptions: SegmentDrawingOptions,
    path: SegmentPath,
) = Box(
    modifier = modifier
) {
    path.scoringParts.forEach { part ->
        val drawable = part.segment.getDrawable(
            segmentDrawingOptions = segmentDrawingOptions,
            density = LocalDensity.current,
        )

        SegmentWidget(part.topLeft, part.segment.size, drawable)
    }

    var separatorIndex = 0
    path.separatorParts.forEach { part ->
        val drawable = part.segment.getDrawable(
            segmentDrawingOptions = segmentDrawingOptions,
            density = LocalDensity.current
        )

        separatorIndex++
        SeparatorWidget(
            topLeft = part.topLeft,
            size = part.segment.size,
            drawable = drawable,
            separatorIndex = separatorIndex,
            indexToText = { index -> index.times(5).takeIf { it != 90 }?.toString() ?: "S" },
            style = TextStyle(Color.Black, 18.sp, FontWeight.Bold),
            labelOffset = part.segment.getSeparatorLabelOffset(segmentDrawingOptions),
        )
    }
}