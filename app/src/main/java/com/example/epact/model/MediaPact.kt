package com.example.epact.model


data class MediaPactResponse(
    val data: List<MediaPactData>? = emptyList()
)

// Item de media (foto ou vídeo)
data class MediaPactData(
    val id: Int,
    val titulo: String?,
    val legenda: String?,
    val tipo: String?,        // "foto" ou "video"
    val video: String?,
    val destaque: Boolean?,
    val ordem: Int?,
    val imagem: StrapiMediaData? = null  // reutiliza a classe já existente
)