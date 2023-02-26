package com.tainin.composablepegboard.pegboard.segments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.tainin.composablepegboard.pegboard.options.LinearSegmentDirection

@Composable
fun SegmentSeparator(
    modifier: Modifier,
    labelPosition: LinearSegmentDirection,
    lineThickness: Dp,
    labelContent: @Composable BoxScope.() -> Unit,
) = Box(
    modifier = modifier
        .drawWithCache {
            val (start, end) = when (labelPosition) {
                LinearSegmentDirection.North, LinearSegmentDirection.South ->
                    Offset(size.width / 2, 0f) to Offset(size.width / 2, size.height)

                LinearSegmentDirection.East, LinearSegmentDirection.West ->
                    Offset(0f, size.height / 2) to Offset(size.width, size.height / 2)
            }
            val thickness = lineThickness.toPx()

            onDrawBehind {
                drawLine(
                    color = Color.Black,
                    start = start,
                    end = end,
                    strokeWidth = thickness,
                    alpha = 1f,
                )
            }
        }
        .layout { measurable, constraints ->
            val placeable = measurable.measure(Constraints())
            layout(constraints.maxWidth, constraints.maxHeight) {
                placeable.place(
                    when (labelPosition) {
                        LinearSegmentDirection.North ->
                            IntOffset((constraints.maxWidth - placeable.width) / 2, -placeable.height)

                        LinearSegmentDirection.South ->
                            IntOffset((constraints.maxWidth - placeable.width) / 2, constraints.maxHeight)

                        LinearSegmentDirection.East ->
                            IntOffset(constraints.maxWidth, (constraints.maxHeight - placeable.height) / 2)

                        LinearSegmentDirection.West ->
                            IntOffset(-placeable.width, (constraints.maxHeight - placeable.height) / 2)
                    }
                )
            }
        },
    content = labelContent,
)