package com.tainin.composablepegboard.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.*
import com.tainin.composablepegboard.model.InputAction

@OptIn(ExperimentalComposeUiApi::class)
fun KeyEvent.toInputAction(): InputAction {
    if (type != KeyEventType.KeyDown) return InputAction.Unknown
    when (key) {
        Key.Escape -> InputAction.Cancel
        Key.Backspace -> InputAction.Backspace
        Key.Delete -> InputAction.Undo
        Key.Enter -> InputAction.Confirm
        else -> null
    }?.let { return it }

    return InputAction.Digit.fromInt(utf16CodePoint.toChar().digitToIntOrNull() ?: -1)
}