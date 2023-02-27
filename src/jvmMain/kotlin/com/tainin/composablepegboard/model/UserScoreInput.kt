package com.tainin.composablepegboard.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.*
import com.tainin.composablepegboard.utils.getDigitOrNull

private val digitRange = 0..9

class UserScoreInput(private val game: Game) {
    private var player by mutableStateOf<Player?>(null)
    private var scoreInput by mutableStateOf(0)

    val inputValue get() = scoreInput
    val selectedPlayer get() = player

    val hasSelectedPlayer get() = player?.let { true } ?: false

    @OptIn(ExperimentalComposeUiApi::class)
    fun applyKeyEvent(keyEvent: KeyEvent): Boolean {
        if (keyEvent.type != KeyEventType.KeyDown) return false
        when (keyEvent.key) {
            Key.Escape -> reset()
            Key.Backspace -> popDigit()
            Key.Delete -> popScore()
            Key.Enter -> confirmScore()
            else -> null
        }?.let { return true }

        keyEvent.getDigitOrNull()?.let { digit ->
            player?.let { addDigit(digit); return true }
            player = game[LineOrder.Forward].getOrNull(digit - 1) ?: return false
            return true
        }

        return false
    }

    private fun confirmScore() {
        if (game.hasWinner) return
        if (scoreInput <= 0) return
        player?.score?.plusAssign(scoreInput)
        reset()
    }

    private fun popScore() {
        player?.score?.pop()
    }

    private fun popDigit() {
        scoreInput /= 10
    }

    private fun reset() {
        player = null; scoreInput = 0
    }

    private fun addDigit(digit: Int) {
        require(digit in digitRange) { "digit must be within $digitRange" }
        scoreInput *= 10
        scoreInput += digit
    }
}