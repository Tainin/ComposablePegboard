package com.tainin.composablepegboard.ui.pegboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import com.tainin.composablepegboard.geometry.drawing.SegmentDrawable
import com.tainin.composablepegboard.utils.segmentWidgetDraw
import com.tainin.composablepegboard.utils.segmentWidgetOffset
import com.tainin.composablepegboard.utils.segmentWidgetSize

@Composable
fun SegmentWidget(
    topLeft: DpOffset,
    size: DpSize,
    drawable: SegmentDrawable,
    content: @Composable BoxScope.() -> Unit = {},
) = Box(
    modifier = Modifier
        .segmentWidgetSize(size)
        .segmentWidgetOffset(topLeft)
        .segmentWidgetDraw(drawable),
    content = content,
)

@Composable
fun BoxScope.SeparatorLabel(
    separatorIndex: Int,
    indexToText: (index: Int) -> String,
    style: TextStyle,
    labelOffset: DpOffset
) = BasicText(
    text = indexToText(separatorIndex),
    modifier = Modifier
        .wrapContentSize(unbounded = true)
        .align(Alignment.Center)
        .segmentWidgetOffset(labelOffset),
    style = style,
)

@Composable
fun SeparatorWidget(
    topLeft: DpOffset,
    size: DpSize,
    drawable: SegmentDrawable,
    separatorIndex: Int,
    indexToText: (index: Int) -> String,
    style: TextStyle,
    labelOffset: DpOffset
) = SegmentWidget(topLeft, size, drawable) {
    SeparatorLabel(separatorIndex, indexToText, style, labelOffset)
}