package com.tainin.composablepegboard.pegboard.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import com.tainin.composablepegboard.model.Game

@Composable
fun WinPositionUpdater(
    vararg keys: Any?,
    game: Game,
    pegPosition: Offset
) = LaunchedEffect(keys) {
    snapshotFlow { game.hasWinner }.collect { _ ->
        game.withWinner { player ->
            when (player.score.pair.lead) {
                player.score.pair.a -> player.pegPositions.a = pegPosition
                player.score.pair.b -> player.pegPositions.b = pegPosition
            }
        }
    }
}