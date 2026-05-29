package com.example.epact.ui.screens.welcome

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.epact.R
import com.example.epact.ui.screens.media.MediaViewModel
import com.example.epact.ui.theme.PactAccent
import com.example.epact.ui.theme.PactBlueSoft
import com.example.epact.ui.theme.PactMuted
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sqrt
import kotlin.random.Random

private val BgDark      = Color(0xFF070A10)
private val BgLight     = Color(0xFF0B1020)
private val AccentColor = Color(0xFFE6822E)

// ─── Ponto da constelação ─────────────────────────────────────────────────

private data class StarPoint(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val radius: Float,
    val phase: Float
)

private fun generateStars(count: Int): List<StarPoint> =
    List(count) {
        StarPoint(
            x      = Random.nextFloat(),
            y      = Random.nextFloat(),
            vx     = (Random.nextFloat() - 0.5f) * 0.00018f,
            vy     = (Random.nextFloat() - 0.5f) * 0.00018f,
            radius = 1.5f + Random.nextFloat() * 2f,
            phase  = Random.nextFloat() * (Math.PI * 2).toFloat()
        )
    }

private val stars = generateStars(32)

// ─── WelcomeScreen ───────────────────────────────────────────────────────

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen(
    onCompaniesClick: () -> Unit,
    onMapClick: () -> Unit,
    mediaViewModel: MediaViewModel = viewModel()
) {
    val logoAlpha    = remember { Animatable(0f) }
    val textAlpha    = remember { Animatable(0f) }
    val taglineAlpha = remember { Animatable(0f) }
    val btnAlpha     = remember { Animatable(0f) }
    val starsAlpha   = remember { Animatable(0f) }
    val galleryAlpha = remember { Animatable(0f) }

    val mediaItems by mediaViewModel.items
    val fotos = mediaItems.filter { it.tipo == "foto" && it.imagem?.url != null }
    val pagerState = rememberPagerState { fotos.size.coerceAtLeast(1) }

    // Auto-scroll carrossel
    LaunchedEffect(fotos.size) {
        while (true) {
            delay(3000)
            if (fotos.size > 1) {
                pagerState.animateScrollToPage((pagerState.currentPage + 1) % fotos.size)
            }
        }
    }

    // Animação contínua da constelação
    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue  = 1f,
        animationSpec = infiniteRepeatable(
            animation   = tween(8000, easing = LinearEasing),
            repeatMode  = RepeatMode.Restart
        ),
        label = "time"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue  = 1f,
        animationSpec = infiniteRepeatable(
            animation   = tween(2500, easing = FastOutSlowInEasing),
            repeatMode  = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Sequência de entrada
    LaunchedEffect(Unit) {
        launch { starsAlpha.animateTo(1f, tween(1200)) }
        delay(600)
        launch { logoAlpha.animateTo(1f, tween(900, easing = FastOutSlowInEasing)) }
        delay(500)
        launch { textAlpha.animateTo(1f, tween(600)) }
        delay(350)
        launch { taglineAlpha.animateTo(1f, tween(500)) }
        delay(300)
        launch { galleryAlpha.animateTo(1f, tween(700)) }
        delay(200)
        launch { btnAlpha.animateTo(1f, tween(600)) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BgDark, BgLight))),
        contentAlignment = Alignment.Center
    ) {

        // ── Constelação animada ───────────────────────────────────────
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .alpha(starsAlpha.value)
        ) {
            drawConstellation(stars, time, pulse)
        }

        // ── Conteúdo ─────────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.weight(1f))

            // Logo
            AsyncImage(
                model             = R.drawable.icone_branco,
                contentDescription = "ePACT",
                modifier          = Modifier
                    .size(90.dp)
                    .alpha(logoAlpha.value),
                contentScale      = ContentScale.Fit
            )

            Spacer(Modifier.height(22.dp))

            // Nome
            Text(
                text     = "ePACT",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color    = Color.White,
                letterSpacing = 8.sp,
                modifier = Modifier.alpha(textAlpha.value)
            )

            Spacer(Modifier.height(10.dp))

            // Linha laranja
            Box(
                modifier = Modifier
                    .alpha(taglineAlpha.value)
                    .width(48.dp)
                    .height(2.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, AccentColor, Color.Transparent)
                        )
                    )
            )

            Spacer(Modifier.height(10.dp))

            // Tagline
            Text(
                text          = "UM NOVO ALENTEJO, TECNOLÓGICO E INOVADOR",
                fontSize      = 13.sp,
                fontWeight    = FontWeight.Medium,
                color         = PactMuted,
                letterSpacing = 2.sp,
                textAlign     = TextAlign.Center,
                modifier      = Modifier.alpha(taglineAlpha.value)
            )
            Text(
                text          = "PACT · ÉVORA",
                fontSize      = 10.sp,
                color         = AccentColor.copy(alpha = 0.75f),
                letterSpacing = 2.sp,
                modifier      = Modifier
                    .alpha(taglineAlpha.value)
                    .padding(top = 4.dp)
            )

            Spacer(Modifier.height(28.dp))

            // ── Carrossel de fotos ────────────────────────────────────
            Box(
                modifier = Modifier
                    .alpha(galleryAlpha.value)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                if (fotos.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(PactBlueSoft)
                    )
                } else {
                    HorizontalPager(
                        state    = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val foto = fotos[page]
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        ) {
                            AsyncImage(
                                model              = foto.imagem?.url,
                                contentDescription = foto.titulo,
                                modifier           = Modifier.fillMaxSize(),
                                contentScale       = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, BgDark.copy(alpha = 0.75f))
                                        )
                                    )
                            )
                            if (!foto.titulo.isNullOrBlank()) {
                                Text(
                                    text     = foto.titulo,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color    = Color.White.copy(alpha = 0.85f),
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(10.dp)
                                )
                            }
                        }
                    }

                    // Dots
                    if (fotos.size > 1) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            fotos.indices.forEach { i ->
                                Box(
                                    modifier = Modifier
                                        .size(
                                            width  = if (i == pagerState.currentPage) 16.dp else 5.dp,
                                            height = 5.dp
                                        )
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(
                                            if (i == pagerState.currentPage) AccentColor
                                            else Color.White.copy(alpha = 0.35f)
                                        )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // Botão principal
            Box(
                modifier = Modifier
                    .alpha(btnAlpha.value)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
                    .background(PactAccent)
                    .clickable(
                        onClick            = onCompaniesClick,
                        indication         = null,
                        interactionSource  = remember { MutableInteractionSource() }
                    )
                    .padding(vertical = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text          = "Explorar o ecossistema",
                    fontSize      = 15.sp,
                    fontWeight    = FontWeight.Bold,
                    color         = Color.White,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(Modifier.height(14.dp))

            // Link secundário


            Spacer(Modifier.height(44.dp))
        }
    }
}

// ─── Canvas — constelação ─────────────────────────────────────────────────

private fun DrawScope.drawConstellation(
    points: List<StarPoint>,
    time: Float,
    pulse: Float
) {
    val w = size.width
    val h = size.height
    val maxDist = w * 0.22f

    // Atualiza posições
    points.forEach { p ->
        p.x = (p.x + p.vx).let { if (it < 0f) 1f else if (it > 1f) 0f else it }
        p.y = (p.y + p.vy).let { if (it < 0f) 1f else if (it > 1f) 0f else it }
    }

    // Linhas entre pontos próximos
    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            val ax = points[i].x * w
            val ay = points[i].y * h
            val bx = points[j].x * w
            val by = points[j].y * h
            val dx = ax - bx
            val dy = ay - by
            val dist = sqrt(dx * dx + dy * dy)
            if (dist < maxDist) {
                val alpha = (1f - dist / maxDist) * 0.28f
                drawLine(
                    color       = AccentColor.copy(alpha = alpha),
                    start       = Offset(ax, ay),
                    end         = Offset(bx, by),
                    strokeWidth = 1f
                )
            }
        }
    }

    // Pontos
    points.forEach { p ->
        val cx = p.x * w
        val cy = p.y * h
        val pulseFactor = 0.6f + 0.4f * ((pulse + p.phase / (Math.PI * 2).toFloat()) % 1f)

        // Halo suave
        drawCircle(
            color  = AccentColor.copy(alpha = 0.08f * pulseFactor),
            radius = p.radius * 4f,
            center = Offset(cx, cy)
        )
        // Ponto principal
        drawCircle(
            color  = AccentColor.copy(alpha = 0.55f + 0.35f * pulseFactor),
            radius = p.radius,
            center = Offset(cx, cy)
        )
    }
}