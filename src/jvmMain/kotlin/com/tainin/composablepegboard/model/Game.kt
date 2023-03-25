package com.tainin.composablepegboard.model

enum class LineOrder {
    Forward,
    Reverse,
}

class Game(private val winningScore: Int, vararg colors: PlayerColor) {
    private val players = sequence {
        colors.forEach { yield(Player.makeForColor(it)) }
    }.toList()

    private val reversed = players.asReversed()
    val playerCount = players.size

    val playerSequence = players.asSequence() //TODO Temporary compatibility fix until I get a chance to refactor

    private val winner get() = players.find { it.score.pair.lead >= winningScore }
    val hasWinner get() = winner?.let { true } ?: false
    val ongoing get() = winner?.let { false } ?: true
    fun withWinner(action: (Player) -> Unit) = winner?.let { action(it); true } ?: false

    companion object {
        fun makeTwoPlayerGame(winningScore: Int) = Game(winningScore, PlayerColor.Red, PlayerColor.Blue)
        fun makeThreePlayerGame(winningScore: Int) =
            Game(winningScore, PlayerColor.Red, PlayerColor.Green, PlayerColor.Blue)
    }

    operator fun get(order: LineOrder) = when (order) {
        LineOrder.Forward -> players
        LineOrder.Reverse -> reversed
    }
}