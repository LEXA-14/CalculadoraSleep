package com.example.calculadorasleep.presentation.sleep.edit

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.example.calculadorasleep.domain.sleep.UseCases.SleepCalculationMode
import com.example.calculadorasleep.ui.theme.CloudColor
import com.example.calculadorasleep.ui.theme.MoonBody
import com.example.calculadorasleep.ui.theme.MoonShadow
import com.example.calculadorasleep.ui.theme.StarColor
import com.example.calculadorasleep.ui.theme.SunCore
import com.example.calculadorasleep.ui.theme.SunRays
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SkyIllustration(
    mode: SleepCalculationMode,
    modifier: Modifier = Modifier
) {
    Crossfade(
        targetState = mode,
        animationSpec = tween(400),
        modifier = modifier,
        label = "sky_illustration"
    ) { currentMode ->
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (currentMode == SleepCalculationMode.WAKE_UP_AT) {
                drawClouds(CloudColor.copy(alpha = 0.55f))
                drawSun()
            } else {
                drawClouds(MoonShadow.copy(alpha = 0.30f))
                drawStars()
                drawMoon()
            }
        }
    }
}

private fun DrawScope.drawSun() {
    val center = Offset(size.width * 0.5f, size.height * 0.5f)
    val radius = size.minDimension * 0.35f
    val rayCount = 8
    val rayGap = (size.minDimension * 0.05f).coerceAtLeast(2f)
    val rayLength = radius * 0.5f
    repeat(rayCount) { i ->
        val angle = (2 * Math.PI / rayCount) * i
        val start = Offset(
            x = center.x + cos(angle).toFloat() * (radius + rayGap),
            y = center.y + sin(angle).toFloat() * (radius + rayGap)
        )
        val end = Offset(
            x = center.x + cos(angle).toFloat() * (radius + rayGap + rayLength),
            y = center.y + sin(angle).toFloat() * (radius + rayGap + rayLength)
        )
        drawLine(
            color = SunRays.copy(alpha = 0.7f),
            start = start,
            end = end,
            strokeWidth = (size.minDimension * 0.04f).coerceAtLeast(1f),
            cap = StrokeCap.Round
        )
    }
    drawCircle(color = SunCore, radius = radius, center = center)
}

private fun DrawScope.drawMoon() {
    val center = Offset(size.width * 0.5f, size.height * 0.5f)
    val radius = size.minDimension * 0.35f
    val fullCircle = Path().apply {
        addOval(Rect(center = center, radius = radius))
    }
    val cutoutCircle = Path().apply {
        addOval(
            Rect(
                center = Offset(center.x + radius * 0.55f, center.y - radius * 0.35f),
                radius = radius * 0.85f
            )
        )
    }
    val crescent = Path().apply {
        op(fullCircle, cutoutCircle, PathOperation.Difference)
    }
    drawPath(path = crescent, color = MoonBody)
}

private fun DrawScope.drawStars() {
    val stars = listOf(
        Offset(size.width * 0.2f, size.height * 0.2f) to 1.5f,
        Offset(size.width * 0.8f, size.height * 0.25f) to 1.2f,
        Offset(size.width * 0.3f, size.height * 0.7f) to 1.0f,
        Offset(size.width * 0.7f, size.height * 0.8f) to 1.3f,
        Offset(size.width * 0.1f, size.height * 0.5f) to 0.8f
    )
    stars.forEach { (offset, radiusDp) ->
        drawCircle(color = StarColor, radius = (size.minDimension * 0.02f * radiusDp).coerceAtLeast(1f), center = offset)
    }
}

private fun DrawScope.drawClouds(color: Color) {
    fun cloud(cx: Float, cy: Float, scale: Float) {
        val baseRadius = size.minDimension * 0.12f * scale
        drawCircle(color = color, radius = baseRadius, center = Offset(cx, cy))
        drawCircle(
            color = color,
            radius = baseRadius * 1.3f,
            center = Offset(cx + baseRadius * 1.1f, cy + baseRadius * 0.3f)
        )
        drawCircle(
            color = color,
            radius = baseRadius * 0.9f,
            center = Offset(cx + baseRadius * 2.2f, cy)
        )
    }
    cloud(size.width * 0.15f, size.height * 0.7f, 1f)
    cloud(size.width * 0.55f, size.height * 0.8f, 0.7f)
}