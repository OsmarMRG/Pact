package com.example.epact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().userAgentValue = packageName
        setContent {
            EpactApp()
        }
    }
}

data class BottomItem(
    val label: String,
    val route: String
)

data class Metric(
    val title: String,
    val value: String,
    val icon: String
)

data class Company(
    val id: Int,
    val name: String,
    val category: String,
    val city: String,
    val shortDescription: String,
    val fullDescription: String,
    val tags: List<String>
)

private val pactGreen = Color(0xFF0A5C4A)
private val pactGreenSoft = Color(0xFFE9F5F1)
private val pactGreenDark = Color(0xFF063E32)
private val pactAccent = Color(0xFF23A26D)
private val pactText = Color(0xFF1F2937)
private val pactMuted = Color(0xFF6B7280)
private val pactBg = Color(0xFFF7FAF9)

private val metrics = listOf(
    Metric("Empresas", "55", "business"),
    Metric("Postos de trabalho", "540", "groups"),
    Metric("Edifícios", "6", "apartment"),
    Metric("Projetos liderados", "5", "trend")
)

private val companies = listOf(
    Company(
        id = 1,
        name = "AgroSense Labs",
        category = "Agrotech",
        city = "Évora",
        shortDescription = "Soluções digitais para agricultura inteligente e monitorização de campo.",
        fullDescription = "Empresa exemplo para demo. Pode representar uma startup ou empresa incubada ligada à inovação agrícola, sensores, dados e apoio à produção regional.",
        tags = listOf("Sensores", "Dados", "Agricultura")
    ),
    Company(
        id = 2,
        name = "Blue Alentejo Tech",
        category = "Economia Azul",
        city = "Sines",
        shortDescription = "Projetos ligados a recursos hídricos, monitorização e tecnologia azul.",
        fullDescription = "Empresa exemplo pensada para mostrar uma vertente mais ligada à economia azul e à ligação entre inovação, território e sustentabilidade.",
        tags = listOf("Água", "Mar", "Sustentabilidade")
    ),
    Company(
        id = 3,
        name = "HealthNode",
        category = "Saúde Digital",
        city = "Beja",
        shortDescription = "Ferramentas digitais para apoio clínico, dados e serviços de proximidade.",
        fullDescription = "Empresa exemplo para representar a área da saúde digital. Na versão real podes mostrar logótipo, parceiros, contactos e projetos.",
        tags = listOf("Saúde", "Software", "Dados")
    ),
    Company(
        id = 4,
        name = "EnerVolt Systems",
        category = "Energia",
        city = "Portalegre",
        shortDescription = "Soluções para eficiência energética e sistemas inteligentes.",
        fullDescription = "Empresa exemplo com foco em energia e transição sustentável. Serve para dar variedade e mostrar setores diferentes do ecossistema.",
        tags = listOf("Energia", "Eficiência", "IoT")
    ),
    Company(
        id = 5,
        name = "Rural Data Studio",
        category = "Transformação Digital",
        city = "Évora",
        shortDescription = "Plataformas e dashboards para negócios e organizações do território.",
        fullDescription = "Empresa exemplo orientada para visualização de dados, apoio à decisão e digitalização de processos em contexto regional.",
        tags = listOf("Dashboards", "Cloud", "Gestão")
    ),
    Company(
        id = 6,
        name = "Mobility Forge",
        category = "Mobilidade",
        city = "Elvas",
        shortDescription = "Soluções para mobilidade inteligente e logística conectada.",
        fullDescription = "Empresa exemplo para demonstrar variedade de áreas e mostrar que o ecossistema pode ser explorado por tema, cidade ou tipo de solução.",
        tags = listOf("Mobilidade", "Logística", "Smart")
    )
)

private val categories = listOf(
    "Todos",
    "Agrotech",
    "Economia Azul",
    "Saúde Digital",
    "Energia",
    "Transformação Digital",
    "Mobilidade"
)

@Composable
fun EpactApp() {
    val navController = rememberNavController()

    MaterialTheme {
        Surface(color = pactBg) {
            MainScaffold(navController = navController)
        }
    }
}

@Composable
fun MainScaffold(navController: NavHostController) {
    val bottomItems = listOf(
        BottomItem("Início", "home"),
        BottomItem("Ecossistema", "ecosystem"),
        BottomItem("Empresas", "companies"),
        BottomItem("Mapa", "map"),
        BottomItem("Media", "media")
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = currentRoute != "company/{companyId}"

    Scaffold(
        topBar = {
            TopBar(
                currentRoute = currentRoute,
                onBack = { navController.popBackStack() }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = Color.White) {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                when (item.route) {
                                    "home" -> Icon(Icons.Default.Home, contentDescription = item.label)
                                    "ecosystem" -> Icon(Icons.Default.Public, contentDescription = item.label)
                                    "companies" -> Icon(Icons.Default.Business, contentDescription = item.label)
                                    "map" -> Icon(Icons.Default.LocationOn, contentDescription = item.label)
                                    else -> Icon(Icons.Default.PhotoLibrary, contentDescription = item.label)
                                }
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("ecosystem") { EcosystemScreen() }
            composable("companies") { CompaniesScreen(navController) }
            composable("company/{companyId}") { backStackEntry ->
                val companyId = backStackEntry.arguments?.getString("companyId")?.toIntOrNull()
                val company = companies.firstOrNull { it.id == companyId }
                if (company != null) {
                    CompanyDetailScreen(company = company)
                }
            }
            composable("map") { MapScreen() }
            composable("media") { MediaScreen() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(currentRoute: String?, onBack: () -> Unit) {
    val title = when (currentRoute) {
        "home" -> "EPACT"
        "ecosystem" -> "Ecossistema"
        "companies" -> "Empresas"
        "map" -> "Mapa"
        "media" -> "Media"
        "company/{companyId}" -> "Detalhe"
        else -> "EPACT"
    }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            if (currentRoute == "company/{companyId}") {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }
            }
        }
    )
}

@Composable
fun HomeScreen(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(pactBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            HeroCard()
        }

        item {
            SectionTitle("Indicadores")
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(metrics) { metric ->
                    MetricCard(metric = metric)
                }
            }
        }

        item {
            InfoBlock(
                title = "Objetivo da aplicação",
                text = "Mostrar o ecossistema do PACT de forma visual, clara e fácil de explorar. Esta base já está preparada para crescer com dados reais, logos, vídeos e integração futura com API."
            )
        }

        item {
            InfoBlock(
                title = "O que já consegues demonstrar",
                text = "Apresentação do espaço, métricas, visão do ecossistema, lista de empresas, detalhe por empresa, localização e zona multimédia."
            )
        }

        item {
            ElevatedCard(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Explorar rapidamente",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = pactText
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = { navController.navigate("companies") }) {
                            Text("Ver empresas")
                        }
                        TextButton(onClick = { navController.navigate("map") }) {
                            Text("Abrir mapa")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeroCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(pactGreen, pactAccent)
                )
            )
            .padding(22.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Ecossistema PACT",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Empresas, inovação, espaço e impacto regional numa experiência mobile pronta para demo.",
                    color = Color.White,
                    lineHeight = 22.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MiniBadge("Visual")
                MiniBadge("Mapa")
                MiniBadge("Empresas")
                MiniBadge("Media")
            }
        }
    }
}

@Composable
fun MiniBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White.copy(alpha = 0.18f))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = Color.White, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun MetricCard(metric: Metric) {
    Card(
        modifier = Modifier.width(170.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(pactGreenSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (metric.icon) {
                        "business" -> Icons.Default.Business
                        "groups" -> Icons.Default.Groups
                        "apartment" -> Icons.Default.Apartment
                        else -> Icons.Default.TrendingUp
                    },
                    contentDescription = metric.title,
                    tint = pactGreen
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(metric.value, fontWeight = FontWeight.Bold, fontSize = 28.sp, color = pactText)
            Text(metric.title, color = pactMuted)
        }
    }
}

@Composable
fun EcosystemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(pactBg)
            .padding(16.dp)
    ) {
        SectionTitle("Sobre o ecossistema")
        Spacer(modifier = Modifier.height(12.dp))

        InfoBlock(
            title = "O que é o ePACT",
            text = "Uma ligação entre o PACT, entidades da região e o tecido empresarial e institucional, focada em inovação, conhecimento e valorização económica e social."
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoBlock(
            title = "Objetivo",
            text = "Promover a transferência de conhecimento entre centros de saber e a sociedade, apoiando crescimento económico sustentável e propostas de valor mais forte para a região."
        )

        Spacer(modifier = Modifier.height(16.dp))
        SectionTitle("Quem pode fazer parte")
        Spacer(modifier = Modifier.height(12.dp))
        HighlightRow("Startups regionais")
        HighlightRow("Empresas inovadoras")
        HighlightRow("Empresas de tecnologia")
        HighlightRow("Empresários")

        Spacer(modifier = Modifier.height(16.dp))
        SectionTitle("Soluções a mostrar na app")
        Spacer(modifier = Modifier.height(12.dp))
        HighlightCard("Premium", "Acesso a vantagens do PACT, serviços comuns e utilização de espaços.")
        Spacer(modifier = Modifier.height(10.dp))
        HighlightCard("InPACT", "Incubação física com espaço dedicado e acesso a infraestruturas.")
        Spacer(modifier = Modifier.height(10.dp))
        HighlightCard("OnPACT", "Incubadora digital para presença mais flexível e acompanhamento remoto.")
        Spacer(modifier = Modifier.height(10.dp))
        HighlightCard("Cowork", "Zona de trabalho pronta para equipas, nómadas digitais e colaboração.")
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompaniesScreen(navController: NavHostController) {
    var search by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("Todos") }

    val filteredCompanies = companies.filter { company ->
        val matchesSearch = company.name.contains(search, ignoreCase = true) ||
                company.category.contains(search, ignoreCase = true) ||
                company.city.contains(search, ignoreCase = true)

        val matchesCategory = selectedCategory == "Todos" || company.category == selectedCategory
        matchesSearch && matchesCategory
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(pactBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionTitle("Empresas do ecossistema")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Lista demo com pesquisa e filtro. Trocas depois pelos dados reais sem partir o visual.",
                color = pactMuted
            )
        }

        item {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Pesquisar empresa, categoria ou cidade") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Pesquisar")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = RoundedCornerShape(20.dp)
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) }
                    )
                }
            }
        }

        items(filteredCompanies) { company ->
            CompanyCard(company = company) {
                navController.navigate("company/${company.id}")
            }
        }
    }
}

@Composable
fun CompanyCard(company: Company, onClick: () -> Unit) {
    ElevatedCard(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(company.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = pactText)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(company.category, color = pactGreen, fontWeight = FontWeight.Medium)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(pactGreenSoft)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(company.city, color = pactGreenDark, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(company.shortDescription, color = pactMuted, lineHeight = 21.sp)
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(company.tags) { tag ->
                    AssistChip(onClick = { }, label = { Text(tag) })
                }
            }
        }
    }
}

@Composable
fun CompanyDetailScreen(company: Company) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(pactBg)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(pactGreenDark, pactGreen)
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(company.name, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(company.category, color = Color.White.copy(alpha = 0.88f))
                }
                MiniBadge(company.city)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        InfoBlock(
            title = "Descrição",
            text = company.fullDescription
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoBlock(
            title = "Aplicação futura",
            text = "Nesta página podes depois ligar logótipo, contactos, website, projetos, equipa, galerias e localização da empresa no ecossistema."
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tags", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = pactText)
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(company.tags) { tag ->
                        AssistChip(onClick = { }, label = { Text(tag) })
                    }
                }
            }
        }
    }
}

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val pactAddress = "Herdade da Barba Rala, Rua Luís Adelino Fonseca, Lote 1A, 7005-345 Évora"
    val pactPoint = GeoPoint(38.5488092, -7.9111854)
    val mapView = rememberMapViewWithLifecycle(pactPoint, pactAddress)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(pactBg)
            .padding(16.dp)
    ) {
        SectionTitle("Localização do espaço")
        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(28.dp))
                .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(28.dp))
        ) {
            AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize(),
                update = {
                    it.controller.setZoom(16.0)
                    it.controller.setCenter(pactPoint)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        InfoBlock(
            title = "Morada",
            text = pactAddress
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${Uri.encode(pactAddress)}"))
                    context.startActivity(intent)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Abrir Maps")
            }

            TextButton(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(pactAddress)}")
                    )
                    context.startActivity(intent)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Trajeto")
            }
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(point: GeoPoint, title: String): MapView {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(16.0)
            controller.setCenter(point)

            val marker = Marker(this)
            marker.position = point
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = title
            overlays.add(marker)
        }
    }

    DisposableEffect(lifecycle, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDetach()
                else -> Unit
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    return mapView
}

@Composable
fun MediaScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(pactBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionTitle("Galeria e vídeos")
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Zona preparada para receber imagens reais do espaço, drone shots e vídeos institucionais.",
                color = pactMuted
            )
        }

        item {
            MediaCard(
                title = "Drone do espaço",
                subtitle = "Placeholder para vídeo aéreo ou reel de apresentação",
                badge = "Vídeo"
            )
        }

        item {
            MediaCard(
                title = "Galeria do campus",
                subtitle = "Placeholder para imagens do edifício, auditório, cowork e zonas comuns",
                badge = "Imagem"
            )
        }

        item {
            MediaCard(
                title = "Empresas e eventos",
                subtitle = "Placeholder para conteúdos de networking, open day, formações e reuniões",
                badge = "Galeria"
            )
        }

        item {
            InfoBlock(
                title = "Como ligar media real",
                text = "Podes depois usar imagens guardadas em drawable, URLs remotas, vídeos locais em raw ou vídeos do YouTube com player integrado."
            )
        }
    }
}

@Composable
fun MediaCard(title: String, subtitle: String, badge: String) {
    ElevatedCard(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = title,
                        tint = pactGreen,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = badge,
                        color = pactGreenDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = pactText)
            Spacer(modifier = Modifier.height(6.dp))
            Text(subtitle, color = pactMuted, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = pactText
    )
}

@Composable
fun InfoBlock(title: String, text: String) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = pactText)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text, color = pactMuted, lineHeight = 22.sp)
        }
    }
}

@Composable
fun HighlightRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(pactAccent)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, color = pactText, fontWeight = FontWeight.Medium)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun HighlightCard(title: String, text: String) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = pactGreenSoft)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = pactGreenDark)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text, color = pactText)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EpactPreview() {
    EpactApp()
}