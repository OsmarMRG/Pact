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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.epact.R
import com.example.epact.model.Company
import com.example.epact.model.EmpresaData
import com.example.epact.ui.components.CategoryFilters
import com.example.epact.ui.components.SectionTitle
import com.example.epact.ui.theme.PactAccent
import com.example.epact.ui.theme.PactBlack
import com.example.epact.ui.theme.PactBorder
import com.example.epact.ui.theme.PactCard
import com.example.epact.ui.theme.PactGreen
import com.example.epact.ui.theme.PactMuted
import com.example.epact.ui.theme.PactSurfaceAlt
import com.example.epact.ui.theme.PactText
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// ─── Mapeamento edifício → empresas (por nome, para casar com o Strapi) ────
// Quando adicionares todas as empresas ao Strapi, certifica-te que o campo
// `nome` bate certo com os nomes aqui. Ajusta pinX/pinY conforme necessário.
data class MapBuilding(
    val id: String,
    val label: String,       // número visível no pin (ex: "A", "B", "C1")
    val subtitle: String,    // nome descritivo do edifício
    val pinX: Float,         // 0f = esquerda, 1f = direita
    val pinY: Float,         // 0f = topo, 1f = fundo
    val companyNames: List<String>  // nomes das empresas neste edifício
)

private val mapBuildings = listOf(
    MapBuilding(
        id = "A0", label = "A", subtitle = "Edifício A · Piso 0",
        pinX = 0.36f, pinY = 0.22f,
        companyNames = listOf("Interprev", "foursolutions", "Qstaff", "Auditório")
    ),
    MapBuilding(
        id = "A1", label = "A", subtitle = "Edifício A · Piso 1",
        pinX = 0.64f, pinY = 0.11f,
        companyNames = listOf("NTT DATA", "Solvit", "PropWorx", "DigitalWorks",
            "IPParking", "SDAC", "Verde100Truques")
    ),
    MapBuilding(
        id = "B", label = "B", subtitle = "Edifício B",
        pinX = 0.16f, pinY = 0.60f,
        companyNames = listOf("BSO Consulting", "IG&H")
    ),
    MapBuilding(
        id = "C1", label = "C1", subtitle = "Edifício C1",
        pinX = 0.44f, pinY = 0.70f,
        companyNames = listOf("Peak&Peak", "Vidigal Silva & Carlos Silva", "N10GLED",
            "Empowered Startups", "Jerónimo Martins")
    ),
    MapBuilding(
        id = "C2", label = "C2", subtitle = "Edifício C2",
        pinX = 0.60f, pinY = 0.57f,
        companyNames = listOf("Fraunhofer Portugal", "Trustworthy AI", "IPParking")
    ),
    MapBuilding(
        id = "D", label = "D", subtitle = "Edifício D",
        pinX = 0.75f, pinY = 0.63f,
        companyNames = listOf("TE Connectivity")
    ),
    MapBuilding(
        id = "E", label = "E", subtitle = "Edifício E",
        pinX = 0.44f, pinY = 0.87f,
        companyNames = listOf("CEiiA", "KPMG", "Jerónimo Martins", "Bee 2 Solutions")
    )
)

// ─── Ecrã principal ─────────────────────────────────────────────────────────

@Composable
fun CompaniesScreen(
    viewModel: CompanyViewModel = viewModel(),
    categories: List<String>,
    onCompanyClick: (Int) -> Unit
) {
    val companies by viewModel.companies
    val isLoading by viewModel.isLoading

    // Tab activa: false = Lista, true = Mapa
    var showMap by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PactBlack)
    ) {
        // ── TOGGLE LISTA / MAPA ──────────────────────────────────────────────
        ViewToggle(
            showMap = showMap,
            onToggle = { showMap = it }
        )

        // ── CONTEÚDO ─────────────────────────────────────────────────────────
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PactAccent)
            }
        } else if (showMap) {
            MapaView(
                allCompanies = companies,
                onCompanyClick = onCompanyClick
            )
        } else {
            ListaView(
                companies = companies,
                categories = categories,
                onCompanyClick = onCompanyClick
            )
        }
    }
}

// ─── Toggle Lista / Mapa ────────────────────────────────────────────────────

@Composable
private fun ViewToggle(showMap: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Botão Lista
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(if (!showMap) PactGreen else PactCard)
                .border(
                    0.5.dp,
                    if (!showMap) PactGreen else PactBorder,
                    RoundedCornerShape(12.dp)
                )
                .clickable { onToggle(false) }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Default.FormatListBulleted,
                    contentDescription = null,
                    tint = if (!showMap) Color.White else PactMuted,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Lista",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (!showMap) Color.White else PactMuted
                )
            }
        }

        // Botão Mapa
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(if (showMap) PactGreen else PactCard)
                .border(
                    0.5.dp,
                    if (showMap) PactGreen else PactBorder,
                    RoundedCornerShape(12.dp)
                )
                .clickable { onToggle(true) }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Default.Map,
                    contentDescription = null,
                    tint = if (showMap) Color.White else PactMuted,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Mapa",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (showMap) Color.White else PactMuted
                )
            }
        }
    }
}

// ─── Vista em Lista ──────────────────────────────────────────────────────────

@Composable
private fun ListaView(
    companies: List<EmpresaData>,
    categories: List<String>,
    onCompanyClick: (Int) -> Unit
) {
    var search by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("Todos") }

    val filtered = companies.filter { e ->
        // AJUSTE STRAPI 5: Removido .attributes
        val matchSearch = (e.nome ?: "").contains(search, ignoreCase = true) ||
                (e.descricao ?: "").contains(search, ignoreCase = true)
        val matchCat = selectedCategory == "Todos" ||
                e.category?.equals(selectedCategory, ignoreCase = true) == true
        matchSearch && matchCat
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PactBlack),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Barra de pesquisa
        item {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Pesquisar empresa...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = RoundedCornerShape(14.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Filtros de categoria
        item {
            CategoryFilters(
                categories = categories,
                selectedCategory = selectedCategory,
                onSelect = { selectedCategory = it }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Contador
        item {
            Text(
                text = "${filtered.size} empresa${if (filtered.size != 1) "s" else ""}",
                fontSize = 11.sp,
                color = PactMuted,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }

        // Empresas — estilo Directory
        items(filtered) { empresaData ->
            DirectoryRow(
                empresaData = empresaData,
                onClick = { onCompanyClick(empresaData.id) }
            )
            // Divisor
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(PactBorder)
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

// ─── Linha de empresa estilo Directory ──────────────────────────────────────

@Composable
private fun DirectoryRow(
    empresaData: EmpresaData,
    onClick: () -> Unit
) {
    val attr = empresaData.attributes

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Logo — quando Coil estiver integrado, substitui pelo AsyncImage com o URL do Strapi
        // val logoUrl = "https://meaningful-desire-049927a41b.strapiapp.com" +
        //               (attr.logo?.data?.attributes?.url ?: "")
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = attr.nome.take(2).uppercase(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = PactGreen
            )
        }

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = attr.nome,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = PactText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = attr.category ?: "Sem categoria",
                fontSize = 11.sp,
                color = PactAccent,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 2.dp)
            )
            Text(
                text = attr.descricao,
                fontSize = 11.sp,
                color = PactMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // Cidade + seta
        Column(horizontalAlignment = Alignment.End) {
            if (!attr.city.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PactSurfaceAlt)
                        .border(0.5.dp, PactBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(attr.city, fontSize = 10.sp, color = PactMuted)
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text("›", fontSize = 20.sp, color = PactBorder)
        }
    }
}

// ─── Vista do Mapa ───────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapaView(
    allCompanies: List<EmpresaData>,
    onCompanyClick: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    var selectedBuilding by remember { mutableStateOf<MapBuilding?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    // Zoom e pan
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 5f)
        val maxX = (imageSize.width * (scale - 1f)) / 2f
        val maxY = (imageSize.height * (scale - 1f)) / 2f
        offset = Offset(
            x = (offset.x + panChange.x).coerceIn(-maxX, maxX),
            y = (offset.y + panChange.y).coerceIn(-maxY, maxY)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── MAPA COM PINS ────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxSize()
                .transformable(state = transformState)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .onSizeChanged { imageSize = it }
        ) {
            // ⚠️ ADICIONA O FICHEIRO PNG EM: app/src/main/res/drawable/mapa_pact.png
            // É o mesmo ficheiro PNG da sinalética que enviaste.
            Image(
                painter = painterResource(id = R.drawable.mapa_pact),
                contentDescription = "Mapa do PACT",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            // Pins por edifício
            mapBuildings.forEach { building ->
                if (imageSize.width > 0 && imageSize.height > 0) {
                    val pinX = (building.pinX * imageSize.width).roundToInt()
                    val pinY = (building.pinY * imageSize.height).roundToInt()

                    // Conta quantas empresas do Strapi estão neste edifício
                    val count = building.companyNames.count { name ->
                        allCompanies.any {
                            it.attributes.nome.contains(name, ignoreCase = true) ||
                                    name.contains(it.attributes.nome, ignoreCase = true)
                        }
                    }.takeIf { it > 0 } ?: building.companyNames.size

                    BuildingPin(
                        building = building,
                        count = count,
                        pinX = pinX,
                        pinY = pinY,
                        onClick = {
                            selectedBuilding = building
                            scope.launch { sheetState.show() }
                        }
                    )
                }
            }
        }

        // Dica de uso
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(PactCard.copy(alpha = 0.92f))
                .border(0.5.dp, PactBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            Text(
                text = "Pinça para zoom · Toca num edifício",
                fontSize = 11.sp,
                color = PactMuted
            )
        }
    }

    // ── BOTTOM SHEET ─────────────────────────────────────────────────────────
    selectedBuilding?.let { building ->
        // Filtra as empresas do Strapi que pertencem a este edifício
        val buildingCompanies = building.companyNames.mapNotNull { name ->
            allCompanies.firstOrNull {
                it.attributes.nome.contains(name, ignoreCase = true) ||
                        name.contains(it.attributes.nome, ignoreCase = true)
            }
        }.distinctBy { it.id }

        ModalBottomSheet(
            onDismissRequest = { selectedBuilding = null },
            sheetState = sheetState,
            containerColor = PactCard,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            BuildingSheet(
                building = building,
                companies = buildingCompanies,
                onCompanyClick = { companyId ->
                    scope.launch { sheetState.hide() }
                    selectedBuilding = null
                    onCompanyClick(companyId)
                },
                onDismiss = {
                    scope.launch { sheetState.hide() }
                    selectedBuilding = null
                }
            )
        }
    }
}

// ─── Pin de edifício ─────────────────────────────────────────────────────────

@Composable
private fun BuildingPin(
    building: MapBuilding,
    count: Int,
    pinX: Int,
    pinY: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .offset { IntOffset(pinX - 24, pinY - 48) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Bolha principal
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(PactAccent)
                .border(2.dp, Color.White, RoundedCornerShape(50.dp))
                .clickable { onClick() }
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = building.label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (count > 0) {
                    Box(
                        modifier = Modifier
                            .size(17.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$count",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = PactAccent
                        )
                    }
                }
            }
        }
        // Ponta do pin
        Box(
            modifier = Modifier
                .size(width = 8.dp, height = 5.dp)
                .background(PactAccent)
        )
    }
}

// ─── Bottom Sheet do edifício ────────────────────────────────────────────────

@Composable
private fun BuildingSheet(
    building: MapBuilding,
    companies: List<EmpresaData>,
    onCompanyClick: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        // Cabeçalho
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = building.subtitle,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = PactText
                )
                Text(
                    text = "${companies.size} empresa${if (companies.size != 1) "s" else ""}",
                    fontSize = 12.sp,
                    color = PactMuted,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Fechar", tint = PactMuted)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(PactBorder)
        )

        // Lista de empresas do edifício
        companies.forEach { empresaData ->
            SheetCompanyRow(
                empresaData = empresaData,
                onClick = { onCompanyClick(empresaData.id) }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(0.5.dp)
                    .background(PactBorder)
            )
        }

        // Se não houver empresas do Strapi ainda (dados em falta)
        if (companies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Empresas a ser adicionadas brevemente",
                    fontSize = 13.sp,
                    color = PactMuted
                )
            }
        }
    }
}

// ─── Linha de empresa no sheet ───────────────────────────────────────────────

@Composable
private fun SheetCompanyRow(
    empresaData: EmpresaData,
    onClick: () -> Unit
) {
    val attr = empresaData.attributes

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Logo placeholder (substitui por AsyncImage com Coil quando disponível)
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = attr.nome.take(2).uppercase(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = PactGreen
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = attr.nome,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = PactText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = attr.category ?: "Sem categoria",
                fontSize = 12.sp,
                color = PactAccent,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Text("›", fontSize = 22.sp, color = PactMuted)
    }
}

// ─── Adapter legacy (mantém compatibilidade se ainda usado noutro sítio) ────

@Composable
fun CompanyCardFromApi(
    empresaData: EmpresaData,
    onClick: () -> Unit
) {
    val attr = empresaData.attributes
    val company = Company(
        id = empresaData.id,
        name = attr.nome,
        category = attr.category ?: "Sem categoria",
        city = attr.city ?: "",
        shortDescription = attr.descricao,
        fullDescription = attr.descricao,
        website = attr.url ?: "",
        tags = attr.tags ?: emptyList(),
        logoRes = null,
        gallery = emptyList()
    )
    com.example.epact.ui.components.CompanyCard(company = company, onClick = onClick)
}