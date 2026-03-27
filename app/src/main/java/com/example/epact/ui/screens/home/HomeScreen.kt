package com.example.epact.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.epact.model.Metric
import com.example.epact.ui.components.HeroCard
import com.example.epact.ui.components.InfoBlock
import com.example.epact.ui.components.MetricCard
import com.example.epact.ui.components.SectionTitle
import com.example.epact.ui.theme.PactBlack
import com.example.epact.ui.theme.PactText

@Composable
fun HomeScreen(
    metrics: List<Metric>,
    onCompaniesClick: () -> Unit,
    onMapClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .background(PactBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { HeroCard() }

        item { SectionTitle("Indicadores") }

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
                text = "//"
            )
        }

        item {
            InfoBlock(
                title = "Informações",
                text = "//"
            )
        }

//        item {
//            Card(
//                colors = CardDefaults.cardColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface)
//            ) {
//                Column(modifier = Modifier.padding(18.dp)) {
//                    Text(
//                        text = "Explorar rapidamente",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = PactText
//                    )
//                    Spacer(modifier = Modifier.height(12.dp))
//                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
//                        Button(onClick = onCompaniesClick) {
//                            Text("Ver empresas")
//                        }
//                        TextButton(onClick = onMapClick) {
//                            Text("Abrir mapa")
//                        }
//                    }
//                }
//            }
//        }
    }
}
