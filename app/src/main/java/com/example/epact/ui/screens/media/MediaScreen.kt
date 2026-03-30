package com.example.epact.ui.screens.media

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.epact.ui.theme.PactAccent
import com.example.epact.ui.theme.PactBlack
import com.example.epact.ui.theme.PactBorder
import com.example.epact.ui.theme.PactCard
import com.example.epact.ui.theme.PactGreen
import com.example.epact.ui.theme.PactGreenSoft
import com.example.epact.ui.theme.PactMuted
import com.example.epact.ui.theme.PactSurfaceAlt
import com.example.epact.ui.theme.PactText

// ─── Modelos de dados de media ─────────────────────────────────────────────

data class MediaPhoto(
    val id: Int,
    val caption: String,
    val placeholderLabel: String,
    // val imageRes: Int,   // descomenta quando tiveres imagens reais
)

data class MediaVideo(
    val id: Int,
    val title: String,
    val duration: String,
    val date: String,
    val youtubeUrl: String = "",   // coloca aqui o URL do YouTube quando disponível
    val placeholderLabel: String,
    // val thumbnailRes: Int,       // descomenta quando tiveres thumbnail real
)

// ─── Dados — substitui pelos reais quando disponíveis ──────────────────────

private val pactPhotos = listOf(
    MediaPhoto(1, "Entrada principal do PACT", "Fachada"),
    MediaPhoto(2, "Zona de coworking – piso 1", "Cowork"),
    MediaPhoto(3, "Auditório para 80 pessoas", "Auditório"),
    MediaPhoto(4, "Open space das startups", "Open Space"),
    MediaPhoto(5, "Salas de reunião privadas", "Reuniões"),
    MediaPhoto(6, "Zona de descanso e networking", "Networking"),
    MediaPhoto(7, "Receção e hall de entrada", "Receção"),
    MediaPhoto(8, "Vista aérea do espaço", "Vista aérea"),
    MediaPhoto(9, "Laboratório de inovação", "Lab"),
)

private val pactVideos = listOf(
    MediaVideo(
        id = 1,
        title = "Vista aérea do PACT – drone 4K",
        duration = "3 min 42 s",
        date = "Mar 2024",
        placeholderLabel = "Drone",
        youtubeUrl = ""   // coloca o URL aqui
    ),
    MediaVideo(
        id = 2,
        title = "Apresentação institucional do PACT",
        duration = "5 min 10 s",
        date = "Jan 2024",
        placeholderLabel = "Institucional",
        youtubeUrl = ""
    ),
    MediaVideo(
        id = 3,
        title = "Open Day 2024 – resumo do evento",
        duration = "2 min 18 s",
        date = "Fev 2024",
        placeholderLabel = "Open Day",
        youtubeUrl = ""
    ),
)

// ─── Ecrã principal ────────────────────────────────────────────────────────

@Composable
fun MediaScreen() {
    val context = LocalContext.current

    // hero em destaque (a primeira foto)
    val heroPhoto = pactPhotos.first()
    val gridPhotos = pactPhotos.drop(1).take(5)   // 5 fotos na grelha
    val remainingCount = pactPhotos.size - 1 - gridPhotos.size

    var lightboxIndex by remember { mutableStateOf<Int?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(PactBlack),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {

            // ── 1. HERO EM DESTAQUE ──────────────────────────────────────────
            item {
                HeroBlock(
                    photo = heroPhoto,
                    onTap = { lightboxIndex = 0 }
                )
            }

            // ── 2. LABEL SECÇÃO FOTOS ────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionLabel(text = "GALERIA DE FOTOS")
                    Text(
                        text = "${pactPhotos.size} fotos",
                        fontSize = 12.sp,
                        color = PactMuted
                    )
                }
            }

            // ── 3. GRELHA 3×N ────────────────────────────────────────────────
            item {
                // LazyVerticalGrid dentro de LazyColumn precisa de altura fixa
                val rows = Math.ceil((gridPhotos.size + 1) / 3.0).toInt()
                val cellSize = 110
                val gridHeight = (rows * cellSize + (rows - 1) * 2).dp

                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(gridHeight)
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    userScrollEnabled = false
                ) {
                    itemsIndexed(gridPhotos) { index, photo ->
                        PhotoCell(
                            photo = photo,
                            onClick = { lightboxIndex = index + 1 }  // +1 porque o hero é index 0
                        )
                    }

                    // Última célula: "ver todas" se houver mais fotos
                    if (remainingCount > 0) {
                        item {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PactCard)
                                    .clickable { lightboxIndex = gridPhotos.size + 1 },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "+$remainingCount",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PactText
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "ver todas",
                                        fontSize = 11.sp,
                                        color = PactMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── 4. DIVISOR ───────────────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(PactBorder)
                )
            }

            // ── 5. LABEL SECÇÃO VÍDEOS ───────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionLabel(text = "VÍDEOS")
                    Text(
                        text = "${pactVideos.size} vídeos",
                        fontSize = 12.sp,
                        color = PactMuted
                    )
                }
            }

            // ── 6. LISTA DE VÍDEOS ───────────────────────────────────────────
            items(pactVideos.size) { index ->
                val video = pactVideos[index]
                VideoRow(
                    video = video,
                    onTap = {
                        if (video.youtubeUrl.isNotBlank()) {
                            context.startActivity(
                                Intent(Intent.ACTION_VIEW, Uri.parse(video.youtubeUrl))
                            )
                        }
                    }
                )
                if (index < pactVideos.size - 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(0.5.dp)
                            .background(PactBorder)
                    )
                }
            }
        }

        // ── LIGHTBOX ─────────────────────────────────────────────────────────
        lightboxIndex?.let { index ->
            PhotoLightbox(
                photos = pactPhotos,
                initialIndex = index,
                onDismiss = { lightboxIndex = null }
            )
        }
    }
}

// ─── Hero block ────────────────────────────────────────────────────────────

@Composable
private fun HeroBlock(photo: MediaPhoto, onTap: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onTap() }
    ) {
        // Placeholder — substitui por Image() com painterResource quando tiveres a foto
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PactGreenSoft)
        )

        // Overlay gradiente
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(0.75f)),
                        startY = 80f
                    )
                )
        )

        // Badge "EM DESTAQUE"
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(PactAccent)
                .padding(horizontal = 9.dp, vertical = 4.dp)
        ) {
            Text(
                text = "EM DESTAQUE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 0.06.sp
            )
        }

        // Ícone de imagem centrado
        Icon(
            imageVector = Icons.Default.Image,
            contentDescription = null,
            tint = PactGreen.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.Center)
                .size(48.dp)
        )

        // Legenda em baixo
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = photo.caption,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = photo.placeholderLabel,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.65f)
            )
        }
    }
}

// ─── Célula da grelha ──────────────────────────────────────────────────────

@Composable
private fun PhotoCell(photo: MediaPhoto, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(PactGreenSoft)
            .clickable { onClick() }
    ) {
        // Placeholder — substitui por Image() quando tiveres a foto real
        Icon(
            imageVector = Icons.Default.Image,
            contentDescription = null,
            tint = PactGreen.copy(alpha = 0.4f),
            modifier = Modifier
                .align(Alignment.Center)
                .size(24.dp)
        )

        // Overlay + legenda
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(0.5f)),
                        startY = 30f
                    )
                )
        )
        Text(
            text = photo.placeholderLabel,
            fontSize = 10.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(6.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ─── Linha de vídeo ────────────────────────────────────────────────────────

@Composable
private fun VideoRow(video: MediaVideo, onTap: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTap() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Thumbnail
        Box(
            modifier = Modifier
                .size(width = 72.dp, height = 52.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PactGreenSoft),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder — substitui por Image() quando tiveres thumbnail real
            Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = null,
                tint = PactAccent,
                modifier = Modifier.size(26.dp)
            )
        }

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = video.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = PactText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = video.duration,
                    fontSize = 11.sp,
                    color = PactMuted
                )
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .clip(CircleShape)
                        .background(PactMuted)
                )
                Text(
                    text = video.date,
                    fontSize = 11.sp,
                    color = PactMuted
                )
            }
        }

        // Seta
        Text("›", fontSize = 20.sp, color = PactMuted)
    }
}

// ─── Lightbox fullscreen ───────────────────────────────────────────────────

@Composable
private fun PhotoLightbox(
    photos: List<MediaPhoto>,
    initialIndex: Int,
    onDismiss: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(initialIndex.coerceIn(0, photos.size - 1)) }
    val photo = photos[currentIndex]

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Imagem — substitui por Image() com painterResource quando tiveres fotos reais
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PactGreenSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = PactGreen,
                    modifier = Modifier.size(64.dp)
                )
            }

            // Gradiente topo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black.copy(0.75f), Color.Transparent)
                        )
                    )
            )

            // Gradiente fundo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(0.75f))
                        )
                    )
            )

            // Contador topo esquerdo
            Text(
                text = "${currentIndex + 1} / ${photos.size}",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(20.dp)
            )

            // Fechar
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Fechar",
                    tint = Color.White
                )
            }

            // Navegar — anterior
            if (currentIndex > 0) {
                IconButton(
                    onClick = { currentIndex-- },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(0.45f))
                ) {
                    Icon(Icons.Default.NavigateBefore, "Anterior", tint = Color.White)
                }
            }

            // Navegar — seguinte
            if (currentIndex < photos.size - 1) {
                IconButton(
                    onClick = { currentIndex++ },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(0.45f))
                ) {
                    Icon(Icons.Default.NavigateNext, "Seguinte", tint = Color.White)
                }
            }

            // Legenda + pontos
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = photo.caption,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    photos.indices.forEach { i ->
                        Box(
                            modifier = Modifier
                                .size(if (i == currentIndex) 8.dp else 5.dp)
                                .clip(CircleShape)
                                .background(
                                    if (i == currentIndex) Color.White
                                    else Color.White.copy(alpha = 0.3f)
                                )
                        )
                    }
                }
            }
        }
    }
}

// ─── Auxiliar ──────────────────────────────────────────────────────────────

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