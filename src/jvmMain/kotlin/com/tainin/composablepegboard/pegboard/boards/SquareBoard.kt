package com.tainin.composablepegboard.pegboard.boards

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tainin.composablepegboard.model.Game
import com.tainin.composablepegboard.model.LineOrder
import com.tainin.composablepegboard.pegboard.options.*
import com.tainin.composablepegboard.pegboard.segments.*

@Composable
fun SquareBoard(
    boardOffset: Offset,
    streetOptions: StreetOptions,
    segmentGap: Dp,
    segmentAspectRatio: Float,
    game: Game,
    useHighlight: Boolean,
) {
    val segmentSize = streetOptions.streetWidth * segmentAspectRatio

    @Composable
    fun segmentSeparator(
        segmentIndex: Int,
        labelPosition: LinearSegmentDirection,
    ) = SegmentSeparator(
        modifier = Modifier
            .requiredSize(
                when (labelPosition) {
                    LinearSegmentDirection.North, LinearSegmentDirection.South ->
                        DpSize(segmentGap, streetOptions.streetWidth)

                    LinearSegmentDirection.East, LinearSegmentDirection.West ->
                        DpSize(streetOptions.streetWidth, segmentGap)
                }
            ),
        labelPosition = labelPosition,
        lineThickness = segmentGap / 3,
    ) {
        Text(
            text = segmentIndex.takeUnless { it == 18 }?.times(5)?.toString() ?: "S",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }

    @Composable
    fun cornerSegment(
        segmentIndex: Int,
        arcFocus: ArcFocus,
    ) = ArcSegment(
        modifier = Modifier
            .requiredSize(segmentSize),
        segmentIndex = segmentIndex,
        game = game,
        boardOffset = boardOffset,
        arcSegmentOptions = ArcSegmentOptions(focus = arcFocus, direction = ArcDirection.AntiClockwise),
        streetOptions = streetOptions,
        useHighlight = useHighlight,
    )

    @Composable
    fun sideSegment(
        segmentIndex: Int,
        segmentDirection: LinearSegmentDirection,
    ) = LinearSegment(
        modifier = Modifier
            .requiredSize(
                when (segmentDirection) {
                    LinearSegmentDirection.North, LinearSegmentDirection.South ->
                        DpSize(streetOptions.streetWidth, segmentSize)

                    LinearSegmentDirection.East, LinearSegmentDirection.West ->
                        DpSize(segmentSize, streetOptions.streetWidth)
                }
            ),
        segmentIndex = segmentIndex,
        game = game,
        boardOffset = boardOffset,
        segmentDirection = segmentDirection,
        streetOptions = streetOptions,
        useHighlight = useHighlight,
    )

    @Composable
    fun startArea() {
        segmentSeparator(
            segmentIndex = 0,
            labelPosition = LinearSegmentDirection.West,
        )
        StartSegment(
            modifier = Modifier
                .requiredSize(
                    streetOptions.streetWidth.let { DpSize(it, it * 1.25f) }
                ),
            game = game,
            boardOffset = boardOffset,
            segmentDirection = LinearSegmentDirection.North,
            streetOptions = streetOptions,
            useHighlight = useHighlight,
        )
        Box(
            modifier = Modifier
                .requiredWidth(streetOptions.streetWidth)
                .requiredHeight(32.dp)
        ) {
            Row(
                modifier = Modifier
                    .wrapContentSize(unbounded = true)
                    .align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                game[LineOrder.Forward].indices.forEach { playerIndex ->
                    Text(
                        modifier = Modifier
                            .wrapContentSize(),
                        text = playerIndex.inc().toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }

    @Composable
    fun winnerArea() = WinSegment(
        modifier = Modifier.requiredSize(streetOptions.streetWidth),
        game = game,
        boardOffset = boardOffset,
        streetOptions = streetOptions,
        useHighlight = useHighlight,
    )

    @Composable
    fun leftSide() = Row(
        modifier = Modifier
            .wrapContentSize(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize(),
            horizontalAlignment = AbsoluteAlignment.Left,
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            cornerSegment(
                segmentIndex = 4,
                arcFocus = ArcFocus.BottomRight,
            )
            (5..8).forEach { i ->
                segmentSeparator(
                    segmentIndex = i,
                    labelPosition = LinearSegmentDirection.East,
                )
                sideSegment(
                    segmentIndex = i,
                    segmentDirection = LinearSegmentDirection.South,
                )
            }
            segmentSeparator(
                segmentIndex = 9,
                labelPosition = LinearSegmentDirection.East,
            )
        }

        segmentSeparator(
            segmentIndex = 4,
            labelPosition = LinearSegmentDirection.South,
        )

        sideSegment(
            segmentIndex = 3,
            segmentDirection = LinearSegmentDirection.West
        )

        segmentSeparator(
            segmentIndex = 3,
            labelPosition = LinearSegmentDirection.South,
        )

        Column(
            modifier = Modifier
                .wrapContentSize(),
            horizontalAlignment = AbsoluteAlignment.Right,
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            cornerSegment(
                segmentIndex = 2,
                arcFocus = ArcFocus.BottomLeft,
            )
            segmentSeparator(
                segmentIndex = 2,
                labelPosition = LinearSegmentDirection.West,
            )
            sideSegment(
                segmentIndex = 1,
                segmentDirection = LinearSegmentDirection.North,
            )
            segmentSeparator(
                segmentIndex = 1,
                labelPosition = LinearSegmentDirection.West,
            )
            sideSegment(
                segmentIndex = 0,
                segmentDirection = LinearSegmentDirection.North,
            )
            startArea()
        }
    }

    @Composable
    fun bottom() = Row(
        modifier = Modifier
            .wrapContentSize(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        cornerSegment(
            segmentIndex = 9,
            arcFocus = ArcFocus.TopRight,
        )
        (10..13).forEach { i ->
            segmentSeparator(
                segmentIndex = i,
                labelPosition = LinearSegmentDirection.North
            )
            sideSegment(
                segmentIndex = i,
                segmentDirection = LinearSegmentDirection.East
            )
        }
        segmentSeparator(
            segmentIndex = 14,
            labelPosition = LinearSegmentDirection.North,
        )
        cornerSegment(
            segmentIndex = 14,
            arcFocus = ArcFocus.TopLeft,
        )
    }

    @Composable
    fun rightSide() = Row(
        modifier = Modifier
            .wrapContentSize(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize(),
            horizontalAlignment = AbsoluteAlignment.Left,
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            cornerSegment(
                segmentIndex = 21,
                arcFocus = ArcFocus.BottomRight,
            )
            segmentSeparator(
                segmentIndex = 22,
                labelPosition = LinearSegmentDirection.East,
            )
            sideSegment(
                segmentIndex = 22,
                segmentDirection = LinearSegmentDirection.South
            )
            segmentSeparator(
                segmentIndex = 23,
                labelPosition = LinearSegmentDirection.East,
            )
            sideSegment(
                segmentIndex = 23,
                segmentDirection = LinearSegmentDirection.South
            )
            segmentSeparator(
                segmentIndex = 24,
                labelPosition = LinearSegmentDirection.East,
            )
            winnerArea()
        }
        segmentSeparator(
            segmentIndex = 21,
            labelPosition = LinearSegmentDirection.South,
        )
        sideSegment(
            segmentIndex = 20,
            segmentDirection = LinearSegmentDirection.West,
        )
        segmentSeparator(
            segmentIndex = 20,
            labelPosition = LinearSegmentDirection.South,
        )
        Column(
            modifier = Modifier
                .wrapContentSize(),
            horizontalAlignment = AbsoluteAlignment.Right,
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            cornerSegment(
                segmentIndex = 19,
                arcFocus = ArcFocus.BottomLeft,
            )
            segmentSeparator(
                segmentIndex = 19,
                labelPosition = LinearSegmentDirection.West
            )
            (18 downTo 15).forEach { i ->
                sideSegment(
                    segmentIndex = i,
                    segmentDirection = LinearSegmentDirection.North,
                )
                segmentSeparator(
                    segmentIndex = i,
                    labelPosition = LinearSegmentDirection.West,
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .requiredWidth(IntrinsicSize.Min)
            .wrapContentHeight(),
        horizontalAlignment = AbsoluteAlignment.Left,
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            leftSide()
            rightSide()
        }
        bottom()
    }
}