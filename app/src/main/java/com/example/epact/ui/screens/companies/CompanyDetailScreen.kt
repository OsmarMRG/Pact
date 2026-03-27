package com.example.epact.ui.screens.companies

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.epact.model.Company
import com.example.epact.ui.components.InfoBlock
import com.example.epact.ui.components.MiniBadge
import com.example.epact.ui.theme.PactBlack
import com.example.epact.ui.theme.PactCard
import com.example.epact.ui.theme.PactGreen
import com.example.epact.ui.theme.PactMuted
import com.example.epact.ui.theme.PactText

@Composable
fun CompanyDetailScreen(company: Company) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PactBlack)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(PactGreen, PactBlack)
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    if (company.logoRes != null) {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = PactCard)
                        ) {
                            Image(
                                painter = painterResource(id = company.logoRes),
                                contentDescription = company.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(82.dp)
                                    .padding(12.dp),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Text(
                        text = company.name,
                        color = PactText,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = company.category,
                        color = PactMuted,
                        fontSize = 16.sp
                    )
                }

                if (company.city.isNotBlank()) {
                    MiniBadge(company.city)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        InfoBlock(
            title = "Descrição",
            text = company.fullDescription
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (!company.website.isNullOrBlank()) {
            InfoBlock(
                title = "Site oficial",
                text = company.website
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(company.website))
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Visitar site")
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = PactCard)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Resumo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = PactText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = company.shortDescription,
                    color = PactMuted
                )
            }
        }

        if (company.tags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PactCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tags",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = PactText
                    )
                    Spacer(modifier = Modifier.height(10.dp))
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
}