package com.tainin.composablepegboard.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class ScorePair(val a: Int, val b: Int) {
    val lead get() = if (a > b) a else b
    operator fun plus(count: Int) = if (a > b) ScorePair(a, a + count) else ScorePair(b + count, b)
}

private class ScoreSnapshot(val pair: ScorePair) {
    var previous: ScoreSnapshot? = null
}

class Score {
    private var snapshot by mutableStateOf(
        ScoreSnapshot(
            ScorePair(0, -1)
        )
    )

    val pair: ScorePair get() = snapshot.pair
    val history: Sequence<Int>
        get() = sequence {
            var snap: ScoreSnapshot? = snapshot
            while (snap != null) {
                yield(snap.pair.lead)
                snap = snap.previous
            }
        }

    operator fun plusAssign(count: Int) {
        ScoreSnapshot((snapshot.pair + count))
            .also { it.previous = snapshot }
            .let { snapshot = it }
    }

    fun pop() = snapshot.previous?.let { snapshot = it; true } ?: false
}