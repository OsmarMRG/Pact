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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Close
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
import com.example.epact.model.EmpresaData
import com.example.epact.ui.components.CategoryFilters
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
import coil.compose.AsyncImage
data class MapBuilding(
    val id: String,
    val label: String,
    val subtitle: String,
    val pinX: Float,
    val pinY: Float,
    val companyNames: List<String>
)

private val mapBuildings = listOf(
    MapBuilding("A0", "A", "Edifício A · Piso 0", 0.36f, 0.22f,
        listOf("Interprev", "foursolutions", "Qstaff")),
    MapBuilding("A1", "A", "Edifício A · Piso 1", 0.64f, 0.11f,
        listOf("NTT DATA", "Solvit", "PropWorx", "DigitalWorks", "IPParking", "SDAC", "Verde100Truques")),
    MapBuilding("B", "B", "Edifício B", 0.16f, 0.60f,
        listOf("BSO Consulting", "IG&H")),
    MapBuilding("C1", "C1", "Edifício C1", 0.44f, 0.70f,
        listOf("Peak&Peak", "Vidigal Silva & Carlos Silva", "N10GLED", "Empowered Startups", "Jerónimo Martins")),
    MapBuilding("C2", "C2", "Edifício C2", 0.60f, 0.57f,
        listOf("Fraunhofer Portugal", "IPParking")),
    MapBuilding("D", "D", "Edifício D", 0.75f, 0.63f,
        listOf("TE Connectivity")),
    MapBuilding("E", "E", "Edifício E", 0.44f, 0.87f,
        listOf("CEiiA", "KPMG", "Jerónimo Martins"))
)

@Composable
fun CompaniesScreen(
    viewModel: CompanyViewModel = viewModel(),
    categories: List<String>,
    onCompanyClick: (Int) -> Unit
) {
    val companies by viewModel.companies
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
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
                                .background(PactGreen)
                                .clickable { viewModel.loadCompanies() }
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text("Tentar novamente", color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }
            showMap -> MapaView(allCompanies = companies, onCompanyClick = onCompanyClick)
            else -> ListaView(companies = companies, categories = categories, onCompanyClick = onCompanyClick)
        }
    }
}

@Composable
private fun ViewToggle(showMap: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(false to "Lista", true to "Mapa").forEach { (isMap, label) ->
            val active = showMap == isMap
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (active) PactGreen else PactCard)
                    .border(0.5.dp, if (active) PactGreen else PactBorder, RoundedCornerShape(12.dp))
                    .clickable { onToggle(isMap) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (isMap) Icons.Default.Map else Icons.AutoMirrored.Filled.FormatListBulleted,
                        contentDescription = null,
                        tint = if (active) Color.White else PactMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                        color = if (active) Color.White else PactMuted)
                }
            }
        }
    }
}

@Composable
private fun ListaView(
    companies: List<EmpresaData>,
    categories: List<String>,
    onCompanyClick: (Int) -> Unit
) {
    var search by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("Todos") }

    val filtered = companies.filter { e ->
        val matchSearch = (e.nome ?: "").contains(search, ignoreCase = true) ||
                (e.descricao ?: "").contains(search, ignoreCase = true)
        val matchCat = selectedCategory == "Todos" ||
                // ✅ CORRETO
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
            CategoryFilters(categories = categories, selectedCategory = selectedCategory, onSelect = { selectedCategory = it })
            Spacer(Modifier.height(6.dp))
        }
        item {
            Text("${filtered.size} empresa${if (filtered.size != 1) "s" else ""}",
                fontSize = 11.sp, color = PactMuted, modifier = Modifier.padding(vertical = 6.dp))
        }
        items(filtered) { empresa ->
            DirectoryRow(empresa = empresa, onClick = { onCompanyClick(empresa.id) })
            Box(Modifier.fillMaxWidth().height(0.5.dp).background(PactBorder))
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun DirectoryRow(empresa: EmpresaData, onClick: () -> Unit) {
    val nome = empresa.nome ?: "Sem nome"
    val descricao = empresa.descricao ?: ""
    val category = empresa.category ?.name?: "Sem categoria"
    val city = empresa.city ?: ""

    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier.size(46.dp).clip(RoundedCornerShape(12.dp)).background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(nome.take(2).uppercase(), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PactGreen)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(nome, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = PactText,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text=category, fontSize = 12.sp, color = PactAccent, fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 2.dp))
            if (descricao.isNotBlank()) {
                Text(descricao, fontSize = 11.sp, color = PactMuted,
                    maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 2.dp))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapaView(allCompanies: List<EmpresaData>, onCompanyClick: (Int) -> Unit) {
    val scope = rememberCoroutineScope()
    var selectedBuilding by remember { mutableStateOf<MapBuilding?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 5f)
        val maxX = (imageSize.width * (scale - 1f)) / 2f
        val maxY = (imageSize.height * (scale - 1f)) / 2f
        offset = Offset(
            (offset.x + panChange.x).coerceIn(-maxX, maxX),
            (offset.y + panChange.y).coerceIn(-maxY, maxY)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .transformable(state = transformState)
                .graphicsLayer(scaleX = scale, scaleY = scale, translationX = offset.x, translationY = offset.y)
                .onSizeChanged { imageSize = it }
        ) {
            Image(
                painter = painterResource(id = R.drawable.mapa_pact),
                contentDescription = "Mapa PACT",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            if (imageSize.width > 0 && imageSize.height > 0) {
                mapBuildings.forEach { building ->
                    val pinX = (building.pinX * imageSize.width).roundToInt()
                    val pinY = (building.pinY * imageSize.height).roundToInt()
                    val count = building.companyNames.size
                    BuildingPin(building, count, pinX, pinY) {
                        selectedBuilding = building
                        scope.launch { sheetState.show() }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(PactCard.copy(alpha = 0.92f))
                .border(0.5.dp, PactBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            Text("Pinça para zoom · Toca num edifício", fontSize = 11.sp, color = PactMuted)
        }
    }

    selectedBuilding?.let { building ->
        val buildingCompanies = building.companyNames.mapNotNull { name ->
            allCompanies.firstOrNull { e ->
                (e.nome ?: "").contains(name, ignoreCase = true) ||
                        name.contains(e.nome ?: "", ignoreCase = true)
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
                onCompanyClick = { id ->
                    scope.launch { sheetState.hide() }
                    selectedBuilding = null
                    onCompanyClick(id)
                },
                onDismiss = {
                    scope.launch { sheetState.hide() }
                    selectedBuilding = null
                }
            )
        }
    }
}

@Composable
private fun BuildingPin(building: MapBuilding, count: Int, pinX: Int, pinY: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier.offset { IntOffset(pinX - 24, pinY - 48) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(PactAccent)
                .border(2.dp, Color.White, RoundedCornerShape(50.dp))
                .clickable { onClick() }
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(building.label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Box(Modifier.size(17.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                    Text("$count", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = PactAccent)
                }
            }
        }
        Box(Modifier.size(width = 8.dp, height = 5.dp).background(PactAccent))
    }
}

@Composable
private fun BuildingSheet(
    building: MapBuilding,
    companies: List<EmpresaData>,
    onCompanyClick: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(building.subtitle, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = PactText)
                Text("${companies.size} empresa${if (companies.size != 1) "s" else ""}",
                    fontSize = 12.sp, color = PactMuted, modifier = Modifier.padding(top = 2.dp))
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Fechar", tint = PactMuted)
            }
        }
        Box(Modifier.fillMaxWidth().height(0.5.dp).background(PactBorder))

        if (companies.isEmpty()) {
            Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Text("Empresas a ser adicionadas brevemente", fontSize = 13.sp, color = PactMuted)
            }
        } else {
            companies.forEach { empresa ->
                SheetRow(empresa = empresa, onClick = { onCompanyClick(empresa.id) })
                Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(0.5.dp).background(PactBorder))
            }
        }
    }
}

@Composable
private fun SheetRow(empresa: EmpresaData, onClick: () -> Unit) {
    val nome = empresa.nome ?: "Sem nome"
    val category = empresa.category ?.name?: "Sem categoria"

    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 20.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(Modifier.size(44.dp).clip(RoundedCornerShape(11.dp)).background(Color.White), contentAlignment = Alignment.Center) {
            Text(nome.take(2).uppercase(), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PactGreen)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(nome, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = PactText,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(category, fontSize = 12.sp, color = PactAccent, fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 2.dp))
        }
        Text("›", fontSize = 22.sp, color = PactMuted)
    }
}