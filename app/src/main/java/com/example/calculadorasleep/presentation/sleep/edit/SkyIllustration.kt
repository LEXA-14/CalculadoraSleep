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

/**
 * Ilustración de fondo tipo "sky" para el header: sol con rayos cuando el modo
 * es despertar, luna creciente con estrellas cuando el modo es dormir.
 * Dibujada 100% con Canvas — sin emojis ni imágenes externas.
 */
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
    val center = Offset(size.width * 0.78f, size.height * 0.35f)
    val radius = size.height * 0.22f
    val rayCount = 8
    val rayGap = 6.dp.toPx()
    val rayLength = radius * 0.6f

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
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
    }

    drawCircle(color = SunCore, radius = radius, center = center)
}

private fun DrawScope.drawMoon() {
    val center = Offset(size.width * 0.78f, size.height * 0.32f)
    val radius = size.height * 0.22f

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
        Offset(size.width * 0.15f, size.height * 0.25f) to 2f,
        Offset(size.width * 0.30f, size.height * 0.55f) to 1.5f,
        Offset(size.width * 0.45f, size.height * 0.20f) to 2.5f,
        Offset(size.width * 0.10f, size.height * 0.65f) to 1.5f,
        Offset(size.width * 0.55f, size.height * 0.45f) to 2f
    )
    stars.forEach { (offset, radiusDp) ->
        drawCircle(color = StarColor, radius = radiusDp.dp.toPx(), center = offset)
    }
}

private fun DrawScope.drawClouds(color: Color) {
    fun cloud(cx: Float, cy: Float, scale: Float) {
        drawCircle(color = color, radius = 14.dp.toPx() * scale, center = Offset(cx, cy))
        drawCircle(
            color = color,
            radius = 18.dp.toPx() * scale,
            center = Offset(cx + 16.dp.toPx() * scale, cy + 4.dp.toPx() * scale)
        )
        drawCircle(
            color = color,
            radius = 12.dp.toPx() * scale,
            center = Offset(cx + 32.dp.toPx() * scale, cy)
        )
    }
    cloud(size.width * 0.10f, size.height * 0.75f, 1f)
    cloud(size.width * 0.50f, size.height * 0.85f, 0.8f)
}