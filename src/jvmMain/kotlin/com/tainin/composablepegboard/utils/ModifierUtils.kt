package com.tainin.composablepegboard.utils

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.*
import com.tainin.composablepegboard.geometry.drawing.SegmentDrawable

fun Modifier.segmentWidgetSize(size: DpSize) = this
    .requiredWidth(max(size.width, 1.dp))
    .requiredHeight(max(size.height, 1.dp))

fun Modifier.segmentWidgetOffset(offset: DpOffset) =
    offset { offset.run { IntOffset(x.roundToPx(), y.roundToPx()) } }

fun Modifier.segmentWidgetDraw(drawable: SegmentDrawable) =
    drawBehind { with(drawable) { draw() } }