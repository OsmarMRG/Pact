package com.example.epact.model

data class BottomItem(
    val label: String,
    val route: String
)

data class Metric(
    val title: String,
    val value: String,
    val icon: String
)

// Representa uma imagem da galeria de uma empresa.
// Quando tiveres fotos reais:
//   1. Adiciona as imagens em res/drawable/
//   2. Descomenta `imageRes: Int`
//   3. Remove `placeholderLabel` (ou mantém como legenda)
data class CompanyImage(
    val id: Int,
    val caption: String,
    val placeholderLabel: String,   // remove quando tiveres imagens reais
    // val imageRes: Int,           // descomenta quando tiveres imagens reais
)

data class Company(
    val id: Int,
    val name: String,
    val category: String,
    val city: String,
    val shortDescription: String,
    val fullDescription: String,
    val tags: List<String>,
    val website: String,
    val logoRes: Int? = null,
    val gallery: List<CompanyImage> = emptyList()   // galeria específica da empresa
)