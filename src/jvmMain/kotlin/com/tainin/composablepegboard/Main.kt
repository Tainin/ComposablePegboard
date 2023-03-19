package com.tainin.composablepegboard

import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import com.tainin.composablepegboard.geometry.drawing.SegmentDrawingOptions
import com.tainin.composablepegboard.geometry.path.SegmentPath
import com.tainin.composablepegboard.model.Game
import com.tainin.composablepegboard.model.LineOrder
import com.tainin.composablepegboard.model.UserScoreInput
import com.tainin.composablepegboard.pegboard.boards.RectangularSpiralBoard
import com.tainin.composablepegboard.pegboard.boards.SquareBoard
import com.tainin.composablepegboard.pegboard.options.StreetOptions
import com.tainin.composablepegboard.pegboard.overlays.PegsOverlay
import com.tainin.composablepegboard.utils.FloatTAU
import com.tainin.composablepegboard.utils.toInputAction


@Composable
fun BoardWithPegs(
    game: Game,
    board: @Composable (boardOffset: Offset) -> Unit,
) {
    var boardOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .requiredWidth(IntrinsicSize.Min)
            .requiredHeight(IntrinsicSize.Min)
            .onPlaced {
                boardOffset = it.positionInRoot()
            }
    ) {
        board(boardOffset)
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
fun PlayerScoreDisplay(
    frameColor: Color,
    scoreText: String,
) = Box(
    modifier = Modifier
        .requiredSize(250.dp, 80.dp)
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .border(
                width = 8.dp,
                color = frameColor,
                shape = CircleShape
            )
    )
    Text(
        modifier = Modifier
            .wrapContentHeight()
            .requiredWidth(100.dp)
            .align(Alignment.Center)
            .border(
                width = 4.dp,
                color = Color.Black,
                shape = CircleShape
            )
            .clip(CircleShape)
            .background(Color.White)
            .padding(16.dp),
        text = scoreText,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun HorizontalGameWindow(
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
        BoardWithPegs(
            game = game,
        ) { boardOffset ->
            RectangularSpiralBoard(
                boardOffset = boardOffset,
                streetOptions = StreetOptions(64.dp, 12.dp),
                streetSpacing = 68.dp,
                segmentGap = 12.dp,
                segmentAspectRatio = 2.5f,
                game = game,
                useHighlight = true,
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .align(Alignment.TopCenter),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            game[LineOrder.Forward].forEach {
                PlayerScoreDisplay(
                    it.color.pegColor,
                    it.score.pair.lead.toString(),
                )
            }
        }
        userScoreInput.selectedPlayer?.let {
            PlayerScoreDisplay(
                it.color.pegColor,
                userScoreInput.inputValue.toString(),
            )
        }
    }
}

@Composable
fun SquareGameWindow(
    game: Game,
    userScoreInput: UserScoreInput,
) = Box(
    modifier = Modifier
        .fillMaxSize()
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .align(AbsoluteAlignment.CenterLeft)
            .absolutePadding(left = 128.dp)
    ) {
        BoardWithPegs(
            game = game,
        ) { boardOffset ->
            SquareBoard(
                boardOffset = boardOffset,
                streetOptions = StreetOptions(64.dp, 12.dp),
                segmentGap = 12.dp,
                segmentAspectRatio = 1.75f,
                game = game,
                useHighlight = true,
            )
        }
    }
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth()
            .align(AbsoluteAlignment.CenterRight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        userScoreInput.selectedPlayer?.let {
            PlayerScoreDisplay(
                it.color.pegColor,
                userScoreInput.inputValue.toString(),
            )
        }
        Column(
            modifier = Modifier
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            game[LineOrder.Forward].forEach {
                PlayerScoreDisplay(
                    it.color.pegColor,
                    it.score.pair.lead.toString(),
                )
            }
        }
    }
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
        onKeyEvent = { keyEvent -> userScoreInput.applyInputAction(keyEvent.toInputAction()) },
    ) {
        //SquareGameWindow(game, userScoreInput)
        //HorizontalGameWindow(game, userScoreInput)
        //return@Window
        val path = arrayOf(
            SegmentPath(16.dp).apply {
                startNewPath(DpOffset.Zero, 88 * FloatTAU / 24)
                addArcSegment(17 * FloatTAU / 144, 200.dp, 6)
                addLineSegment(120.dp, 2)
                addArcSegment(-9 * FloatTAU / 48, 100.dp, 2)
                addLineSegment(120.dp, 4)
                addArcSegment(-6 * FloatTAU / 24, 100.dp)
                addLineSegment(120.dp, 4)
                addArcSegment(-6 * FloatTAU / 24, 100.dp)
                addArcSegment(-12 * FloatTAU / 96, 200.dp, 4)
                shiftToOrigin()
            },
            SegmentPath(16.dp).apply {
                startNewPath(DpOffset(300.dp, 50.dp), 5 * FloatTAU / 8)
                addArcSegment(-2 * FloatTAU / (8 * 12), 650.dp, 12)
                startNewPath(DpOffset((-300).dp, (-50).dp), 1 * FloatTAU / 8)
                addArcSegment(-2 * FloatTAU / (8 * 12), 650.dp, 12)
                shiftToOrigin()
            },
            SegmentPath(16.dp).apply {
                startNewPath(DpOffset.Zero, -1 * FloatTAU / 8)
                addArcSegment(2 * FloatTAU / (8 * 11), 700.dp, 11)
                addArcSegment(2 * FloatTAU / (8 * 2), 130.dp, 2)
                addArcSegment(2 * FloatTAU / (8 * 11), 700.dp, 11)

                shiftToOrigin()
            },
        )[0]

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val segmentDrawingOptions = SegmentDrawingOptions(
                streetWidth = 52.dp,
                lineThickness = 13.dp,
                separatorThickness = 5.dp,
                separatorLabelDistance = 58.dp,
                colors = game[LineOrder.Forward]
                    .asSequence()
                    .map { player -> player.color.highlightColor }
            )

            SegmentPath(
                path = path,
                segmentDrawingOptions = segmentDrawingOptions,
            )
        }
    }
}

@Composable
fun BoxScope.SegmentPath(
    path: SegmentPath,
    segmentDrawingOptions: SegmentDrawingOptions,
) = Box(
    modifier = Modifier
        .padding(segmentDrawingOptions.run { (streetWidth + lineThickness) / 2 })
        .requiredSize(path.bounds.size)
        .align(Alignment.Center)
) {
    path.scoringParts.forEach { part ->
        Box(
            modifier = Modifier
                .requiredWidth(max(part.segment.size.width, 1.dp))
                .requiredHeight(max(part.segment.size.height, 1.dp))
                .offset { part.topLeft.run { IntOffset(x.roundToPx(), y.roundToPx()) } }
                .drawWithCache { onDrawBehind { /*New drawing system in progress*/ } }
        )
    }

    var separatorIndex = 0
    path.separatorParts.forEach { part ->
        Box(
            modifier = Modifier
                .requiredWidth(max(part.segment.size.width, 1.dp))
                .requiredHeight(max(part.segment.size.height, 1.dp))
                .offset { part.topLeft.run { IntOffset(x.roundToPx(), y.roundToPx()) } }
                .drawWithCache { onDrawBehind { /*New drawing system in progress*/ } }
        ) {
            separatorIndex++

            val separatorEndOffset = part.segment.getSeparatorLabelOffset(segmentDrawingOptions)
            Text(
                modifier = Modifier
                    .wrapContentSize(unbounded = true)
                    .align(Alignment.Center)
                    .offset { separatorEndOffset.run { IntOffset(x.roundToPx(), y.roundToPx()) } },
                text = separatorIndex.times(5).takeIf { it != 90 }?.toString() ?: "S",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}