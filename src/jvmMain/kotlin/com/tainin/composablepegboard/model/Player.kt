package com.tainin.composablepegboard.model

import androidx.compose.ui.graphics.Color

data class PlayerColor(val r: Int, val g: Int, val b: Int) {
    companion object {
        private const val MASK = 0xFF
        private const val HIGHLIGHT_ALPHA = 0x48

        val Red = PlayerColor(255, 100, 100)
        val Green = PlayerColor(0, 255, 180)
        val Blue = PlayerColor(0, 180, 255)
        val Purple = PlayerColor(180, 100, 255)
        val Yellow = PlayerColor(255, 180, 100)
    }

    val highlightColor = Color(r and MASK, g and MASK, b and MASK, HIGHLIGHT_ALPHA)
    val pegColor = Color(r and MASK, g and MASK, b and MASK)

}

class Player private constructor(val color: PlayerColor, val score: Score, val pegPositions: PegPairPositions) {
    companion object {
        fun makeForColor(color: PlayerColor) = Player(color, Score(), PegPairPositions())
    }
}