package com.example.epact.ui.screens.companies

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.epact.model.EmpresaData
import com.example.epact.model.StrapiMediaData
import com.example.epact.ui.theme.PactAccent
import com.example.epact.ui.theme.PactBlack
import com.example.epact.ui.theme.PactBorder
import com.example.epact.ui.theme.PactBlueSoft
import com.example.epact.ui.theme.PactCard
import com.example.epact.ui.theme.PactMuted
import com.example.epact.ui.theme.PactOrange
import com.example.epact.ui.theme.PactSurfaceAlt
import com.example.epact.ui.theme.PactText
import kotlinx.coroutines.launch
import androidx.compose.foundation.ExperimentalFoundationApi

@Composable
fun EdificioDetailScreen(
    codigoEdificio: String,
    companyViewModel: CompanyViewModel = viewModel(),
    edificioViewModel: EdificioViewModel = viewModel(),
    onCompanyClick: (Int) -> Unit
) {
    val companies   by companyViewModel.companies
    val isLoading   by companyViewModel.isLoading
    val edificios   by edificioViewModel.edificios

    // Encontra o edifício pelo código
    val edificio = edificios.firstOrNull {
        it.codigo?.equals(codigoEdificio, ignoreCase = true) == true
    }

    // Filtra empresas deste edifício
    // O campo edificio na EmpresaData deve ter o código — ajusta o nome se necessário
    val empresasEdificio = companies.filter { empresa ->
        empresa.edificio?.equals(codigoEdificio, ignoreCase = true) == true
    }

    val fotos = edificio?.fotos ?: emptyList()
    val nomeEdificio = edificio?.nome ?: "Edifício $codigoEdificio"
    val descricao = edificio?.descricao

    var lightboxIndex by remember { mutableStateOf<Int?>(null) }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PactAccent)
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(PactBlack)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 48.dp)
        ) {

            // ── 1. HERO com fotos do Strapi ──────────────────────────────
            item {
                HeroCarrossel(
                    fotos = fotos,
                    nomeEdificio = nomeEdificio,
                    descricao = descricao,
                    onFotoClick = { index -> lightboxIndex = index }
                )
            }

            // ── 2. Contador de empresas ──────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "EMPRESAS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PactMuted,
                        letterSpacing = 0.08.sp
                    )
                    Text(
                        "${empresasEdificio.size} empresa${if (empresasEdificio.size != 1) "s" else ""}",
                        fontSize = 12.sp,
                        color = PactMuted
                    )
                }
                Box(Modifier.fillMaxWidth().height(0.5.dp).background(PactBorder))
            }

            // ── 3. Lista de empresas ─────────────────────────────────────
            if (empresasEdificio.isEmpty()) {
                item {
                    Box(
                        Modifier.fillMaxWidth().padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Empresas a ser adicionadas brevemente",
                            fontSize = 13.sp,
                            color = PactMuted
                        )
                    }
                }
            } else {
                items(empresasEdificio) { empresa ->
                    EmpresaRow(
                        empresa = empresa,
                        onClick = { onCompanyClick(empresa.id) }
                    )
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(0.5.dp)
                            .background(PactBorder)
                    )
                }
            }
        }

        // ── Lightbox de fotos ────────────────────────────────────────────
        lightboxIndex?.let { index ->
            FotoLightbox(
                fotos = fotos,
                initialIndex = index,
                onDismiss = { lightboxIndex = null }
            )
        }
    }
}

// ─── Hero carrossel de fotos ──────────────────────────────────────────────
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HeroCarrossel(
    fotos: List<StrapiMediaData>,
    nomeEdificio: String,
    descricao: String?,
    onFotoClick: (Int) -> Unit
) {
    val pagerState = rememberPagerState { fotos.size.coerceAtLeast(1) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        if (fotos.isEmpty()) {
            // Placeholder sem fotos
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(PactBlueSoft, PactBlack)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Image,
                    null,
                    tint = PactAccent.copy(alpha = 0.3f),
                    modifier = Modifier.size(64.dp)
                )
            }
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onFotoClick(page) }
                ) {
                    AsyncImage(
                        model = fotos[page].url,
                        contentDescription = fotos[page].name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Dots de navegação
            if (fotos.size > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 72.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    fotos.indices.forEach { i ->
                        Box(
                            modifier = Modifier
                                .size(if (i == pagerState.currentPage) 8.dp else 5.dp)
                                .clip(CircleShape)
                                .background(
                                    if (i == pagerState.currentPage) Color.White
                                    else Color.White.copy(0.4f)
                                )
                        )
                    }
                }
            }
        }

        // Gradiente inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(0.85f))
                    )
                )
        )

        // Badge edifício
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(PactAccent)
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                nomeEdificio,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 0.06.sp
            )
        }

        // Texto no fundo do hero
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Text(
                nomeEdificio,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 28.sp
            )
            if (!descricao.isNullOrBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    descricao,
                    fontSize = 13.sp,
                    color = Color.White.copy(0.75f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }
            // Contador de fotos
            if (fotos.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(0.4f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        "${pagerState.currentPage + 1} / ${fotos.size}",
                        fontSize = 11.sp,
                        color = Color.White.copy(0.8f)
                    )
                }
            }
        }
    }
}

// ─── Linha de empresa ─────────────────────────────────────────────────────

@Composable
private fun EmpresaRow(empresa: EmpresaData, onClick: () -> Unit) {
    val nome     = empresa.nome ?: "Sem nome"
    val category = empresa.category?.name ?: "Sem categoria"
    val descricao = empresa.descricao ?: ""
    val city     = empresa.city ?: ""
    val logoUrl  = empresa.logoRes?.url

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (!logoUrl.isNullOrBlank()) {
                AsyncImage(
                    model = logoUrl,
                    contentDescription = nome,
                    modifier = Modifier.fillMaxSize().padding(6.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text(
                    nome.take(2).uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PactOrange
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                nome,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = PactText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                category,
                fontSize = 11.sp,
                color = PactAccent,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 2.dp)
            )
            if (descricao.isNotBlank()) {
                Text(
                    descricao,
                    fontSize = 11.sp,
                    color = PactMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            if (city.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PactSurfaceAlt)
                        .border(0.5.dp, PactBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) { Text(city, fontSize = 10.sp, color = PactMuted) }
                Spacer(Modifier.height(4.dp))
            }
            Text("›", fontSize = 22.sp, color = PactMuted)
        }
    }
}

// ─── Lightbox fullscreen ──────────────────────────────────────────────────

@Composable
private fun FotoLightbox(
    fotos: List<StrapiMediaData>,
    initialIndex: Int,
    onDismiss: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(initialIndex.coerceIn(0, fotos.size - 1)) }
    val item = fotos[currentIndex]

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

            AsyncImage(
                model = item.url,
                contentDescription = item.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            Box(
                Modifier.fillMaxWidth().height(120.dp).align(Alignment.TopCenter)
                    .background(Brush.verticalGradient(listOf(Color.Black.copy(0.75f), Color.Transparent)))
            )
            Box(
                Modifier.fillMaxWidth().height(120.dp).align(Alignment.BottomCenter)
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.75f))))
            )

            Text(
                "${currentIndex + 1} / ${fotos.size}",
                color = Color.White.copy(0.7f),
                fontSize = 13.sp,
                modifier = Modifier.align(Alignment.TopStart).padding(20.dp)
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
            ) {
                Icon(Icons.Default.Close, "Fechar", tint = Color.White)
            }

            if (currentIndex > 0) {
                IconButton(
                    onClick = { currentIndex-- },
                    modifier = Modifier
                        .align(Alignment.CenterStart).padding(8.dp)
                        .clip(CircleShape).background(Color.Black.copy(0.45f))
                ) {
                    Icon(Icons.Default.NavigateBefore, "Anterior", tint = Color.White)
                }
            }
            if (currentIndex < fotos.size - 1) {
                IconButton(
                    onClick = { currentIndex++ },
                    modifier = Modifier
                        .align(Alignment.CenterEnd).padding(8.dp)
                        .clip(CircleShape).background(Color.Black.copy(0.45f))
                ) {
                    Icon(Icons.Default.NavigateNext, "Seguinte", tint = Color.White)
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                fotos.indices.forEach { i ->
                    Box(
                        modifier = Modifier
                            .size(if (i == currentIndex) 8.dp else 5.dp)
                            .clip(CircleShape)
                            .background(
                                if (i == currentIndex) Color.White
                                else Color.White.copy(0.3f)
                            )
                    )
                }
            }
        }
    }
}