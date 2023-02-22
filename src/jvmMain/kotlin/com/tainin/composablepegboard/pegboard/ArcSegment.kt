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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import com.tainin.composablepegboard.model.Game
import com.tainin.composablepegboard.pegboard.options.ArcSegmentOptions
import com.tainin.composablepegboard.pegboard.options.SegmentLineOptions
import com.tainin.composablepegboard.pegboard.options.StreetOptions
import com.tainin.composablepegboard.utils.transform
import com.tainin.composablepegboard.utils.unitFromAngle

@Composable
fun ArcSegment(
    modifier: Modifier = Modifier,
    segmentIndex: Int,
    game: Game,
    boardOffset: Offset,
    arcSegmentOptions: ArcSegmentOptions,
    streetOptions: StreetOptions,
    useHighlight: Boolean,
) {

    var segmentOffset by remember { mutableStateOf(Offset.Zero) }
    var segmentSize by remember { mutableStateOf(Size.Zero) }

    val (thickness, lineSpacing) = with(LocalDensity.current) {
        streetOptions.run { lineThickness.toPx() to lineSpacing(game.playerCount).transform { it.toPx() } }
    }

    val dimensions = arcSegmentOptions.run {
        ArcDimensions(
            segmentOffset = segmentOffset,
            center = focus.getCenterOffset(segmentSize),
            lineSpacing = lineSpacing,
            arcAngles = getAngles(),
            outerRadius = focus.getRadius(segmentSize),
            thickness = thickness,
        )
    }

    Box(
        modifier = modifier
            .clip(RectangleShape)
            .onPlaced {
                segmentOffset = it.positionInRoot() - boardOffset
                segmentSize = it.size.toSize()
            }
    ) {
        game[arcSegmentOptions.direction.lineOrder]
            .forEachIndexed { lineIndex, player ->
                val lineOptions = SegmentLineOptions(
                    segmentIndex = segmentIndex,
                    lineIndex = lineIndex,
                    player = player,
                )

                ArcSegmentLine(
                    lineOptions = lineOptions,
                    dimensions = dimensions,
                    useHighlight = useHighlight,
                )
            }
    }
}

@Composable
private fun ArcSegmentLine(
    lineOptions: SegmentLineOptions,
    dimensions: ArcDimensions,
    useHighlight: Boolean,
) {
    ScoreToPositionUpdater(
        keys = arrayOf(dimensions),
        lineOptions = lineOptions,
    ) { lineIndex, segmentOffset -> dimensions.globalHoleOffset(lineIndex, segmentOffset) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                val holePositions = List(5) { holeIndex ->
                    dimensions.localHoleOffset(lineOptions.lineIndex, holeIndex)
                }
                val radius = dimensions.lineRadius(lineOptions.lineIndex)
                val stroke = Stroke(dimensions.thickness)

                onDrawBehind {
                    if (useHighlight)
                        drawCircle(
                            color = lineOptions.player.color.highlightColor,
                            radius = radius,
                            center = dimensions.center,
                            alpha = 1f,
                            style = stroke
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

private class ArcDimensions(
    val segmentOffset: Offset,
    val center: Offset,
    val lineSpacing: Pair<Float, Float>,
    val arcAngles: Pair<Float, Float>,
    val outerRadius: Float,
    val thickness: Float,
) {
    fun lineRadius(lineIndex: Int) = outerRadius - lineSpacing.run { first + (second * lineIndex) }
    private fun holeAngle(holeIndex: Int): Float {
        val t = (2 * holeIndex + 1) / 10f
        return (1 - t) * arcAngles.first + t * arcAngles.second
    }

    fun localHoleOffset(lineIndex: Int, holeIndex: Int) =
        center + Offset.unitFromAngle(holeAngle(holeIndex)) * lineRadius(lineIndex)

    fun globalHoleOffset(lineIndex: Int, holeIndex: Int) = localHoleOffset(lineIndex, holeIndex) + segmentOffset
}