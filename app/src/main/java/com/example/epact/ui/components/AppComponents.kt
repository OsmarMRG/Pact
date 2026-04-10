package com.example.epact.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.epact.model.BottomItem
import com.example.epact.model.Company
import com.example.epact.model.Metric
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
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = PactText
    )
}

@Composable
fun InfoBlock(title: String, text: String) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = PactCard)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PactText)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text, color = PactMuted, lineHeight = 22.sp)
        }
    }
}

@Composable
fun HighlightCard(title: String, text: String) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = PactSurfaceAlt)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = PactAccent)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text, color = PactText)
        }
    }
}

@Composable
fun HighlightRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(PactCard)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(PactAccent)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, color = PactText, fontWeight = FontWeight.Medium)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun MiniBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = PactText, fontWeight = FontWeight.Medium)
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
                    colors = listOf(PactGreen, PactBlack)
                )
            )
            .padding(22.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Um novo Alentejo",
                    color = PactAccent,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Tecnológico e inovador",
                    color = PactText,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Uma app para mostrar o ecossistema, o espaço e as empresas ligadas ao PACT.",
                    color = PactMuted,
                    lineHeight = 22.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MiniBadge("Ecossistema")
                MiniBadge("Empresas")
                MiniBadge("Mapa")
            }
        }
    }
}

@Composable
fun MetricCard(metric: Metric) {
    Card(
        modifier = Modifier.width(170.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = PactCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(PactGreenSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (metric.icon) {
                        "business" -> Icons.Default.Business
                        "groups" -> Icons.Default.Groups
                        "apartment" -> Icons.Default.Apartment
                        else -> Icons.AutoMirrored.Filled.TrendingUp
                    },
                    contentDescription = metric.title,
                    tint = PactAccent
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(metric.value, fontWeight = FontWeight.Bold, fontSize = 28.sp, color = PactText)
            Text(metric.title, color = PactMuted)
        }
    }
}

@Composable
fun CompanyCard(company: Company, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = PactCard),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            if (company.logoRes != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(PactSurfaceAlt)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.Image(
                        painter = androidx.compose.ui.res.painterResource(id = company.logoRes),
                        contentDescription = company.name,
                        modifier = Modifier.height(52.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        company.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = PactText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        company.category,
                        color = PactAccent,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (company.city.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(PactSurfaceAlt)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            company.city,
                            color = PactText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                company.shortDescription,
                color = PactMuted,
                lineHeight = 21.sp
            )

            if (!company.website.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = company.website,
                    color = PactAccent,
                    fontSize = 13.sp,
                    maxLines = 1
                )
            }

            if (company.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(company.tags) { tag ->
                        AssistChip(
                            onClick = { },
                            label = { Text(tag) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(currentRoute: String?, onBack: () -> Unit) {
    // Esconder a top bar no ecrã inicial — o Hero já serve de cabeçalho
    if (currentRoute == "home") return

    val title = when {
        currentRoute == null -> "EPACT"
        currentRoute.startsWith("company/") -> "Detalhe"
        currentRoute == "welcome"   -> "EPACT"
        currentRoute == "companies" -> "Empresas"
        currentRoute == "map"       -> "Mapa"
        currentRoute == "media"     -> "Media"
        else -> "EPACT"
    }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = PactBlack,
            titleContentColor = PactText,
            navigationIconContentColor = PactText
        ),
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        },
        navigationIcon = {
            if (currentRoute?.startsWith("company/") == true) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
            }
        }
    )
}

@Composable
fun AppBottomBar(
    items: List<BottomItem>,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    iconForRoute: @Composable (String, String) -> Unit
) {
    NavigationBar(containerColor = PactSurfaceAlt) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { iconForRoute(item.route, item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

@Composable
fun CategoryFilters(
    categories: List<String>,
    selectedCategory: String,
    onSelect: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onSelect(category) },
                label = { Text(category) }
            )
        }
    }
}
