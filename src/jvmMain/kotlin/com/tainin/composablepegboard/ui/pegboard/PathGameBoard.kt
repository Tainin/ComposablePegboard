package com.tainin.composablepegboard.ui.pegboard

import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tainin.composablepegboard.geometry.drawing.SegmentDrawingOptions
import com.tainin.composablepegboard.geometry.path.SegmentPath
import com.tainin.composablepegboard.model.LineOrder
import com.tainin.composablepegboard.utils.toOffset

@Composable
fun PathGameBoard(
    modifier: Modifier = Modifier,
    segmentDrawingOptions: SegmentDrawingOptions,
    path: SegmentPath,
) = Box(
    modifier = modifier
) {
    val startPart = path.startPart.let { part ->
        val drawable = part.segment.getDrawable(
            segmentDrawingOptions = segmentDrawingOptions,
            density = LocalDensity.current,
        )
        val size = part.segment.size
        val topLeft = part.topLeft
        Triple(topLeft, size, drawable)
    }

    val scoringParts = path.scoringParts.map { part ->
        val drawable = part.segment.getDrawable(
            segmentDrawingOptions = segmentDrawingOptions,
            density = LocalDensity.current,
        )
        val size = part.segment.size
        val topLeft = part.topLeft
        Triple(topLeft, size, drawable)
    }

    @Composable
    fun getPegOffset(lineIndex: Int, score: Int) = run {
        val partIndex = score / 5
        var holeIndex = score % 5

        val part = when {
            score < 0 -> {
                holeIndex += 2
                startPart
            }

            else -> scoringParts.getOrNull(partIndex)
        }

        val insideOffset = part
            ?.third
            ?.getOrNull(lineIndex)
            ?.getHoleOffset(holeIndex)
            ?: Offset.Zero

        val outsideOffset = part
            ?.first
            ?.toOffset(LocalDensity.current)
            ?: Offset.Zero

        outsideOffset + insideOffset
    }

    startPart.let { (topLeft, size, drawable) ->
        SegmentWidget(topLeft, size, drawable)
    }

    scoringParts.forEach { (topLeft, size, drawable) ->
        SegmentWidget(topLeft, size, drawable)
    }

    var separatorIndex = 0
    path.separatorParts.forEach { part ->
        val drawable = part.segment.getDrawable(
            segmentDrawingOptions = segmentDrawingOptions,
            density = LocalDensity.current
        )

        SeparatorWidget(
            topLeft = part.topLeft,
            size = part.segment.size,
            drawable = drawable,
            separatorIndex = separatorIndex,
            indexToText = { index -> index.times(5).takeIf { it != 90 }?.toString() ?: "S" },
            style = TextStyle(Color.Black, 18.sp, FontWeight.Bold),
            labelOffset = part.segment.getSeparatorLabelOffset(segmentDrawingOptions),
        )
        separatorIndex++
    }

    segmentDrawingOptions.game[LineOrder.Forward].forEachIndexed { i, player ->
        val spec = tween<Offset>(
            durationMillis = 1000,
            delayMillis = 200,
            easing = EaseInOutBack,
        )

        val offsetA by animateOffsetAsState(
            targetValue = getPegOffset(i, player.score.pair.a.dec()),
            animationSpec = spec,
        )
        val offsetB by animateOffsetAsState(
            targetValue = getPegOffset(i, player.score.pair.b.dec()),
            animationSpec = spec,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        color = player.color.pegColor,
                        center = offsetA,
                        radius = 25f,
                        style = Fill,
                    )
                    drawCircle(
                        color = player.color.pegColor,
                        center = offsetB,
                        radius = 25f,
                        style = Fill,
                    )
                }
        )
    }
}