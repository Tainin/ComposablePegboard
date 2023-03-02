package com.tainin.composablepegboard.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class UserScoreInput(private val game: Game) {
    private var player by mutableStateOf<Player?>(null)
    private var scoreInput by mutableStateOf(0)

    val inputValue get() = scoreInput
    val selectedPlayer get() = player

    val hasSelectedPlayer get() = player?.let { true } ?: false

    fun applyInputAction(inputAction: InputAction) = when (inputAction) {
        is InputAction.Digit -> applyDigitAction(inputAction)
        InputAction.Cancel -> reset().let { true }
        InputAction.Confirm -> confirmScore()
        InputAction.Backspace -> player?.let { scoreInput /= 10; true } ?: false
        InputAction.Undo -> player?.run { score.pop(); true } ?: false
        else -> false
    }

    private fun applyDigitAction(digitAction: InputAction.Digit): Boolean {
        player?.let {
            scoreInput *= 10
            scoreInput += digitAction.digit
            return true
        }

        game[LineOrder.Forward].getOrNull(digitAction.digit.dec())?.let {
            player = it
            return true
        }

        return false
    }


    private fun confirmScore() = when {
        game.hasWinner -> false
        scoreInput <= 0 -> false
        else -> player?.run {
            score += scoreInput
            reset()
            true
        } ?: false
    }

    private fun reset() {
        player = null
        scoreInput = 0
    }
}

abstract class InputAction protected constructor() {
    companion object {
        val Unknown = object : InputAction() {}
        val Cancel = object : InputAction() {}
        val Confirm = object : InputAction() {}
        val Backspace = object : InputAction() {}
        val Undo = object : InputAction() {}
    }

    class Digit private constructor(val digit: Int) : InputAction() {
        companion object {
            private val digits = generateSequence(Digit(0)) { Digit(it.digit + 1) }.take(10).toList()
            fun fromInt(digit: Int) = digits.getOrElse(digit) { Unknown }
        }
    }
}