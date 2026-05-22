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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.graphics.StrokeCap
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

private val BgDark       = Color(0xFF070A10)
private val BgLight      = Color(0xFF0B1020)
private val CircuitColor = Color(0xFFE6822E)

// ─── Nós do circuito ─────────────────────────────────────────────────────

private data class CircuitNode(val x: Float, val y: Float, val connections: List<Int>)

private val circuitNodes = listOf(
    CircuitNode(0.10f, 0.12f, listOf(1, 3)),
    CircuitNode(0.32f, 0.07f, listOf(0, 2, 4)),
    CircuitNode(0.58f, 0.10f, listOf(1, 5)),
    CircuitNode(0.08f, 0.32f, listOf(0, 6)),
    CircuitNode(0.30f, 0.25f, listOf(1, 5, 7)),
    CircuitNode(0.68f, 0.20f, listOf(2, 4, 8)),
    CircuitNode(0.12f, 0.52f, listOf(3, 7, 9)),
    CircuitNode(0.42f, 0.42f, listOf(4, 6, 8, 10)),
    CircuitNode(0.72f, 0.36f, listOf(5, 7, 11)),
    CircuitNode(0.07f, 0.70f, listOf(6, 10)),
    CircuitNode(0.36f, 0.63f, listOf(7, 9, 11, 12)),
    CircuitNode(0.78f, 0.56f, listOf(8, 10, 13)),
    CircuitNode(0.52f, 0.80f, listOf(10, 13)),
    CircuitNode(0.85f, 0.74f, listOf(11, 12)),
    CircuitNode(0.18f, 0.87f, listOf(9, 12)),
    CircuitNode(0.90f, 0.18f, listOf(5, 8)),
    CircuitNode(0.93f, 0.50f, listOf(11, 15)),
    CircuitNode(0.88f, 0.88f, listOf(13, 16))
)

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
    val lineWidth    = remember { Animatable(0f) }
    val galleryAlpha = remember { Animatable(0f) }

    // Fotos do Strapi — apenas as do tipo "foto"
    val mediaItems by mediaViewModel.items
    val fotos = mediaItems.filter { it.tipo == "foto" && it.imagem?.url != null }

    // Pager do carrossel
    val pagerState = rememberPagerState { fotos.size.coerceAtLeast(1) }

    // Auto-scroll de 3 em 3 segundos
    LaunchedEffect(fotos.size) {
        while (true) {
            delay(3000)
            if (fotos.isNotEmpty()) {
                val next = (pagerState.currentPage + 1) % fotos.size
                pagerState.animateScrollToPage(next)
            }
        }
    }

    // Sequência de entrada
    val infiniteTransition = rememberInfiniteTransition(label = "circuit")
    val particleProgress by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Restart),
        label = "particle"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    LaunchedEffect(Unit) {
        launch { lineWidth.animateTo(1f, tween(1400, easing = FastOutSlowInEasing)) }
        delay(900)
        launch { logoAlpha.animateTo(1f, tween(800, easing = FastOutSlowInEasing)) }
        delay(600)
        launch { textAlpha.animateTo(1f, tween(500)) }
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

        // ── Circuito animado no fundo ────────────────────────────────
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircuit(circuitNodes, lineWidth.value, particleProgress, pulseAlpha)
        }

        // ── Conteúdo ─────────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {

            Spacer(Modifier.weight(1f))

            // Logo
            AsyncImage(
                model = R.drawable.icone_branco,
                contentDescription = "ePACT",
                modifier = Modifier
                    .size(88.dp)
                    .alpha(logoAlpha.value),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(20.dp))

            // Nome
            Text(
                text = "ePACT",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
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
                            listOf(Color.Transparent, CircuitColor, Color.Transparent)
                        )
                    )
            )

            Spacer(Modifier.height(10.dp))

            // Tagline
            Text(
                text = "UM NOVO ALENTEJO, TÉCNOLOGICO E INOVADOR",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = PactMuted,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(taglineAlpha.value)
            )
            Text(
                text = "PACT · ÉVORA",
                fontSize = 10.sp,
                color = CircuitColor.copy(alpha = 0.75f),
                letterSpacing = 2.sp,
                modifier = Modifier
                    .alpha(taglineAlpha.value)
                    .padding(top = 4.dp)
            )

            Spacer(Modifier.height(28.dp))

            // ── Carrossel de fotos do Strapi ─────────────────────────
            Box(
                modifier = Modifier
                    .alpha(galleryAlpha.value)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                if (fotos.isEmpty()) {
                    // Placeholder enquanto carrega
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(PactBlueSoft)
                    )
                } else {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val foto = fotos[page]
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        ) {
                            AsyncImage(
                                model = foto.imagem?.url,
                                contentDescription = foto.titulo,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            // Gradiente sobre a foto
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, BgDark.copy(0.7f))
                                        )
                                    )
                            )
                            // Título da foto
                            if (!foto.titulo.isNullOrBlank()) {
                                Text(
                                    text = foto.titulo,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White.copy(0.85f),
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(10.dp)
                                )
                            }
                        }
                    }

                    // Dots indicadores
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
                                            width = if (i == pagerState.currentPage) 16.dp else 5.dp,
                                            height = 5.dp
                                        )
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(
                                            if (i == pagerState.currentPage) CircuitColor
                                            else Color.White.copy(0.35f)
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
                        onClick = onCompaniesClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .padding(vertical = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Entrar no ePACT",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(Modifier.height(14.dp))

            // Link secundário
            Text(
                text = "Explorar Mapa →",
                fontSize = 13.sp,
                color = PactMuted,
                modifier = Modifier
                    .alpha(btnAlpha.value)
                    .clickable(
                        onClick = onMapClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )

            Spacer(Modifier.height(40.dp))
        }
    }
}

// ─── Canvas — circuito ───────────────────────────────────────────────────

private fun DrawScope.drawCircuit(
    nodes: List<CircuitNode>,
    lineProgress: Float,
    particleProgress: Float,
    pulseAlpha: Float
) {
    val w = size.width
    val h = size.height
    val drawnEdges = mutableSetOf<Pair<Int, Int>>()

    nodes.forEachIndexed { i, node ->
        node.connections.forEach { j ->
            val edge = if (i < j) Pair(i, j) else Pair(j, i)
            if (drawnEdges.add(edge)) {
                val start = Offset(nodes[i].x * w, nodes[i].y * h)
                val end   = Offset(nodes[j].x * w, nodes[j].y * h)
                drawLine(CircuitColor.copy(alpha = 0.10f), start, end, 1.2f, StrokeCap.Round)
                if (lineProgress > 0f) {
                    val animEnd = Offset(
                        start.x + (end.x - start.x) * lineProgress,
                        start.y + (end.y - start.y) * lineProgress
                    )
                    drawLine(CircuitColor.copy(alpha = 0.30f), start, animEnd, 1.2f, StrokeCap.Round)
                }
            }
        }
    }

    nodes.forEach { node ->
        val c = Offset(node.x * w, node.y * h)
        drawCircle(CircuitColor.copy(alpha = pulseAlpha * 0.10f * lineProgress), 12f, c)
        drawCircle(CircuitColor.copy(alpha = 0.28f * lineProgress), 4.5f, c)
        drawCircle(CircuitColor.copy(alpha = 0.75f * lineProgress), 2.2f, c)
    }

    if (lineProgress >= 0.9f) {
        val edgeList = drawnEdges.toList()
        if (edgeList.isNotEmpty()) {
            val total     = edgeList.size
            val edgeIndex = (particleProgress * total).toInt().coerceIn(0, total - 1)
            val frac      = (particleProgress * total) - edgeIndex
            val edge      = edgeList[edgeIndex]
            val start     = Offset(nodes[edge.first].x  * w, nodes[edge.first].y  * h)
            val end       = Offset(nodes[edge.second].x * w, nodes[edge.second].y * h)
            val pos       = Offset(start.x + (end.x - start.x) * frac, start.y + (end.y - start.y) * frac)
            drawCircle(CircuitColor.copy(alpha = 0.18f), 10f,  pos)
            drawCircle(CircuitColor.copy(alpha = 0.95f), 4f,   pos)
            drawCircle(Color.White.copy(alpha = 0.85f),  1.8f, pos)
        }
    }
}