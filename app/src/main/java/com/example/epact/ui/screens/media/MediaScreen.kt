package com.example.epact.ui.screens.media

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.epact.model.MediaPactData
import com.example.epact.ui.theme.PactAccent
import com.example.epact.ui.theme.PactBlack
import com.example.epact.ui.theme.PactBorder
import com.example.epact.ui.theme.PactCard
import com.example.epact.ui.theme.PactOrange
import com.example.epact.ui.theme.PactBlueSoft
import com.example.epact.ui.theme.PactMuted
import com.example.epact.ui.theme.PactText

@Composable
fun MediaScreen(
    viewModel: MediaViewModel = viewModel()
) {
    val context = LocalContext.current
    val items by viewModel.items
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage

    // Separa os itens por tipo
    val hero = items.firstOrNull { it.destaque == true }
        ?: items.firstOrNull { it.tipo == "foto" }
    val fotos = items.filter { it.tipo == "foto" && it.id != hero?.id }
    val videos = items.filter { it.tipo == "video" }

    // Estado do lightbox — inclui o hero (índice 0 = hero, resto = fotos)
    val todasFotos = listOfNotNull(hero) + fotos
    var lightboxIndex by remember { mutableStateOf<Int?>(null) }
    var videoUrlToPlay by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PactAccent)
                }
            }

            errorMessage != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(errorMessage ?: "", color = PactMuted)
                        Spacer(Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(PactAccent)
                                .clickable { viewModel.loadMedia() }
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text("Tentar novamente", color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }

            items.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Sem conteúdo disponível", color = PactMuted)
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PactBlack),
                    contentPadding = PaddingValues(bottom = 40.dp)
                ) {

                    // ── 1. HERO EM DESTAQUE ──────────────────────────────────
                    hero?.let { heroItem ->
                        item {
                            HeroBlock(
                                item = heroItem,
                                onTap = { lightboxIndex = 0 }
                            )
                        }
                    }

                    // ── 2. GALERIA DE FOTOS ──────────────────────────────────
                    if (fotos.isNotEmpty()) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SectionLabel("GALERIA DE FOTOS")
                                Text(
                                    "${fotos.size} foto${if (fotos.size != 1) "s" else ""}",
                                    fontSize = 12.sp, color = PactMuted
                                )
                            }
                        }

                        item {
                            val cols = 3
                            val rows = kotlin.math.ceil(fotos.size / cols.toDouble()).toInt()
                            val gridHeight = (rows * 112 + (rows - 1) * 2).dp

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(cols),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(gridHeight)
                                    .padding(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                                userScrollEnabled = false
                            ) {
                                itemsIndexed(fotos) { index, foto ->
                                    FotoCell(
                                        item = foto,
                                        onClick = {
                                            lightboxIndex = index + 1
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // ── 3. DIVISOR ───────────────────────────────────────────
                    if (videos.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(24.dp))
                            Box(Modifier.fillMaxWidth().height(0.5.dp).background(PactBorder))
                        }

                        // ── 4. VÍDEOS ────────────────────────────────────────
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SectionLabel("VÍDEOS")
                                Text(
                                    "${videos.size} vídeo${if (videos.size != 1) "s" else ""}",
                                    fontSize = 12.sp, color = PactMuted
                                )
                            }
                        }

                        items(videos.size) { index ->
                            val videoItem = videos[index]
                            VideoRow(
                                item = videoItem,
                                onTap = {
                                    val url = videoItem.video
                                    if (!url.isNullOrBlank()) {
                                        videoUrlToPlay = url // Abre o vídeo na app!
                                    }
                                }
                            )
                            if (index < videos.size - 1) {
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
                }
            }
        }

        // ── LIGHTBOX DE FOTOS ────────────────────────────────────────────
        lightboxIndex?.let { index ->
            FotoLightbox(
                fotos = todasFotos,
                initialIndex = index,
                onDismiss = { lightboxIndex = null }
            )
        }

        // ── OVERLAY DE VÍDEOS (SOLUÇÃO DEFINITIVA SEM DIALOG) ────────────
        videoUrlToPlay?.let { url ->
            VideoPlayerOverlay(
                url = url,
                onDismiss = { videoUrlToPlay = null }
            )
        }
    }
}

// ─── Hero block ────────────────────────────────────────────────────────────

@Composable
private fun HeroBlock(item: MediaPactData, onTap: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clickable { onTap() }
    ) {
        if (item.imagem?.url != null) {
            AsyncImage(
                model = item.imagem.url,
                contentDescription = item.titulo,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(Modifier.fillMaxSize().background(PactBlueSoft))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(0.8f)),
                        startY = 80f
                    )
                )
        )

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

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            if (!item.titulo.isNullOrBlank()) {
                Text(
                    text = item.titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (!item.legenda.isNullOrBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.legenda,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ─── Célula de foto na grelha ───────────────────────────────────────────────

@Composable
private fun FotoCell(item: MediaPactData, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(6.dp))
            .background(PactBlueSoft)
            .clickable { onClick() }
    ) {
        if (item.imagem?.url != null) {
            AsyncImage(
                model = item.imagem.url,
                contentDescription = item.titulo,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                Icons.Default.Image, null,
                tint = PactOrange.copy(alpha = 0.4f),
                modifier = Modifier.align(Alignment.Center).size(24.dp)
            )
        }

        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(0.5f)),
                        startY = 40f
                    )
                )
        )
        if (!item.titulo.isNullOrBlank()) {
            Text(
                text = item.titulo,
                fontSize = 9.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(5.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ─── Linha de vídeo ────────────────────────────────────────────────────────

@Composable
private fun VideoRow(item: MediaPactData, onTap: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTap() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 80.dp, height = 56.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PactBlueSoft),
            contentAlignment = Alignment.Center
        ) {
            if (item.imagem?.url != null) {
                AsyncImage(
                    model = item.imagem.url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PlayCircle, null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else {
                Icon(
                    Icons.Default.PlayCircle, null,
                    tint = PactAccent,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.titulo ?: "Sem título",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = PactText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )
            if (!item.legenda.isNullOrBlank()) {
                Spacer(Modifier.height(3.dp))
                Text(
                    text = item.legenda,
                    fontSize = 11.sp,
                    color = PactMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (item.destaque == true) {
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(PactAccent.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("Destaque", fontSize = 10.sp, color = PactAccent, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Text("›", fontSize = 20.sp, color = PactMuted)
    }
}

// ─── Lightbox de fotos ─────────────────────────────────────────────────────

@Composable
private fun FotoLightbox(
    fotos: List<MediaPactData>,
    initialIndex: Int,
    onDismiss: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(initialIndex.coerceIn(0, fotos.size - 1)) }
    val item = fotos[currentIndex]

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            if (item.imagem?.url != null) {
                AsyncImage(
                    model = item.imagem.url,
                    contentDescription = item.titulo,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            } else {
                Box(
                    Modifier.fillMaxSize().background(PactBlueSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Image, null, tint = PactOrange, modifier = Modifier.size(64.dp))
                }
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .align(Alignment.TopCenter)
                    .background(Brush.verticalGradient(listOf(Color.Black.copy(0.75f), Color.Transparent)))
            )

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .align(Alignment.BottomCenter)
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.8f))))
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
                        .align(Alignment.CenterStart)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(0.45f))
                ) {
                    Icon(Icons.Default.NavigateBefore, "Anterior", tint = Color.White)
                }
            }

            if (currentIndex < fotos.size - 1) {
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

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!item.titulo.isNullOrBlank()) {
                    Text(
                        item.titulo,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (!item.legenda.isNullOrBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        item.legenda,
                        color = Color.White.copy(0.65f),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
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
}

// ─── Auxiliares ────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = PactMuted,
        letterSpacing = 0.08.sp
    )
}

// ─── Player Interno Definitivo (SEM DIALOG) ────────────────────────────────

@Composable
fun VideoPlayerOverlay(url: String, onDismiss: () -> Unit) {
    // Interceta o botão "Voltar" do telemóvel para fechar o vídeo
    BackHandler(onBack = onDismiss)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            // Impede cliques fantasmas na lista que ficou por trás do fundo preto
            .clickable(onClick = {}),
        contentAlignment = Alignment.Center
    ) {
        val videoId = getYouTubeId(url)

        if (videoId != null) {
            androidx.compose.ui.viewinterop.AndroidView(
                factory = { ctx ->
                    android.webkit.WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true

                        webChromeClient = android.webkit.WebChromeClient()
                        webViewClient = android.webkit.WebViewClient()
                        setBackgroundColor(android.graphics.Color.BLACK)

                        // Carrega o YouTube sem autoplay forçado
                        loadUrl("https://www.youtube.com/embed/$videoId")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )
        } else {
            Text(
                text = "Link de vídeo inválido",
                color = Color.White
            )
        }

        // Botão de Fechar no canto superior direito
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 16.dp)
        ) {
            Icon(Icons.Default.Close, "Fechar", tint = Color.White, modifier = Modifier.size(32.dp))
        }
    }
}

private fun getYouTubeId(url: String): String? {
    return when {
        url.contains("v=") -> url.substringAfter("v=").substringBefore("&")
        url.contains("youtu.be/") -> url.substringAfter("youtu.be/").substringBefore("?")
        url.contains("shorts/") -> url.substringAfter("shorts/").substringBefore("?")
        url.contains("live/") -> url.substringAfter("live/").substringBefore("?")
        else -> null
    }
}