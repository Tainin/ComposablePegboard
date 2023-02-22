package com.tainin.composablepegboard

import androidx.compose.animation.core.EaseInQuint
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.takeOrElse
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.tainin.composablepegboard.model.Game
import com.tainin.composablepegboard.model.LineOrder
import com.tainin.composablepegboard.pegboard.options.*
import com.tainin.composablepegboard.pegboard.segments.ArcSegment
import com.tainin.composablepegboard.pegboard.segments.LinearSegment

private const val LINEAR_SEGMENT_SCALE = 2.5f

@Composable
fun TestBoard(
    game: Game,
) {
    val streetOptions = remember { StreetOptions(78.dp, 18.dp) }
    //val streetOptions = remember { StreetOptions(64.dp, 16.dp) }
    var boardOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(145,90,60,255))
            .onPlaced {
                boardOffset = it.positionInRoot()
            }
    ) {
        LinearSegment(
            modifier = Modifier
                .requiredSize(streetOptions.streetWidth.let { DpSize(it * LINEAR_SEGMENT_SCALE, it) })
                .align(AbsoluteAlignment.BottomRight),
            segmentIndex = 3,
            game = game,
            boardOffset = boardOffset,
            segmentDirection = LinearSegmentDirection.West,
            streetOptions = streetOptions,
            useHighlight = true,
        )
        LinearSegment(
            modifier = Modifier
                .requiredSize(streetOptions.streetWidth.let { DpSize(it, it * LINEAR_SEGMENT_SCALE) })
                .align(AbsoluteAlignment.BottomLeft),
            segmentIndex = 4,
            game = game,
            boardOffset = boardOffset,
            segmentDirection = LinearSegmentDirection.North,
            streetOptions = streetOptions,
            useHighlight = true,
        )
        LinearSegment(
            modifier = Modifier
                .requiredSize(streetOptions.streetWidth.let { DpSize(it * LINEAR_SEGMENT_SCALE, it) })
                .align(AbsoluteAlignment.TopLeft),
            segmentIndex = 5,
            game = game,
            boardOffset = boardOffset,
            segmentDirection = LinearSegmentDirection.East,
            streetOptions = streetOptions,
            useHighlight = true,
        )
        LinearSegment(
            modifier = Modifier
                .requiredSize(streetOptions.streetWidth.let { DpSize(it, it * LINEAR_SEGMENT_SCALE) })
                .align(AbsoluteAlignment.TopRight),
            segmentIndex = 6,
            game = game,
            boardOffset = boardOffset,
            segmentDirection = LinearSegmentDirection.South,
            streetOptions = streetOptions,
            useHighlight = true,
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.5f)
                .align(AbsoluteAlignment.CenterLeft)
        ) {
            ArcSegment(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f),
                segmentIndex = 0,
                game = game,
                boardOffset = boardOffset,
                arcSegmentOptions = ArcSegmentOptions(ArcFocus.BottomRight, ArcDirection.AntiClockwise),
                streetOptions = streetOptions,
                useHighlight = true,
            )

            ArcSegment(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f),
                segmentIndex = 1,
                game = game,
                boardOffset = boardOffset,
                arcSegmentOptions = ArcSegmentOptions(ArcFocus.TopRight, ArcDirection.AntiClockwise),
                streetOptions = streetOptions,
                useHighlight = true,
            )
        }
        ArcSegment(
            modifier = Modifier
                .fillMaxHeight(.5f)
                .fillMaxWidth(.5f)
                .align(AbsoluteAlignment.BottomRight),
            segmentIndex = 2,
            game = game,
            boardOffset = boardOffset,
            arcSegmentOptions = ArcSegmentOptions(ArcFocus.Left, ArcDirection.AntiClockwise),
            streetOptions = streetOptions,
            useHighlight = true,
        )

        val animationSpec = tween<Offset>(
            durationMillis = 500,
            delayMillis = 0,
            easing = EaseInQuint,
        )

        game[LineOrder.Forward].forEach { player ->
            val aPosition by animateOffsetAsState(
                targetValue = player.pegPositions.a.takeOrElse { Offset.Zero },
                animationSpec = animationSpec,
            )

            val bPosition by animateOffsetAsState(
                targetValue = player.pegPositions.b.takeOrElse { Offset.Zero },
                animationSpec = animationSpec,
            )

            fun DrawScope.drawPeg(offset: Offset) = drawCircle(
                player.color.pegColor,
                12f,
                offset,
                1f,
                Fill
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind { drawPeg(aPosition); drawPeg(bPosition) }
            )
        }
    }
}

fun main() = application {
    val game = remember {
        //Game(PlayerColor.Red)
        //Game.makeTwoPlayerGame()
        Game.makeThreePlayerGame()
        //Game(PlayerColor.Red, PlayerColor.Green, PlayerColor.Blue, PlayerColor.Purple, PlayerColor.Yellow)
    }

    Window(
        onCloseRequest = ::exitApplication,
        onKeyEvent = { keyEvent ->
            if (keyEvent.type != KeyEventType.KeyDown) return@Window false
            val digit = keyEvent.key.nativeKeyCode.run {
                takeIf { it in (48L..57L) }?.minus(48) ?: return@Window false
            }

            game[LineOrder.Forward][digit].score += listOf(1).random()

            true
        }
    ) {
        TestBoard(game = game)
    }
}