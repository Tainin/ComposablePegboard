package com.tainin.composablepegboard.pegboard.segments

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.toSize
import com.tainin.composablepegboard.model.Game
import com.tainin.composablepegboard.pegboard.options.StreetOptions

@Composable
fun WinSegment(
    modifier: Modifier,
    game: Game,
    boardOffset: Offset,
    streetOptions: StreetOptions,
    useHighlight: Boolean,
) {
    var segmentOffset by remember { mutableStateOf(Offset.Zero) }
    var segmentSize by remember { mutableStateOf(Size.Zero) }

    val localSegmentCenter = segmentSize.run { Offset(width / 2, height / 2) }
    val globalSegmentCenter = segmentOffset + localSegmentCenter
    val radius = segmentSize.minDimension / 2

    LaunchedEffect(globalSegmentCenter) {
        snapshotFlow { game.hasWinner() }.collect { _ ->
            game.withWinner { player ->
                when(player.score.pair.lead) {
                    player.score.pair.a -> player.pegPositions.a = globalSegmentCenter
                    player.score.pair.b -> player.pegPositions.b = globalSegmentCenter
                }
            }
        }
    }

    Box(
        modifier = modifier
            .clip(RectangleShape)
            .onPlaced {
                segmentOffset = it.positionInRoot() - boardOffset
                segmentSize = it.size.toSize()
            }
            .drawWithCache {
                onDrawBehind {
                    if (useHighlight) game.withWinner {
                        drawCircle(
                            color = it.color.highlightColor,
                            radius = radius,
                            center = center,
                            alpha = 1f,
                            style = Fill,
                        )
                    }
                    drawCircle(
                        color = Color.Black,
                        radius = streetOptions.lineThickness.toPx() / 2,
                        center = center,
                        alpha = 1f,
                        style = Fill,
                    )
                }
            }
    )
}