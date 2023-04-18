package com.tainin.composablepegboard.geometry.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.tainin.composablepegboard.model.Player

abstract class SegmentDrawable {
    abstract fun DrawScope.draw()
}

abstract class HighlightedSegmentDrawable(
    private val usePlayerHighlights: Boolean,
) : SegmentDrawable() {
    protected abstract fun DrawScope.drawHighlight()
    protected abstract fun DrawScope.drawOverHighlight()

    final override fun DrawScope.draw() {
        if (usePlayerHighlights)
            drawHighlight()
        drawOverHighlight()
    }
}

abstract class PeggingLineDrawable(
    val player: Player,
    val lineThickness: Float,
    private val holeCount: Int,
    usePlayerHighlight: Boolean,
) : HighlightedSegmentDrawable(usePlayerHighlight) {

    protected abstract fun getHoleOffset(fraction: Float): Offset
    fun getHoleOffset(index: Int) = getHoleOffset((0.5f + index) / holeCount)
    final override fun DrawScope.drawOverHighlight() =
        (0 until holeCount).forEach { i ->
            val center = getHoleOffset(i)
            drawCircle(
                color = Color.Black,
                radius = lineThickness / 2f,
                center = center,
                style = Fill,
            )
        }
}

class MultiDrawable<Drawable : SegmentDrawable>(
    private val drawables: Sequence<Drawable>,
) : SegmentDrawable() {
    override fun DrawScope.draw() =
        drawables.forEach { drawable ->
            with(drawable) { draw() }
        }

    fun getOrNull(index: Int) = drawables.drop(index).firstOrNull()
}