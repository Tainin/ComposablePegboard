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
import com.tainin.composablepegboard.model.Player
import com.tainin.composablepegboard.pegboard.options.ArcSegmentOptions
import com.tainin.composablepegboard.pegboard.options.StreetOptions
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

    val maxRadius = arcSegmentOptions.focus.getRadius(segmentSize)
    val (start, sweep) = arcSegmentOptions.getAngles()
    val dimensions = SweepDimensions(
        center = arcSegmentOptions.focus.getCenterOffset(segmentSize),
        startAngle = start + sweep / 10,
        stepAngle = sweep / 5,
        offset = segmentOffset,
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

        val (radii, lineThickness) = with(density) {
            insets.map { maxRadius - it.toPx() } to streetOptions.lineThickness.toPx()
        }

        radii
            .zip(game[arcSegmentOptions.direction.lineOrder].asSequence())
            .forEach { line ->
                ArcLine(
                    segmentIndex = segmentIndex,
                    player = line.second,
                    dimensions = ArcDimensions(dimensions, line.first),
                    lineThickness = lineThickness,
                    useHighlight = useHighlight,
                )
            }
    }
}

@Composable
private fun ArcLine(
    player: Player,
    segmentIndex: Int,
    dimensions: ArcDimensions,
    lineThickness: Float,
    useHighlight: Boolean,
) {
    ScoreToPositionUpdater(
        keys = arrayOf(dimensions),
        player = player,
        segmentIndex = segmentIndex
    ) { i -> dimensions.segmentOffset(i) + dimensions.sweep.offset }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                val holePositions = List(5) { i -> dimensions.segmentOffset(i) }
                val holeRadius = lineThickness / 2
                val stroke = Stroke(lineThickness)

                onDrawBehind {
                    if (useHighlight)
                        drawCircle(
                            color = player.color.highlightColor,
                            radius = dimensions.radius,
                            center = dimensions.sweep.center,
                            alpha = 1f,
                            style = stroke
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

private class SweepDimensions(val offset: Offset, val center: Offset, val startAngle: Float, val stepAngle: Float)
private class ArcDimensions(val sweep: SweepDimensions, val radius: Float) {
    fun arcOffset(index: Int) = Offset.unitFromAngle(
        sweep.run {
            stepAngle.times(index).plus(startAngle)
        }
    ).times(radius)

    fun segmentOffset(index: Int) = arcOffset(index).plus(sweep.center)
}