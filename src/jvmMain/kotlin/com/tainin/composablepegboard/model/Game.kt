package com.tainin.composablepegboard.model

enum class LineOrder {
    Forward,
    Reverse,
}

class Game(vararg colors: PlayerColor) {
    private val players = sequence {
        colors.forEach { yield(Player.makeForColor(it)) }
    }.toList()

    private val reversed = players.asReversed()
    val playerCount = players.size
    fun withWinner(action: (Player) -> Unit) =
        players.find { it.score.pair.lead > 120 }?.let { action(it); true } ?: false

    fun hasWinner() = withWinner { /* no-op */ }
    fun ongoing() = !hasWinner()

    companion object {
        fun makeTwoPlayerGame() = Game(PlayerColor.Red, PlayerColor.Blue)
        fun makeThreePlayerGame() = Game(PlayerColor.Red, PlayerColor.Green, PlayerColor.Blue)
    }

    operator fun get(order: LineOrder) = when (order) {
        LineOrder.Forward -> players
        LineOrder.Reverse -> reversed
    }
}