package com.tainin.composablepegboard.geometry

import androidx.compose.ui.unit.DpRect

abstract class BoardSegment {
    abstract val anchor: Anchor
    abstract val nextAnchor: Anchor
    abstract val boundingRect: DpRect
}