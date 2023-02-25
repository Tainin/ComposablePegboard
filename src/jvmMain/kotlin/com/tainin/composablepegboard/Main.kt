package com.tainin.composablepegboard

import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.tainin.composablepegboard.model.Game
import com.tainin.composablepegboard.model.LineOrder
import com.tainin.composablepegboard.pegboard.boards.RectangularSpiralBoard
import com.tainin.composablepegboard.pegboard.options.StreetOptions
import com.tainin.composablepegboard.pegboard.overlays.PegsOverlay

fun debugKeyEventHandler(game: Game, keyEvent: KeyEvent): Boolean {
    if (keyEvent.type != KeyEventType.KeyDown) return false
    val digit = keyEvent.key.nativeKeyCode.run {
        takeIf { it in (48L..57L) }?.minus(48) ?: return false
    }.dec()

    if (digit !in game[LineOrder.Forward].indices) return false

    game[LineOrder.Forward][digit].score.run {
        when {
            keyEvent.isShiftPressed -> pop()
            game.ongoing() -> plusAssign(listOf(1, 2, 3, 4, 5).random())
            else -> Unit
        }
    }

    return true
}

fun main() = application {
    val game = remember {
        //Game(PlayerColor.Purple)
        //Game.makeTwoPlayerGame()
        Game.makeThreePlayerGame()
        //Game(PlayerColor.Red, PlayerColor.Green, PlayerColor.Blue, PlayerColor.Purple)
    }

    val windowState = rememberWindowState(
        placement = WindowPlacement.Floating,
        isMinimized = false,
        position = WindowPosition(alignment = Alignment.Center),
        size = DpSize(1920.dp, 800.dp)
    )

    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        onKeyEvent = { keyEvent -> debugKeyEventHandler(game, keyEvent) },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            var boardOffset by remember { mutableStateOf(Offset.Zero) }

            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .align(AbsoluteAlignment.TopLeft)
            ) {
                game[LineOrder.Forward].forEach { player ->
                    Text(
                        text = player.score.history.take(10).joinToString(", ")
                    )
                }
            }

            Box(
                modifier = Modifier
                    .requiredHeight(IntrinsicSize.Min)
                    .requiredWidth(IntrinsicSize.Min)
                    .align(Alignment.Center)
                    .onPlaced {
                        boardOffset = it.positionInRoot()
                    }
            ) {
                RectangularSpiralBoard(
                    boardOffset = boardOffset,
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
                        durationMillis = 1000,
                        delayMillis = 0,
                        easing = EaseInOutBack,
                    ),
                    pegSize = 24.dp,
                )
            }
        }
    }
}