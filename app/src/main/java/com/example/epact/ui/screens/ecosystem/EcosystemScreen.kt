package com.example.epact.ui.screens.ecosystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.epact.ui.components.HighlightCard
import com.example.epact.ui.components.HighlightRow
import com.example.epact.ui.components.InfoBlock
import com.example.epact.ui.components.SectionTitle
import com.example.epact.ui.theme.PactBlack

@Composable
fun EcosystemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PactBlack)
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
        SectionTitle("Soluções")
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
