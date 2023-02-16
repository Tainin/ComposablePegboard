package com.tainin.composablepegboard.pegboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import com.tainin.composablepegboard.model.Game
import com.tainin.composablepegboard.model.Player
import com.tainin.composablepegboard.pegboard.options.LinearSegmentDirection
import com.tainin.composablepegboard.pegboard.options.StreetOptions

@Composable
fun LinearSegment(
    modifier: Modifier = Modifier,
    segmentIndex: Int,
    game: Game,
    boardOffset: Offset,
    segmentDirection: LinearSegmentDirection,
    streetOptions: StreetOptions,
    useHighlight: Boolean,
) {

    var segmentOffset by remember { mutableStateOf(Offset.Zero) }
    var segmentSize by remember { mutableStateOf(Size.Zero) }

    val dimensions = BoxDimensions(
        offset = segmentOffset,
        width = segmentSize.width,
        startX = segmentSize.width / 10,
        stepX = segmentSize.width / 5,
    )

    val insets = streetOptions.getLineInsets(game.playerCount)

    Box(
        modifier = modifier
            .clip(RectangleShape)
            .onPlaced {
                segmentOffset = it.positionInRoot() - boardOffset
                segmentSize = it.size.toSize()
            }
    ) {
        val density = LocalDensity.current

        val (yOffsets, lineThickness) = with(density) {
            insets.map { it.toPx() } to streetOptions.lineThickness.toPx()
        }

        yOffsets
            .zip(game[segmentDirection.lineOrder].asSequence())
            .forEach { line ->
                Line(
                    player = line.second,
                    segmentIndex = segmentIndex,
                    dimensions = LineDimensions(dimensions, line.first),
                    lineThickness = lineThickness,
                    useHighlight = useHighlight,
                )
            }
    }
}

@Composable
private fun Line(
    player: Player,
    segmentIndex: Int,
    dimensions: LineDimensions,
    lineThickness: Float,
    useHighlight: Boolean,
) {
    ScoreToPositionUpdater(
        keys = arrayOf(),
        player = player,
        segmentIndex = segmentIndex,
    ) { i -> dimensions.boardOffset(i) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                val holePositions = List(5) { i -> dimensions.boxOffset(i) }
                val holeRadius = lineThickness / 2
                val line = dimensions.run {
                    Offset(0f, yOffset) to Offset(box.width, yOffset)
                }

                onDrawBehind {
                    if (useHighlight)
                        drawLine(
                            color = player.color.highlightColor,
                            start = line.first,
                            end = line.second,
                            strokeWidth = lineThickness,
                            alpha = 1f
                        )
                    holePositions.forEach { position ->
                        drawCircle(
                            color = Color.Black,
                            radius = holeRadius,
                            center = position,
                            alpha = 1f,
                            style = Fill
                        )
                    }
                }
            }
    )
}

private class BoxDimensions(val offset: Offset, val width: Float, val startX: Float, val stepX: Float)
private class LineDimensions(val box: BoxDimensions, val yOffset: Float) {
    fun boxOffset(index: Int) = box.run {
        stepX.times(index).plus(startX)
    }.let { Offset(it, yOffset) }

    fun boardOffset(index: Int) = boxOffset(index).plus(box.offset)
}