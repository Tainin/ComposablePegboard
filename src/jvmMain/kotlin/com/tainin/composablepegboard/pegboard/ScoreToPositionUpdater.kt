package com.tainin.composablepegboard.pegboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import com.tainin.composablepegboard.model.Player
import com.tainin.composablepegboard.utils.toSegmentOffset

@Composable
fun ScoreToPositionUpdater(
    vararg keys: Any?,
    player: Player,
    segmentIndex: Int,
    converter: (Int) -> Offset
) = LaunchedEffect(keys) {
    snapshotFlow { player.score.pair }.collect { pair ->
        pair.a.toSegmentOffset(segmentIndex)?.let { i -> player.pegPositions.a = converter(i) }
        pair.b.toSegmentOffset(segmentIndex)?.let { i -> player.pegPositions.b = converter(i) }
    }
}