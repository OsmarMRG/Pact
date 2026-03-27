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

data class Company(
    val id: Int,
    val name: String,
    val category: String,
    val city: String,
    val shortDescription: String,
    val fullDescription: String,
    val tags: List<String>,
    val website: String,
    val logoRes: Int? = null
)
