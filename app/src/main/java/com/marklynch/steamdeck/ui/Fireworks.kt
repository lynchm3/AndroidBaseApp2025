package com.marklynch.steamdeck.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


@Composable
fun Fireworks(trigger: Boolean, onAnimationEnd: () -> Unit) {
    var particles by remember { mutableStateOf(emptyList<Particle>()) }

    // Trigger fireworks when "trigger" becomes true
    LaunchedEffect(trigger) {
        if (trigger) {
            particles = createFireworkParticles()

            val animationDuration = 2000
            val startTime = withFrameNanos { it }

            while (withFrameNanos { it } - startTime < animationDuration * 1_000_000) {
                particles = particles.map { particle ->
                    particle.copy(
                        x = particle.x + particle.velocityX,
                        y = particle.y + particle.velocityY,
                        alpha = particle.alpha - 0.02f,
                        scale = particle.scale * 0.98f
                    )
                }.filter { it.alpha > 0 }
                withFrameNanos { }
            }

            onAnimationEnd() // Reset the trigger state after animation
        }
    }

    // Draw particles as circles
    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            drawCircle(
                color = particle.color.copy(alpha = particle.alpha),
                radius = particle.scale * 5.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(particle.x, particle.y)
            )
        }
    }
}



// Data class for particle properties
data class Particle(
    var x: Float,
    var y: Float,
    val velocityX: Float,
    val velocityY: Float,
    val color: Color,
    val scale: Float,
    var alpha: Float
)

// Function to create particles with random properties
fun createFireworkParticles(): List<Particle> {
    val particles = mutableListOf<Particle>()
    val centerX = 400f
    val centerY = 800f
    val numParticles = 100
    for (i in 0 until numParticles) {
        val angle = Random.nextDouble(0.0, Math.PI * 2).toFloat()
        val speed = Random.nextFloat() * 8f + 2f
        particles.add(
            Particle(
                x = centerX,
                y = centerY,
                velocityX = (cos(angle) * speed),
                velocityY = (sin(angle) * speed),
                color = Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat()
                ),
                scale = Random.nextFloat() * 2f + 1f,
                alpha = 1f
            )
        )
    }
    return particles
}
