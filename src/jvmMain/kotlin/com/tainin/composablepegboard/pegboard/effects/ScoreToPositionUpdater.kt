package com.tainin.composablepegboard.pegboard.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import com.tainin.composablepegboard.pegboard.options.SegmentLineOptions
import com.tainin.composablepegboard.utils.toSegmentOffset

@Composable
fun ScoreToPositionUpdater(
    vararg keys: Any?,
    lineOptions: SegmentLineOptions,
    converter: (lineIndex: Int, segmentOffset: Int) -> Offset
) = LaunchedEffect(keys) {
    snapshotFlow { lineOptions.player.score.pair }.collect { pair ->
        pair.a.toSegmentOffset(lineOptions.segmentIndex)?.let { segOffset ->
            lineOptions.player.pegPositions.a = converter(lineOptions.lineIndex, segOffset)
        }
        pair.b.toSegmentOffset(lineOptions.segmentIndex)?.let { segOffset ->
            lineOptions.player.pegPositions.b = converter(lineOptions.lineIndex, segOffset)
        }
    }
}