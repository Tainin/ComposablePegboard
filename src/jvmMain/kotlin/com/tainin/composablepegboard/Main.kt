package com.tainin.composablepegboard

import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import com.tainin.composablepegboard.model.Game
import com.tainin.composablepegboard.model.LineOrder
import com.tainin.composablepegboard.model.UserScoreInput
import com.tainin.composablepegboard.pegboard.boards.RectangularSpiralBoard
import com.tainin.composablepegboard.pegboard.options.StreetOptions
import com.tainin.composablepegboard.pegboard.overlays.PegsOverlay


@Composable
fun GameBoard(game: Game) {
    var boardOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .requiredHeight(IntrinsicSize.Min)
            .requiredWidth(IntrinsicSize.Min)
            .onPlaced {
                boardOffset = it.positionInRoot()
            }
    ) {
        RectangularSpiralBoard(
            boardOffset = boardOffset,
            streetOptions = StreetOptions(64.dp, 12.dp),
            streetSpacing = 68.dp,
            segmentGap = 12.dp,
            segmentAspectRatio = 2.5f,
            game = game,
            useHighlight = true,
        )
        PegsOverlay(
            game = game,
            animationSpec = tween(
                durationMillis = 1000,
                delayMillis = 200,
                easing = EaseInOutBack,
            ),
            pegSize = 20.dp,
        )
    }
}

@Composable
fun BoxScope.DebugScoreHistory(game: Game) = Column(
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

@Composable
fun BoxScope.UserScoreInputDialog(userScoreInput: UserScoreInput) {
    if (userScoreInput.hasSelectedPlayer) {
        Box(
            modifier = Modifier
                .requiredSize(800.dp, 300.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(32.dp))
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(64.dp)
                    .border(
                        width = 8.dp,
                        color = userScoreInput.selectedPlayer!!.color.pegColor,
                        shape = RoundedCornerShape(32.dp)
                    )
            )
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .requiredWidth(500.dp)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp),
                text = userScoreInput.inputValue.toString(),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun GameWindow(
    game: Game,
    userScoreInput: UserScoreInput,
) = Box(
    modifier = Modifier.fillMaxSize()
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .align(Alignment.BottomCenter)
            .padding(bottom = 128.dp)
    ) {
        GameBoard(game)
        UserScoreInputDialog(userScoreInput)
    }
    DebugScoreHistory(game)
}

@Composable
fun rememberGame() = remember {
    //Game(121, PlayerColor.Purple)
    //Game.makeTwoPlayerGame(121)
    Game.makeThreePlayerGame(121)
    //Game(121, PlayerColor.Red, PlayerColor.Green, PlayerColor.Blue, PlayerColor.Purple)
}

@Composable
fun rememberGameWindowState() = rememberWindowState(
    placement = WindowPlacement.Floating,
    isMinimized = false,
    position = WindowPosition(alignment = Alignment.Center),
    size = DpSize(1800.dp, 1000.dp)
)

@Composable
fun rememberUserScoreInput(game: Game) = remember { UserScoreInput(game) }

fun main() = application {
    val game = rememberGame()
    val windowState = rememberGameWindowState()
    val userScoreInput = rememberUserScoreInput(game)

    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        onKeyEvent = { keyEvent -> userScoreInput.applyKeyEvent(keyEvent) },
    ) {
        GameWindow(game, userScoreInput)
    }
}