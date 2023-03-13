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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import com.tainin.composablepegboard.geometry.*
import com.tainin.composablepegboard.model.Game
import com.tainin.composablepegboard.model.LineOrder
import com.tainin.composablepegboard.model.UserScoreInput
import com.tainin.composablepegboard.pegboard.boards.RectangularSpiralBoard
import com.tainin.composablepegboard.pegboard.boards.SquareBoard
import com.tainin.composablepegboard.pegboard.options.StreetOptions
import com.tainin.composablepegboard.pegboard.overlays.PegsOverlay
import com.tainin.composablepegboard.utils.FloatTAU
import com.tainin.composablepegboard.utils.toInputAction
import com.tainin.composablepegboard.utils.topLeft


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
        val segments = sequenceOf(
            /*
            LineSegment(-FloatHalfPI, 150.dp),
            ArcSegment(-FloatHalfPI, -FloatHalfPI, 80.dp),
            LineSegment(-FloatPI, 150.dp),
            ArcSegment(-FloatPI, -FloatHalfPI, 80.dp),
            LineSegment(FloatHalfPI, 600.dp),
            ArcSegment(FloatHalfPI, -FloatHalfPI, 80.dp),
            LineSegment(0f, 600.dp),
            ArcSegment(0f, -FloatHalfPI, 80.dp),
            LineSegment(-FloatHalfPI, 600.dp),
            ArcSegment(-FloatHalfPI, -FloatHalfPI, 80.dp),
            LineSegment(-FloatPI, 150.dp),
            ArcSegment(-FloatPI, -FloatHalfPI, 80.dp),
            LineSegment(FloatHalfPI, 150.dp),
            ArcSegment(FloatHalfPI, FloatHalfPI, 320.dp)
            */

            ArcSegment(16 * FloatTAU / 24, 17 * FloatTAU / 24, 200.dp),
            LineSegment(9 * FloatTAU / 24, 300.dp),
            ArcSegment(9 * FloatTAU / 24, -9 * FloatTAU / 24, 100.dp),
            LineSegment(0 * FloatTAU / 24, 500.dp),
            ArcSegment(0 * FloatTAU / 24, -6 * FloatTAU / 24, 100.dp),
            LineSegment(-6 * FloatTAU / 24, 500.dp),
            ArcSegment(-6 * FloatTAU / 24, -6 * FloatTAU / 24, 100.dp),
            ArcSegment(-12 * FloatTAU / 24, -12 * FloatTAU / 24, 200.dp),


            /*
            ArcSegment(-FloatHalfPI, FloatHalfPI, 200.dp),
            LineSegment(0f, 500.dp),
            ArcSegment(0f, -FloatHalfPI, 200.dp),
            */
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val segmentDrawingOptions = SegmentDrawingOptions(
                streetWidth = 72.dp,
                lineThickness = 16.dp,
                colors = game[LineOrder.Forward]
                    .asSequence()
                    .map { player -> player.color.highlightColor }
            )

            testLayout(
                modifier = Modifier
                    .wrapContentSize(
                        align = Alignment.Center,
                        unbounded = true
                    )
                    .align(Alignment.Center)
                    .background(Color.Gray)
                    .padding(segmentDrawingOptions.run { (streetWidth + lineThickness) / 2 }),
                segments = segments,
                segmentDrawingOptions = segmentDrawingOptions,
            )
        }
    }
}

@Composable
fun testLayout(
    modifier: Modifier = Modifier,
    segments: Sequence<Segment>,
    segmentDrawingOptions: SegmentDrawingOptions,
) = Layout(
    content = {
        segments.forEach { segment ->
            Box(
                modifier = Modifier
                    .requiredSize(segment.size)
                    .drawWithCache { drawSegment(segment, segmentDrawingOptions) }
            )
        }
    },
    modifier = modifier,
) { measurables, constraints ->
    var bounds = DpRect(DpOffset.Zero, DpSize.Zero)
    var anchor = DpOffset.Zero

    val placements = segments
        .zip(measurables.asSequence())
        .map {
            val rect = DpRect(
                anchor - it.first.positions.start,
                it.first.size,
            )

            bounds = DpRect(
                left = min(bounds.left, rect.left),
                top = min(bounds.top, rect.top),
                right = max(bounds.right, rect.right),
                bottom = max(bounds.bottom, rect.bottom),
            )

            anchor = rect.topLeft + it.first.positions.end

            rect.topLeft to it.second.measure(constraints)
        }.toList()

    layout(bounds.width.roundToPx(), bounds.height.roundToPx()) {
        placements.forEach { (offset, placeable) ->
            val position = offset - bounds.topLeft
            position.run { placeable.place(x.roundToPx(), y.roundToPx()) }
        }
    }
}