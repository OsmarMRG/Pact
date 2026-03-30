package com.example.epact.ui.screens.companies

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.epact.model.Company
import com.example.epact.model.CompanyImage
import com.example.epact.ui.theme.PactAccent
import com.example.epact.ui.theme.PactBlack
import com.example.epact.ui.theme.PactBorder
import com.example.epact.ui.theme.PactCard
import com.example.epact.ui.theme.PactGreen
import com.example.epact.ui.theme.PactGreenSoft
import com.example.epact.ui.theme.PactMuted
import com.example.epact.ui.theme.PactSurfaceAlt
import com.example.epact.ui.theme.PactText

@Composable
fun CompanyDetailScreen(company: Company) {
    val context = LocalContext.current
    var lightboxIndex by remember { mutableStateOf<Int?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(PactBlack)
        ) {

            // ── 1. HEADER STRIP ─────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF063E32), PactGreen)
                        )
                    )
            )

            // ── 2. LOGO FLUTUANTE + FAVORITO ────────────────────────────────
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
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (company.logoRes != null) {
                                androidx.compose.foundation.Image(
                                    painter = painterResource(id = company.logoRes),
                                    contentDescription = company.name,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    contentScale = ContentScale.Fit
                                )
                            } else {
                                Text(
                                    text = company.name.first().toString(),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PactGreen
                                )
                            }
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
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = PactMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // ── 3. NOME E CATEGORIA ─────────────────────────────────────────
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-12).dp)
            ) {
                Text(
                    text = company.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = PactText,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = if (company.city.isNotBlank())
                        "${company.category} · ${company.city}"
                    else
                        company.category,
                    fontSize = 13.sp,
                    color = PactAccent,
                    fontWeight = FontWeight.Medium
                )
            }

            Divider()

            // ── 4. GALERIA DA EMPRESA ───────────────────────────────────────
            if (company.gallery.isNotEmpty()) {
                Column(modifier = Modifier.padding(vertical = 18.dp)) {
                    SectionLabel(
                        text = "GALERIA",
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(company.gallery) { index, item ->
                            GalleryCard(
                                item = item,
                                onClick = { lightboxIndex = index }
                            )
                        }
                    }
                }
                Divider()
            }

            // ── 5. SOBRE ────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                SectionLabel(text = "SOBRE")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = company.fullDescription,
                    fontSize = 14.sp,
                    color = PactText,
                    lineHeight = 22.sp
                )
            }

            Divider()

            // ── 6. ÁREAS / TAGS ─────────────────────────────────────────────
            if (company.tags.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    SectionLabel(text = "ÁREAS")
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        company.tags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(PactSurfaceAlt)
                                    .border(0.5.dp, PactBorder, RoundedCornerShape(20.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(tag, fontSize = 12.sp, color = PactText)
                            }
                        }
                    }
                }
                Divider()
            }

            // ── 7. WEBSITE ──────────────────────────────────────────────────
            if (!company.website.isNullOrBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            context.startActivity(
                                Intent(Intent.ACTION_VIEW, Uri.parse(company.website))
                            )
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
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = PactMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = company.website
                            .removePrefix("https://")
                            .removePrefix("http://")
                            .removeSuffix("/"),
                        fontSize = 13.sp,
                        color = PactAccent,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )
                    Text("›", fontSize = 20.sp, color = PactMuted)
                }
                Divider()
            }

            // ── 8. BOTÃO CTA ────────────────────────────────────────────────
            Box(modifier = Modifier.padding(20.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(PactAccent)
                        .clickable {
                            if (!company.website.isNullOrBlank()) {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(company.website))
                                )
                            }
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Visitar site",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // ── 9. LIGHTBOX ─────────────────────────────────────────────────────
        lightboxIndex?.let { index ->
            GalleryLightbox(
                items = company.gallery,
                initialIndex = index,
                onDismiss = { lightboxIndex = null }
            )
        }
    }
}

// ─── Componentes auxiliares ────────────────────────────────────────────────

@Composable
private fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(PactBorder)
    )
}

@Composable
private fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = PactMuted,
        letterSpacing = 0.08.sp,
        modifier = modifier
    )
}

@Composable
fun GalleryCard(item: CompanyImage, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        // ── Quando tiveres imagens reais, substitui este bloco por: ─────────
        // Image(
        //     painter = painterResource(id = item.imageRes),
        //     contentDescription = item.caption,
        //     modifier = Modifier.fillMaxSize(),
        //     contentScale = ContentScale.Crop
        // )
        // ────────────────────────────────────────────────────────────────────

        // Placeholder (remove quando tiveres imagens reais)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PactGreenSoft),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = PactGreen,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.placeholderLabel,
                    fontSize = 11.sp,
                    color = PactMuted
                )
            }
        }

        // Overlay com legenda
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f)),
                        startY = 40f
                    )
                )
        )
        Text(
            text = item.caption,
            fontSize = 11.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        )
    }
}

@Composable
fun GalleryLightbox(
    items: List<CompanyImage>,
    initialIndex: Int,
    onDismiss: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(initialIndex) }
    val item = items[currentIndex]

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // ── Imagem principal ─────────────────────────────────────────────
            // Quando tiveres imagens reais, substitui por:
            // Image(
            //     painter = painterResource(id = item.imageRes),
            //     contentDescription = item.caption,
            //     modifier = Modifier.fillMaxSize(),
            //     contentScale = ContentScale.Fit
            // )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PactGreenSoft),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = PactGreen,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(item.placeholderLabel, color = PactMuted, fontSize = 16.sp)
                }
            }

            // Gradiente superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black.copy(0.7f), Color.Transparent)
                        )
                    )
            )

            // Gradiente inferior
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

            // Botão fechar
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color.White)
            }

            // Contador de posição (ex: 2 / 4)
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(20.dp)
            ) {
                Text(
                    text = "${currentIndex + 1} / ${items.size}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Seta anterior
            if (currentIndex > 0) {
                IconButton(
                    onClick = { currentIndex-- },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(0.4f))
                ) {
                    Icon(
                        Icons.Default.NavigateBefore,
                        contentDescription = "Anterior",
                        tint = Color.White
                    )
                }
            }

            // Seta seguinte
            if (currentIndex < items.size - 1) {
                IconButton(
                    onClick = { currentIndex++ },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(0.4f))
                ) {
                    Icon(
                        Icons.Default.NavigateNext,
                        contentDescription = "Seguinte",
                        tint = Color.White
                    )
                }
            }

            // Legenda + pontos indicadores
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.caption,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items.indices.forEach { i ->
                        Box(
                            modifier = Modifier
                                .size(if (i == currentIndex) 8.dp else 5.dp)
                                .clip(CircleShape)
                                .background(
                                    if (i == currentIndex) Color.White
                                    else Color.White.copy(alpha = 0.35f)
                                )
                        )
                    }
                }
            }
        }
    }
}
