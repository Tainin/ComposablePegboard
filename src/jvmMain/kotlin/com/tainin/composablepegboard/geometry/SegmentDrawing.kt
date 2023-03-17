package com.tainin.composablepegboard.geometry

import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.tainin.composablepegboard.utils.polarOffset
import com.tainin.composablepegboard.utils.radiansToDegrees
import com.tainin.composablepegboard.utils.toOffset

fun CacheDrawScope.drawScoringSegment(
    segment: ScoringSegment,
    segmentDrawingOptions: SegmentDrawingOptions,
) = when (segment) {
    is LineSegment -> drawLineSegment(segment, segmentDrawingOptions)
    is ArcSegment -> drawArcSegment(segment, segmentDrawingOptions)
    else -> error("Unknown segment type")
}

private fun CacheDrawScope.drawArcSegment(
    arcSegment: ArcSegment,
    segmentDrawingOptions: SegmentDrawingOptions,
) = run {

    val lineThickness = segmentDrawingOptions.lineThickness.toPx()
    val stroke = Stroke(lineThickness)
    val holeRadius = lineThickness / 2f
    val center = arcSegment.center.toOffset(this)
    val angles = arcSegment.arcAngles
    val boxes = arcSegment
        .getRadii(segmentDrawingOptions)
        .map { radius -> radius.toPx() }
        .map { radius ->
            val rect = Rect(
                left = center.x - radius,
                top = center.y - radius,
                right = center.x + radius,
                bottom = center.y + radius,
            )
            radius to rect
        }
        .zip(segmentDrawingOptions.colors)

    onDrawBehind {
        boxes.forEach { (dims, color) ->
            val (radius, rect) = dims
            if (segmentDrawingOptions.useHighlight) {
                drawArc(
                    color = color,
                    startAngle = angles.start.radiansToDegrees(),
                    sweepAngle = angles.run { end - start }.radiansToDegrees(),
                    useCenter = false,
                    topLeft = rect.topLeft,
                    size = rect.size,
                    style = stroke,
                )
            }
            repeat(5) { i ->
                val f = (i / 5f) + .1f
                val angle = angles.run { (1 - f) * start + f * end }
                val holeCenter = center + Offset.polarOffset(angle, radius)

                drawCircle(
                    color = Color.Black,
                    radius = holeRadius,
                    center = holeCenter,
                    style = Fill,
                )
            }
        }
    }
}

private fun CacheDrawScope.drawLineSegment(
    lineSegment: LineSegment,
    segmentDrawingOptions: SegmentDrawingOptions,
) = run {

    val lines = lineSegment
        .getLineEnds(segmentDrawingOptions)
        .map { points -> points.transform { point -> point.toOffset(this) } }
        .zip(segmentDrawingOptions.colors)
    val lineThickness = segmentDrawingOptions.lineThickness.toPx()
    val holeRadius = lineThickness / 2f

    onDrawBehind {
        lines.forEach { (endpoints, color) ->
            if (segmentDrawingOptions.useHighlight) {
                drawLine(
                    color = color,
                    start = endpoints.start,
                    end = endpoints.end,
                    strokeWidth = lineThickness,
                )
            }

            repeat(5) { i ->
                val f = (i / 5f) + .1f
                val holeCenter = endpoints.run { lerp(start, end, f) }
                drawCircle(
                    color = Color.Black,
                    radius = holeRadius,
                    center = holeCenter,
                    style = Fill,
                )
            }
        }
    }
}

fun CacheDrawScope.drawSeparatorSegment(
    separatorSegment: SeparatorSegment,
    segmentDrawingOptions: SegmentDrawingOptions,
) = run {

    val endpoints = separatorSegment
        .getSeparatorEnds(segmentDrawingOptions)
        .transform { point -> point.toOffset(this) }
    val lineThickness = segmentDrawingOptions.separatorThickness.toPx()

    onDrawBehind {
        drawLine(
            color = Color.Black,
            start = endpoints.start,
            end = endpoints.end,
            strokeWidth = lineThickness,
        )
    }
}