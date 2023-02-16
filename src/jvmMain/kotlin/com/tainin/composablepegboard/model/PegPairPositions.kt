package com.tainin.composablepegboard.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

class PegPairPositions {
    var a by mutableStateOf(Offset.Unspecified)
    var b by mutableStateOf(Offset.Unspecified)
}