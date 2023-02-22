package com.tainin.composablepegboard.pegboard.overlays

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.takeOrElse
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import com.tainin.composablepegboard.model.Game
import com.tainin.composablepegboard.model.LineOrder
import com.tainin.composablepegboard.model.Player

@Composable
fun PegsOverlay(
    game: Game,
    animationSpec: AnimationSpec<Offset>,
    pegSize: Dp,
) {
    game[LineOrder.Forward].forEach { player ->
        val positionA by animateOffsetAsState(
            targetValue = player.pegPositions.a.takeOrElse { Offset.Zero },
            animationSpec = animationSpec,
        )
        val positionB by animateOffsetAsState(
            targetValue = player.pegPositions.b.takeOrElse { Offset.Zero },
            animationSpec = animationSpec,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    val radius = pegSize.toPx() / 2f

                    onDrawBehind {
                        drawPeg(player, positionA, radius)
                        drawPeg(player, positionB, radius)
                    }
                }
        )
    }
}

private fun DrawScope.drawPeg(
    player: Player,
    offset: Offset,
    radius: Float,
) = drawCircle(
    color = player.color.pegColor,
    radius = radius,
    center = offset,
    alpha = 1f,
    style = Fill,
)