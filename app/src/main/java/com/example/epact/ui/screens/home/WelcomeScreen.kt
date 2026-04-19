package com.example.epact.ui.screens.welcome

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.epact.data.AppData
import com.example.epact.model.Metric
import com.example.epact.ui.components.HighlightCard
import com.example.epact.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    onCompaniesClick: () -> Unit,
    onMapClick: () -> Unit
) {
    // Animação de entrada suave
    val heroAlpha   = remember { Animatable(0f) }
    val contentAlpha = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch { heroAlpha.animateTo(1f, tween(700)) }
        delay(300)
        scope.launch { contentAlpha.animateTo(1f, tween(600)) }
    }

    val siteBg = Color(0xFF070A10)
    val siteBg1 = Color(0xFF0B1020)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(siteBg, siteBg1))
            ),
        contentPadding = PaddingValues(bottom = 48.dp)
    ) {

        // ── HERO ──────────────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .alpha(heroAlpha.value)
            ) {

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(20.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PactAccent)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "PACT · Évora",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.08.sp
                    )
                }


                // Texto principal hero
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 22.dp, vertical = 24.dp)
                ) {
                    Text(
                        text = "Um novo Alentejo",
                        color = PactAccent,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.06.sp
                    )
                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "Tecnológico\ne Inovador",
                        color = PactText,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 42.sp
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "O ecossistema de inovação e tecnologia\nno coração do Alentejo.",
                        color = PactText.copy(alpha = 0.75f),
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )

                    Spacer(Modifier.height(16.dp))

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.White.copy(alpha = 0.08f))
                    )
                }
            }
        }


        // ── MÉTRICAS ──────────────────────────────────────────────────────
        item {
            Column(
                modifier = Modifier
                    .alpha(contentAlpha.value)
                    .padding(top = 28.dp, start = 20.dp, end = 20.dp)
            ) {
                SectionLabel("INDICADORES")
                Spacer(Modifier.height(8.dp))

                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.08f))
                )
                Spacer(Modifier.height(14.dp))
            }
        }

        item {
            LazyRow(
                modifier = Modifier.alpha(contentAlpha.value),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(AppData.metrics) { metric ->
                    WelcomeMetricCard(metric)
                }
            }
        }

        // ── DIVISOR ───────────────────────────────────────────────────────
        item {
            Spacer(Modifier.height(28.dp))
            Box(Modifier.fillMaxWidth().height(0.5.dp).background(PactBorder))
        }

        // ── SOBRE O ECOSSISTEMA ───────────────────────────────────────────
        item {
            Column(
                modifier = Modifier
                    .alpha(contentAlpha.value)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                SectionLabel("SOBRE O ePACT")
                Spacer(Modifier.height(14.dp))
                Text(
                    text = "Uma ligação entre o PACT, entidades da região e o tecido empresarial e institucional — focada em inovação, conhecimento e valorização económica e social do Alentejo.",
                    color = PactText.copy(alpha = 0.85f),
                    fontSize = 15.sp,
                    lineHeight = 24.sp
                )
            }
        }

        // ── SOLUÇÕES ─────────────────────────────────────────────────────
        item {
            Column(
                modifier = Modifier
                    .alpha(contentAlpha.value)
                    .padding(horizontal = 20.dp)
            ) {
                SectionLabel("SOLUÇÕES")
                Spacer(Modifier.height(14.dp))
                SolutionRow(
                    title = "Premium",
                    description = "Acesso a vantagens do PACT, serviços comuns e utilização de espaços.",
                    accent = PactAccent
                )
                Spacer(Modifier.height(10.dp))
                SolutionRow(
                    title = "InPACT",
                    description = "Incubação física com espaço dedicado e acesso a infraestruturas.",
                    accent = PactGreen
                )
                Spacer(Modifier.height(10.dp))
                SolutionRow(
                    title = "OnPACT",
                    description = "Incubadora digital para presença flexível e acompanhamento remoto.",
                    accent = Color(0xFF3D8B7A)
                )
                Spacer(Modifier.height(10.dp))
                SolutionRow(
                    title = "Cowork",
                    description = "Zona de trabalho pronta para equipas, nómadas digitais e colaboração.",
                    accent = Color(0xFF8A6FD8)
                )
            }
        }

        // ── QUEM PODE FAZER PARTE ────────────────────────────────────────
        item {
            Spacer(Modifier.height(28.dp))
            Box(Modifier.fillMaxWidth().height(0.5.dp).background(PactBorder))
            Column(
                modifier = Modifier
                    .alpha(contentAlpha.value)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                SectionLabel("QUEM PODE FAZER PARTE")
                Spacer(Modifier.height(14.dp))
                listOf(
                    "Startups regionais",
                    "Empresas inovadoras",
                    "Empresas de tecnologia",
                    "Empreendedores"
                ).forEach { label ->
                    MemberRow(label)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        // ── CTAs ─────────────────────────────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .alpha(contentAlpha.value)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Botão primário — Empresas
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(PactAccent)
                        .clickable { onCompaniesClick() }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ver Empresas",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
                // Botão secundário — Mapa
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, PactBorder, RoundedCornerShape(14.dp))
                        .background(PactCard)
                        .clickable { onMapClick() }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Explorar Mapa",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PactText
                    )
                }
            }
        }
    }
}

// ─── Metric card compacto ─────────────────────────────────────────────────

@Composable
private fun WelcomeMetricCard(metric: Metric) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(PactCard)
            .border(0.5.dp, PactBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PactGreenSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = metricIcon(metric.icon),
                    contentDescription = metric.title,
                    tint = PactAccent,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = metric.value,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = PactText
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = metric.title,
                color = PactMuted,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }
    }
}

private fun metricIcon(icon: String): ImageVector = when (icon) {
    "business"  -> Icons.Default.Business
    "groups"    -> Icons.Default.Groups
    "apartment" -> Icons.Default.Apartment
    else        -> Icons.AutoMirrored.Filled.TrendingUp
}

// ─── Solução row ──────────────────────────────────────────────────────────

@Composable
private fun SolutionRow(title: String, description: String, accent: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(PactCard)
            .border(0.5.dp, PactBorder, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(accent)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, color = accent, fontSize = 14.sp)
            Spacer(Modifier.height(2.dp))
            Text(description, color = PactMuted, fontSize = 12.sp, lineHeight = 17.sp)
        }
    }
}

// ─── Member row ───────────────────────────────────────────────────────────

@Composable
private fun MemberRow(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(PactCard)
            .border(0.5.dp, PactBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(PactAccent)
        )
        Text(label, color = PactText, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}

// ─── Section label ────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = PactMuted,
        letterSpacing = 0.08.sp
    )
}