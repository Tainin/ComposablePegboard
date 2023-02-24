package com.tainin.composablepegboard.pegboard.boards

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.tainin.composablepegboard.model.Game
import com.tainin.composablepegboard.pegboard.options.*
import com.tainin.composablepegboard.pegboard.segments.ArcSegment
import com.tainin.composablepegboard.pegboard.segments.LinearSegment
import com.tainin.composablepegboard.pegboard.segments.StartSegment

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
    fun rightEndCurve(
        baseSegmentIndex: Int,
    ) = Row(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .requiredWidth(segmentGap)
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(0.5f, true),
            horizontalAlignment = AbsoluteAlignment.Right,
        ) {
            arcSegment(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                segmentIndex = baseSegmentIndex + 0,
                arcSegmentOptions = ArcSegmentOptions(ArcFocus.BottomLeft, ArcDirection.Clockwise),
            )
            Box(
                modifier = Modifier
                    .requiredSize(streetOptions.streetWidth, segmentGap)
            )
            arcSegment(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                segmentIndex = baseSegmentIndex + 1,
                arcSegmentOptions = ArcSegmentOptions(ArcFocus.TopLeft, ArcDirection.Clockwise),
            )
        }
    }

    @Composable
    fun leftEndCurve(
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
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .requiredWidth(segmentGap)
        )
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
            if (i != range.first)
                Box(
                    modifier = Modifier
                        .requiredSize(segmentGap, streetOptions.streetWidth)
                )
            LinearSegment(
                modifier = Modifier
                    .requiredSize(
                        width = width,
                        height = streetOptions.streetWidth,
                    ),
                segmentIndex = baseSegmentIndex + i,
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
                .requiredSize(streetOptions.streetWidth),
            game = game,
            boardOffset = boardOffset,
            segmentDirection = LinearSegmentDirection.East,
            streetOptions = streetOptions,
            useHighlight = useHighlight,
        )
        Box(
            modifier = Modifier
                .requiredSize(segmentGap, streetOptions.streetWidth)
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
                leftEndCurve(16)
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
        rightEndCurve(7)
    }

    boardHelper()
}