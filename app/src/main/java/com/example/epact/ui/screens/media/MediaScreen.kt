package com.example.epact.ui.screens.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.epact.ui.components.InfoBlock
import com.example.epact.ui.components.SectionTitle
import com.example.epact.ui.theme.PactBlack
import com.example.epact.ui.theme.PactCard
import com.example.epact.ui.theme.PactGreen
import com.example.epact.ui.theme.PactMuted
import com.example.epact.ui.theme.PactSurfaceAlt
import com.example.epact.ui.theme.PactText
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun MediaScreen() {
    LazyColumn(
        modifier = Modifier
            .background(PactBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionTitle("Galeria e vídeos")
            Spacer(modifier = Modifier.height(6.dp))

        }

        item {
            MediaCard(
                title = "Drone do espaço",
                subtitle = "para vídeo aéreo ou reel de apresentação",
                badge = "Vídeo"
            )
        }

        item {
            MediaCard(
                title = "Fotos do PACT",
                subtitle = "imagens do edifício, auditório, cowork e zonas comuns",
                badge = "Imagem"
            )
        }

        item {
            MediaCard(
                title = "Empresas e eventos",
                subtitle = "conteúdos de networking, open day, formações e reuniões",
                badge = "Galeria"
            )
        }


    }
}

@Composable
private fun MediaCard(title: String, subtitle: String, badge: String) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = PactCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PactSurfaceAlt)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 42.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = title,
                        tint = PactGreen,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = badge,
                        color = PactText,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PactText)
            Spacer(modifier = Modifier.height(6.dp))
            Text(subtitle, color = PactMuted, textAlign = TextAlign.Center)
        }
    }
}
