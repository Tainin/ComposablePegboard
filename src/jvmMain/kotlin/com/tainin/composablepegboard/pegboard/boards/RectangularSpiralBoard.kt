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
import androidx.compose.ui.unit.sp
import com.tainin.composablepegboard.model.Game
import com.tainin.composablepegboard.pegboard.options.*
import com.tainin.composablepegboard.pegboard.segments.*

@Composable
fun RectangularSpiralBoard(
    boardOffset: Offset,
    streetOptions: StreetOptions,
    streetSpacing: Dp,
    segmentGap: Dp,
    segmentAspectRatio: Float,
    game: Game,
    useHighlight: Boolean,
) {
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
    fun arcSegment(
        modifier: Modifier,
        segmentIndex: Int,
        arcSegmentOptions: ArcSegmentOptions,
    ) = ArcSegment(
        modifier = modifier,
        segmentIndex = segmentIndex,
        game = game,
        boardOffset = boardOffset,
        arcSegmentOptions = arcSegmentOptions,
        streetOptions = streetOptions,
        useHighlight = useHighlight,
    )

    @Composable
    fun winnerArea() {
        WinSegment(
            modifier = Modifier.requiredSize(streetOptions.streetWidth),
            game = game,
            boardOffset = boardOffset,
            streetOptions = streetOptions,
            useHighlight = useHighlight,
        )
    }

    @Composable
    fun eastEndCurve(
        baseSegmentIndex: Int,
    ) = Row(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            listOf(7, 24, 9).forEach { i ->
                segmentSeparator(
                    segmentIndex = i,
                    labelPosition = LinearSegmentDirection.South,
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .align(AbsoluteAlignment.CenterLeft)
            ) {
                winnerArea()
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(),
                horizontalAlignment = AbsoluteAlignment.Right,
            ) {
                arcSegment(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    segmentIndex = baseSegmentIndex + 0,
                    arcSegmentOptions = ArcSegmentOptions(ArcFocus.BottomLeft, ArcDirection.Clockwise),
                )
                segmentSeparator(
                    segmentIndex = baseSegmentIndex + 1,
                    labelPosition = LinearSegmentDirection.West,
                )
                arcSegment(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    segmentIndex = baseSegmentIndex + 1,
                    arcSegmentOptions = ArcSegmentOptions(ArcFocus.TopLeft, ArcDirection.Clockwise),
                )
            }
        }
    }

    @Composable
    fun westEndCurve(
        baseSegmentIndex: Int,
    ) = Row(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(),
    ) {
        arcSegment(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(0.5f, true),
            segmentIndex = baseSegmentIndex,
            arcSegmentOptions = ArcSegmentOptions(ArcFocus.Right, ArcDirection.Clockwise),
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            segmentSeparator(
                segmentIndex = baseSegmentIndex + 1,
                labelPosition = LinearSegmentDirection.South,
            )
            segmentSeparator(
                segmentIndex = baseSegmentIndex + 0,
                labelPosition = LinearSegmentDirection.South,
            )
        }
    }

    @Composable
    fun longStreet(
        baseSegmentIndex: Int,
        segmentCount: Int,
        reverse: Boolean,
    ) = Row(
        modifier = Modifier
            .wrapContentSize(),
        horizontalArrangement = if (reverse) Arrangement.Absolute.Right else Arrangement.Absolute.Left,
    ) {
        val segmentDirection = if (reverse) LinearSegmentDirection.West else LinearSegmentDirection.East
        val width = streetOptions.streetWidth * segmentAspectRatio
        val range = (0 until segmentCount).let { if (reverse) it.reversed() else it }

        range.forEach { i ->
            val segmentIndex = baseSegmentIndex + i
            if (i != range.first)
                segmentSeparator(
                    segmentIndex = segmentIndex + if (reverse) 1 else 0,
                    labelPosition = LinearSegmentDirection.South,
                )
            LinearSegment(
                modifier = Modifier
                    .requiredSize(
                        width = width,
                        height = streetOptions.streetWidth,
                    ),
                segmentIndex = segmentIndex,
                game = game,
                boardOffset = boardOffset,
                segmentDirection = segmentDirection,
                streetOptions = streetOptions,
                useHighlight = useHighlight,
            )
        }
    }

    @Composable
    fun startArea() {
        StartSegment(
            modifier = Modifier
                .requiredSize(
                    streetOptions.streetWidth.let { DpSize(it * 1.25f, it) }
                ),
            game = game,
            boardOffset = boardOffset,
            segmentDirection = LinearSegmentDirection.East,
            streetOptions = streetOptions,
            useHighlight = useHighlight,
        )
        segmentSeparator(
            segmentIndex = 0,
            labelPosition = LinearSegmentDirection.South,
        )
    }

    @Composable
    fun boardHelper() = Row(
        modifier = Modifier
            .wrapContentWidth()
            .requiredHeight(IntrinsicSize.Min),
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(streetSpacing),
            horizontalAlignment = AbsoluteAlignment.Right,
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .requiredHeight(IntrinsicSize.Min),
                verticalAlignment = Alignment.Bottom
            ) {
                startArea()
                longStreet(0, 7, false)
            }
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .requiredHeight(IntrinsicSize.Min),
                verticalAlignment = Alignment.Bottom,
            ) {
                westEndCurve(16)
                Column(
                    modifier = Modifier
                        .wrapContentSize(),
                    verticalArrangement = Arrangement.spacedBy(streetSpacing),
                    horizontalAlignment = AbsoluteAlignment.Right
                ) {
                    longStreet(17, 7, false)
                    longStreet(9, 7, true)
                }
            }
        }
        eastEndCurve(7)
    }

    boardHelper()
}