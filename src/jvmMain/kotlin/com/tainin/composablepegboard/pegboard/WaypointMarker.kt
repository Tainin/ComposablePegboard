package com.tainin.composablepegboard.pegboard

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class MarkerOrientation {
    Horizontal,
    Vertical,
}

@Composable
fun WaypointMarker(
    score: Int,
    orientation: MarkerOrientation = MarkerOrientation.Vertical,
    useHighlight: Boolean,
) = Box(
    modifier = orientation.makeSizeModifier().let { mod ->
        if (useHighlight) mod.background(whiteHighlight) else mod
    }
) {
    makeLineModifiers(orientation).forEach { mod ->
        Box(modifier = mod)
    }
    Text(
        modifier = Modifier
            .requiredWidth(200.dp)
            .align(Alignment.Center)
            .rotateIfVertical(orientation),
        text = "$score",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

fun MarkerOrientation.makeSizeModifier(thickness: Dp = 15.dp) = when(this) {
    MarkerOrientation.Horizontal -> Modifier.fillMaxWidth().requiredHeight(thickness)
    MarkerOrientation.Vertical -> Modifier.fillMaxHeight().requiredWidth(thickness)
}

fun BoxScope.makeLineModifiers(orientation: MarkerOrientation, thickness: Dp = 3.dp, length: Dp = 25.dp) = when(orientation) {
    MarkerOrientation.Horizontal -> sequenceOf(Alignment.CenterStart, Alignment.CenterEnd).map { alignment ->
        Modifier.requiredSize(length, thickness).align(alignment)
    }
    MarkerOrientation.Vertical -> sequenceOf(Alignment.TopCenter, Alignment.BottomCenter).map { alignment ->
        Modifier.requiredSize(thickness, length).align(alignment)
    }
}.map { mod ->
    mod.background(Color.Black)
}

fun Modifier.rotateIfVertical(orientation: MarkerOrientation) = when(orientation) {
    MarkerOrientation.Horizontal -> this
    MarkerOrientation.Vertical -> rotate(-90f)
}

@Preview
@Composable
fun PreviewVerticalWaypointMarker() {
    Box(
        modifier = Modifier
            .requiredSize(150.dp, 75.dp)
            .background(Color.DarkGray)
    ) {
        WaypointMarker(
            score = 95,
            orientation = MarkerOrientation.Vertical,
            useHighlight = true
        )
    }
}

@Preview
@Composable
fun PreviewHorizontalWaypointMarker() {
    Box(
        modifier = Modifier
            .requiredSize(75.dp, 150.dp)
            .background(Color.DarkGray)
    ) {
        WaypointMarker(
            score = 95,
            orientation = MarkerOrientation.Horizontal,
            useHighlight = true
        )
    }
}