package com.tainin.composablepegboard.utils

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.utf16CodePoint

fun KeyEvent.getDigitOrNull() = utf16CodePoint.toChar().digitToIntOrNull()