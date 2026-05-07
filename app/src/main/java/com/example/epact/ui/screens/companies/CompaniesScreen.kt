package com.example.epact.ui.screens.companies

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.epact.R
import com.example.epact.model.EmpresaData
import com.example.epact.ui.components.CategoryFilters
import com.example.epact.ui.theme.PactAccent
import com.example.epact.ui.theme.PactBlack
import com.example.epact.ui.theme.PactBorder
import com.example.epact.ui.theme.PactBlueSoft
import com.example.epact.ui.theme.PactCard
import com.example.epact.ui.theme.PactMuted
import com.example.epact.ui.theme.PactOrange
import com.example.epact.ui.theme.PactSurfaceAlt
import com.example.epact.ui.theme.PactText
import kotlin.math.roundToInt

// ─── Zonas clicáveis calibradas para MapaPactNovo.png ────────────────────
//
// Imagem portrait ~900×1350px. Edifício principal (A) dividido em:
//   Piso 1 → bloco horizontal TOPO   (aprox. 3%–18% da altura)
//   Piso 0 → bloco horizontal ABAIXO (aprox. 18%–40% da altura)
//
// Os restantes edifícios ocupam a zona 40%–100%.

data class EdificioZone(
    val codigo: String,   // deve coincidir com EmpresaData.edificio e EdificioData.codigo
    val label: String,    // texto do chip visível na imagem
    val labelX: Float,    // posição X do chip (0..1)
    val labelY: Float,    // posição Y do chip (0..1)
    val zoneLeft: Float,
    val zoneTop: Float,
    val zoneWidth: Float,
    val zoneHeight: Float
)

private val edificioZones = listOf(

    // ── Piso 1 — bloco horizontal topo ───────────────────────────────
    EdificioZone(
        codigo = "1", label = "Piso 1",
        labelX = 0.50f, labelY = 0.06f,
        zoneLeft = 0.08f, zoneTop = 0.03f,
        zoneWidth = 0.84f, zoneHeight = 0.14f
    ),

    // ── Piso 0 — bloco horizontal imediatamente abaixo ───────────────
    EdificioZone(
        codigo = "0", label = "Piso 0",
        labelX = 0.50f, labelY = 0.24f,
        zoneLeft = 0.08f, zoneTop = 0.17f,
        zoneWidth = 0.84f, zoneHeight = 0.20f
    ),

    // ── C2 — barra fina horizontal, logo abaixo do edifício A ────────
    EdificioZone(
        codigo = "C2", label = "C2",
        labelX = 0.62f, labelY = 0.445f,
        zoneLeft = 0.28f, zoneTop = 0.42f,
        zoneWidth = 0.60f, zoneHeight = 0.06f
    ),

    // ── B — bloco esquerdo, zona média ───────────────────────────────
    EdificioZone(
        codigo = "B", label = "B",
        labelX = 0.16f, labelY = 0.57f,
        zoneLeft = 0.06f, zoneTop = 0.50f,
        zoneWidth = 0.26f, zoneHeight = 0.12f
    ),

    // ── D — bloco direita com forma hexagonal ────────────────────────
    EdificioZone(
        codigo = "D", label = "D",
        labelX = 0.63f, labelY = 0.555f,
        zoneLeft = 0.46f, zoneTop = 0.50f,
        zoneWidth = 0.30f, zoneHeight = 0.10f
    ),

    // ── C1 — barra fina horizontal, abaixo de B e D ──────────────────
    EdificioZone(
        codigo = "C1", label = "C1",
        labelX = 0.65f, labelY = 0.635f,
        zoneLeft = 0.20f, zoneTop = 0.61f,
        zoneWidth = 0.68f, zoneHeight = 0.06f
    ),

    // ── E — triângulo grande no fundo ────────────────────────────────
    EdificioZone(
        codigo = "E", label = "E",
        labelX = 0.50f, labelY = 0.88f,
        zoneLeft = 0.08f, zoneTop = 0.73f,
        zoneWidth = 0.84f, zoneHeight = 0.24f
    )
)

// ─── CompaniesScreen ──────────────────────────────────────────────────────

@Composable
fun CompaniesScreen(
    companyViewModel: CompanyViewModel = viewModel(),
    categories: List<String>,
    onCompanyClick: (Int) -> Unit,
    onEdificioClick: (String) -> Unit
) {
    val companies    by companyViewModel.companies
    val isLoading    by companyViewModel.isLoading
    val errorMessage by companyViewModel.errorMessage

    var showMap by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(PactBlack)) {

        ViewToggle(showMap = showMap, onToggle = { showMap = it })

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
                                .background(PactOrange)
                                .clickable { companyViewModel.loadCompanies() }
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text("Tentar novamente", color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }
            showMap -> MapaView(onEdificioClick = onEdificioClick)
            else    -> ListaView(
                companies = companies,
                categories = categories,
                onCompanyClick = onCompanyClick
            )
        }
    }
}

// ─── Toggle Lista / Mapa ──────────────────────────────────────────────────

@Composable
private fun ViewToggle(showMap: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(false to "Lista", true to "Mapa").forEach { (isMap, label) ->
            val active = showMap == isMap
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (active) PactOrange else PactCard)
                    .border(0.5.dp, if (active) PactOrange else PactBorder, RoundedCornerShape(12.dp))
                    .clickable { onToggle(isMap) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (isMap) Icons.Default.Map
                        else Icons.AutoMirrored.Filled.FormatListBulleted,
                        contentDescription = null,
                        tint = if (active) Color.White else PactMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        label, fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (active) Color.White else PactMuted
                    )
                }
            }
        }
    }
}

// ─── Vista de Mapa ────────────────────────────────────────────────────────

@Composable
private fun MapaView(onEdificioClick: (String) -> Unit) {
    var scale     by remember { mutableStateOf(1f) }
    var offset    by remember { mutableStateOf(Offset.Zero) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 5f)
        val maxX = (imageSize.width  * (scale - 1f)) / 2f
        val maxY = (imageSize.height * (scale - 1f)) / 2f
        offset = Offset(
            (offset.x + panChange.x).coerceIn(-maxX, maxX),
            (offset.y + panChange.y).coerceIn(-maxY, maxY)
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(PactBlueSoft)) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .transformable(state = transformState)
                .graphicsLayer(
                    scaleX = scale, scaleY = scale,
                    translationX = offset.x, translationY = offset.y
                )
                .onSizeChanged { imageSize = it }
        ) {
            Image(
                painter = painterResource(id = R.drawable.mapa_pact_novo),
                contentDescription = "Mapa PACT",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            if (imageSize.width > 0 && imageSize.height > 0) {
                edificioZones.forEach { zone ->

                    // Área clicável invisível
                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = (zone.zoneLeft * imageSize.width).roundToInt(),
                                    y = (zone.zoneTop  * imageSize.height).roundToInt()
                                )
                            }
                            .size(
                                width  = (zone.zoneWidth  * imageSize.width).dp,
                                height = (zone.zoneHeight * imageSize.height).dp
                            )
                            .clickable { onEdificioClick(zone.codigo) }
                    )

                    // Chip com label
                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = (zone.labelX * imageSize.width).roundToInt() - 24,
                                    y = (zone.labelY * imageSize.height).roundToInt() - 14
                                )
                            }
                            .clip(RoundedCornerShape(8.dp))
                            .background(PactAccent.copy(alpha = 0.92f))
                            .border(1.dp, Color.White.copy(0.25f), RoundedCornerShape(8.dp))
                            .clickable { onEdificioClick(zone.codigo) }
                            .padding(horizontal = 9.dp, vertical = 5.dp)
                    ) {
                        Text(
                            zone.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Dica
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 14.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(PactCard.copy(alpha = 0.93f))
                .border(0.5.dp, PactBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            Text(
                "Pinça para zoom · Toca num edifício",
                fontSize = 11.sp,
                color = PactMuted
            )
        }
    }
}

// ─── Vista de Lista ───────────────────────────────────────────────────────

@Composable
private fun ListaView(
    companies: List<EmpresaData>,
    categories: List<String>,
    onCompanyClick: (Int) -> Unit
) {
    var search           by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("Todos") }

    val filtered = companies.filter { e ->
        val matchSearch = (e.nome ?: "").contains(search, ignoreCase = true) ||
                (e.descricao ?: "").contains(search, ignoreCase = true)
        val matchCat = selectedCategory == "Todos" ||
                e.category?.name?.equals(selectedCategory, ignoreCase = true) == true
        matchSearch && matchCat
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(PactBlack),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        item {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Pesquisar empresa...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = RoundedCornerShape(14.dp)
            )
            Spacer(Modifier.height(10.dp))
        }
        item {
            CategoryFilters(
                categories = categories,
                selectedCategory = selectedCategory,
                onSelect = { selectedCategory = it }
            )
            Spacer(Modifier.height(6.dp))
        }
        item {
            Text(
                "${filtered.size} empresa${if (filtered.size != 1) "s" else ""}",
                fontSize = 11.sp, color = PactMuted,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
        items(filtered) { empresa ->
            DirectoryRow(empresa = empresa, onClick = { onCompanyClick(empresa.id) })
            Box(Modifier.fillMaxWidth().height(0.5.dp).background(PactBorder))
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

// ─── Linha de empresa ─────────────────────────────────────────────────────

@Composable
private fun DirectoryRow(empresa: EmpresaData, onClick: () -> Unit) {
    val nome      = empresa.nome ?: "Sem nome"
    val descricao = empresa.descricao ?: ""
    val category  = empresa.category?.name ?: "Sem categoria"
    val city      = empresa.city ?: ""
    val logoUrl   = empresa.logoRes?.url

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
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
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = PactOrange
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(nome, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                color = PactText, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(category, fontSize = 11.sp, color = PactAccent,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 2.dp))
            if (descricao.isNotBlank()) {
                Text(descricao, fontSize = 11.sp, color = PactMuted,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp))
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
            Text("›", fontSize = 20.sp, color = PactBorder)
        }
    }
}