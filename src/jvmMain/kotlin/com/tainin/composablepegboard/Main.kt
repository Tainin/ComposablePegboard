package com.tainin.composablepegboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.runtime.*
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.takeOrElse
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.tainin.composablepegboard.model.Game
import com.tainin.composablepegboard.model.LineOrder
import com.tainin.composablepegboard.pegboard.ArcSegment
import com.tainin.composablepegboard.pegboard.LinearSegment
import com.tainin.composablepegboard.pegboard.options.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestBoard(

) {
    val game = remember { Game.makeThreePlayerGame() }
    val streetOptions = remember { StreetOptions(78.dp, 18.dp) }
    var boardOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onPlaced {
                boardOffset = it.positionInRoot()
            }
            .onClick {
                game[LineOrder.Forward].random().score.plusAssign(listOf(1, 2, 3).random())
            }
    ) {
        LinearSegment(
            modifier = Modifier
                .requiredSize(streetOptions.streetWidth.let { DpSize(it * 3, it) })
                .align(AbsoluteAlignment.BottomRight),
            segmentIndex = 3,
            game = game,
            boardOffset = boardOffset,
            segmentDirection = LinearSegmentDirection.LeftToRight,
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
    Window(onCloseRequest = ::exitApplication) {
        TestBoard()
    }
}