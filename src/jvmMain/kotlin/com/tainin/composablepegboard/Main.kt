package com.tainin.composablepegboard

import androidx.compose.animation.core.EaseInQuint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.tainin.composablepegboard.model.Game
import com.tainin.composablepegboard.model.LineOrder
import com.tainin.composablepegboard.pegboard.boards.RectangularSpiralBoard
import com.tainin.composablepegboard.pegboard.options.StreetOptions
import com.tainin.composablepegboard.pegboard.overlays.PegsOverlay

fun main() = application {
    val game = remember {
        //Game(PlayerColor.Red)
        //Game.makeTwoPlayerGame()
        Game.makeThreePlayerGame()
        //Game(PlayerColor.Red, PlayerColor.Green, PlayerColor.Blue, PlayerColor.Purple)
    }

    Window(
        onCloseRequest = ::exitApplication,
        onKeyEvent = { keyEvent ->
            if (keyEvent.type != KeyEventType.KeyDown) return@Window false
            val digit = keyEvent.key.nativeKeyCode.run {
                takeIf { it in (48L..57L) }?.minus(48) ?: return@Window false
            }

            game[LineOrder.Forward][digit].score += listOf(1,2,3,4,5).random()

            true
        }
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
        ) {
            RectangularSpiralBoard(
                boardOffset = Offset.Zero,
                streetOptions = StreetOptions(72.dp, 16.dp),
                streetSpacing = 64.dp,
                segmentGap = 16.dp,
                segmentAspectRatio = 2.5f,
                game = game,
                useHighlight = true,
            )
            PegsOverlay(
                game = game,
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = 0,
                    easing = EaseInQuint,
                ),
                pegSize = 12.dp,
            )
        }
    }
}