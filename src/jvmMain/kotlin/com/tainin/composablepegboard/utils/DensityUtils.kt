package com.tainin.composablepegboard.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset

fun DpOffset.toOffset(density: Density) = with(density) { Offset(x.toPx(), y.toPx()) }