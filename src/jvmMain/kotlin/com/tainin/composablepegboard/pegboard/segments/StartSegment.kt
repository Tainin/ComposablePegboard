package com.tainin.composablepegboard.pegboard.segments

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
import com.tainin.composablepegboard.pegboard.effects.StartPositionUpdater
import com.tainin.composablepegboard.pegboard.options.LinearSegmentDirection
import com.tainin.composablepegboard.pegboard.options.SegmentLineOptions
import com.tainin.composablepegboard.pegboard.options.StreetOptions
import com.tainin.composablepegboard.utils.transform


@Composable
fun StartSegment(
    modifier: Modifier = Modifier,
    game: Game,
    boardOffset: Offset,
    segmentDirection: LinearSegmentDirection,
    streetOptions: StreetOptions,
    useHighlight: Boolean,
) {

    var segmentOffset by remember { mutableStateOf(Offset.Zero) }
    var segmentSize by remember { mutableStateOf(Size.Zero) }

    val (thickness, lineSpacing) = with(LocalDensity.current) {
        streetOptions.run { lineThickness.toPx() to lineSpacing(game.playerCount).transform { it.toPx() } }
    }

    val lineEnds = when (segmentDirection) {
        LinearSegmentDirection.North -> segmentSize.height to 0f
        LinearSegmentDirection.South -> 0f to segmentSize.height
        LinearSegmentDirection.East -> 0f to segmentSize.width
        LinearSegmentDirection.West -> segmentSize.width to 0f
    }

    val dimensions = StartDimensions(
        segmentOffset = segmentOffset,
        segmentDirection = segmentDirection,
        lineSpacing = lineSpacing,
        lineEnds = lineEnds,
        thickness = thickness,
    )

    Box(
        modifier = modifier
            .clip(RectangleShape)
            .onPlaced {
                segmentOffset = it.positionInRoot() - boardOffset
                segmentSize = it.size.toSize()
            }
    ) {
        game[segmentDirection.lineOrder]
            .forEachIndexed { lineIndex, player ->
                val lineOptions = SegmentLineOptions(
                    segmentIndex = -1,
                    lineIndex = lineIndex,
                    player = player,
                )

                StartSegmentLine(
                    lineOptions = lineOptions,
                    dimensions = dimensions,
                    useHighlight = useHighlight,
                )
            }
    }
}

@Composable
private fun StartSegmentLine(
    lineOptions: SegmentLineOptions,
    dimensions: StartDimensions,
    useHighlight: Boolean,
) {

    StartPositionUpdater(
        keys = arrayOf(dimensions),
        lineOptions = lineOptions,
    ) { lineIndex, segmentOffset -> dimensions.globalHoleOffset(lineIndex, segmentOffset) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                val holePositions = List(2) { holeIndex ->
                    dimensions.localHoleOffset(lineOptions.lineIndex, holeIndex)
                }
                val line = dimensions.lineEnds(lineOptions.lineIndex)

                onDrawBehind {
                    if (useHighlight)
                        drawLine(
                            color = lineOptions.player.color.highlightColor,
                            start = line.first,
                            end = line.second,
                            strokeWidth = dimensions.thickness,
                            alpha = 1f
                        )
                    holePositions.forEach { position ->
                        drawCircle(
                            color = Color.Black,
                            radius = dimensions.thickness / 2,
                            center = position,
                            alpha = 1f,
                            style = Fill
                        )
                    }
                }
            }
    )
}

private class StartDimensions(
    val segmentOffset: Offset,
    val segmentDirection: LinearSegmentDirection,
    val lineSpacing: Pair<Float, Float>,
    val lineEnds: Pair<Float, Float>,
    val thickness: Float,
) {
    private fun lineDistance(lineIndex: Int) = lineSpacing.run { first + (second * lineIndex) }

    private fun holeDistance(holeIndex: Int): Float {
        val t = (2 * holeIndex + 1) / 4f
        return (1 - t) * lineEnds.first + t * lineEnds.second
    }

    fun localHoleOffset(lineIndex: Int, holeIndex: Int) =
        segmentDirection.run { peggingAxis * holeDistance(holeIndex) + crossAxis * lineDistance(lineIndex) }

    fun lineEnds(lineIndex: Int) =
        lineEnds.transform { end -> segmentDirection.run { peggingAxis * end + crossAxis * lineDistance(lineIndex) } }

    fun globalHoleOffset(lineIndex: Int, holeIndex: Int) = localHoleOffset(lineIndex, holeIndex) + segmentOffset
}