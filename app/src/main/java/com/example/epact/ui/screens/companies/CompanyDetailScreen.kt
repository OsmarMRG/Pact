package com.example.epact.ui.screens.companies

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.epact.model.CompanyImage
import com.example.epact.model.StrapiMediaData
import com.example.epact.ui.theme.PactAccent
import com.example.epact.ui.theme.PactBlack
import com.example.epact.ui.theme.PactBorder
import com.example.epact.ui.theme.PactCard
import com.example.epact.ui.theme.PactGreen
import com.example.epact.ui.theme.PactGreenSoft
import com.example.epact.ui.theme.PactMuted
import com.example.epact.ui.theme.PactSurfaceAlt
import com.example.epact.ui.theme.PactText

private const val STRAPI_BASE = "https://meaningful-desire-049927a41b.strapiapp.com"

@Composable
fun CompanyDetailScreen(
    companyId: Int,
    viewModel: CompanyViewModel = viewModel()
) {
    val context = LocalContext.current
    val companies by viewModel.companies
    val isLoading by viewModel.isLoading

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PactAccent)
        }
        return
    }

    // Strapi v5: EmpresaData tem campos diretos, sem .attributes
    val empresa = companies.firstOrNull { it.id == companyId }

    if (empresa == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Empresa não encontrada", color = PactMuted)
        }
        return
    }

    val nome = empresa.nome ?: "Sem nome"
    val descricao = empresa.descricao ?: ""
    val category = empresa.category ?: "Sem categoria"
    val city = empresa.city ?: ""
    val website = empresa.url ?: ""
    val galeria = empresa.Galeria ?: emptyList()

    var lightboxIndex by remember { mutableStateOf<Int?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(PactBlack)
        ) {

            // ── 1. HEADER STRIP ──────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF063E32), PactGreen))
                    )
            )

            // ── 2. LOGO FLUTUANTE + FAVORITO ─────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-28).dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.size(58.dp)
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            // Futuramente: AsyncImage com empresa.logo?.url via Coil
                            // val logoUrl = STRAPI_BASE + (empresa.logo?.url ?: "")
                            Text(
                                text = nome.first().toString(),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = PactGreen
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(PactCard)
                            .border(0.5.dp, PactBorder, CircleShape)
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.FavoriteBorder, "Favorito", tint = PactMuted,
                            modifier = Modifier.size(18.dp))
                    }
                }
            }

            // ── 3. NOME E CATEGORIA ──────────────────────────────────────────
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-12).dp)
            ) {
                Text(nome, fontSize = 22.sp, fontWeight = FontWeight.Bold,
                    color = PactText, lineHeight = 28.sp)
                Spacer(Modifier.height(3.dp))
                Text(
                    text = if (city.isNotBlank()) "$category · $city" else category,
                    fontSize = 13.sp, color = PactAccent, fontWeight = FontWeight.Medium
                )
            }

            Divider()

            // ── 4. GALERIA DO STRAPI ─────────────────────────────────────────
            if (galeria.isNotEmpty()) {
                Column(modifier = Modifier.padding(vertical = 18.dp)) {
                    SectionLabel("GALERIA", Modifier.padding(horizontal = 20.dp))
                    Spacer(Modifier.height(10.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(galeria) { index, media ->
                            StrapiGalleryCard(
                                media = media,
                                onClick = { lightboxIndex = index }
                            )
                        }
                    }
                }
                Divider()
            }

            // ── 5. SOBRE ─────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                SectionLabel("SOBRE")
                Spacer(Modifier.height(8.dp))
                Text(descricao, fontSize = 14.sp, color = PactText, lineHeight = 22.sp)
            }

            Divider()

            // ── 6. WEBSITE ───────────────────────────────────────────────────
            if (website.isNotBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(website)))
                        }
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PactSurfaceAlt),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Language, null, tint = PactMuted, modifier = Modifier.size(16.dp))
                    }
                    Text(
                        text = website.removePrefix("https://").removePrefix("http://").removeSuffix("/"),
                        fontSize = 13.sp, color = PactAccent,
                        modifier = Modifier.weight(1f), maxLines = 1
                    )
                    Text("›", fontSize = 20.sp, color = PactMuted)
                }
                Divider()
            }

            // ── 7. BOTÃO CTA ─────────────────────────────────────────────────
            Box(modifier = Modifier.padding(20.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(PactAccent)
                        .clickable {
                            if (website.isNotBlank()) {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(website)))
                            }
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Visitar site", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }

            Spacer(Modifier.height(32.dp))
        }

        // ── 8. LIGHTBOX ──────────────────────────────────────────────────────
        lightboxIndex?.let { index ->
            StrapiLightbox(
                items = galeria,
                initialIndex = index,
                onDismiss = { lightboxIndex = null }
            )
        }
    }
}

// ─── Card de galeria do Strapi ───────────────────────────────────────────────

@Composable
fun StrapiGalleryCard(media: StrapiMediaData, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(PactGreenSoft)
            .clickable { onClick() }
    ) {
        // Futuramente substitui por AsyncImage quando tiveres Coil:
        // AsyncImage(
        //     model = STRAPI_BASE + (media.url ?: ""),
        //     contentDescription = media.name,
        //     modifier = Modifier.fillMaxSize(),
        //     contentScale = ContentScale.Crop
        // )

        // Placeholder
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Image, null, tint = PactGreen, modifier = Modifier.size(28.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Black.copy(0.55f)), startY = 40f))
        )
        Text(
            text = media.name ?: "",
            fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)
        )
    }
}

// ─── Lightbox do Strapi ──────────────────────────────────────────────────────

@Composable
fun StrapiLightbox(items: List<StrapiMediaData>, initialIndex: Int, onDismiss: () -> Unit) {
    var currentIndex by remember { mutableStateOf(initialIndex.coerceIn(0, items.size - 1)) }
    val item = items[currentIndex]

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

            // Placeholder — substitui por AsyncImage com Coil
            Box(Modifier.fillMaxSize().background(PactGreenSoft), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Image, null, tint = PactGreen, modifier = Modifier.size(64.dp))
            }

            Box(Modifier.fillMaxWidth().height(120.dp).align(Alignment.TopCenter)
                .background(Brush.verticalGradient(listOf(Color.Black.copy(0.75f), Color.Transparent))))
            Box(Modifier.fillMaxWidth().height(140.dp).align(Alignment.BottomCenter)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.75f)))))

            Text("${currentIndex + 1} / ${items.size}", color = Color.White.copy(0.7f),
                fontSize = 13.sp, modifier = Modifier.align(Alignment.TopStart).padding(20.dp))

            IconButton(onClick = onDismiss, modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)) {
                Icon(Icons.Default.Close, "Fechar", tint = Color.White)
            }
            if (currentIndex > 0) {
                IconButton(onClick = { currentIndex-- },
                    modifier = Modifier.align(Alignment.CenterStart).padding(8.dp)
                        .clip(CircleShape).background(Color.Black.copy(0.45f))) {
                    Icon(Icons.Default.NavigateBefore, "Anterior", tint = Color.White)
                }
            }
            if (currentIndex < items.size - 1) {
                IconButton(onClick = { currentIndex++ },
                    modifier = Modifier.align(Alignment.CenterEnd).padding(8.dp)
                        .clip(CircleShape).background(Color.Black.copy(0.45f))) {
                    Icon(Icons.Default.NavigateNext, "Seguinte", tint = Color.White)
                }
            }

            Column(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 36.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(item.name ?: "", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items.indices.forEach { i ->
                        Box(modifier = Modifier
                            .size(if (i == currentIndex) 8.dp else 5.dp)
                            .clip(CircleShape)
                            .background(if (i == currentIndex) Color.White else Color.White.copy(0.3f)))
                    }
                }
            }
        }
    }
}

// ─── Auxiliares ──────────────────────────────────────────────────────────────

@Composable
private fun Divider() {
    Box(Modifier.fillMaxWidth().height(0.5.dp).background(PactBorder))
}

@Composable
private fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(text, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
        color = PactMuted, letterSpacing = 0.08.sp, modifier = modifier)
}

// Mantém compatibilidade com GalleryCard/GalleryLightbox antigos se necessário
@Composable
fun GalleryCard(item: CompanyImage, onClick: () -> Unit) {
    Box(
        modifier = Modifier.width(160.dp).height(110.dp)
            .clip(RoundedCornerShape(14.dp)).background(PactGreenSoft).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Image, null, tint = PactGreen, modifier = Modifier.size(28.dp))
        Box(Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.55f)), startY = 40f)))
        Text(item.caption, fontSize = 11.sp, color = Color.White,
            modifier = Modifier.align(Alignment.BottomStart).padding(8.dp))
    }
}

@Composable
fun GalleryLightbox(items: List<CompanyImage>, initialIndex: Int, onDismiss: () -> Unit) {
    var currentIndex by remember { mutableStateOf(initialIndex) }
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Image, null, tint = PactGreen, modifier = Modifier.size(64.dp))
            IconButton(onClick = onDismiss, modifier = Modifier.align(Alignment.TopEnd).padding(12.dp)) {
                Icon(Icons.Default.Close, "Fechar", tint = Color.White)
            }
        }
    }
}