package com.example.epact.model

import com.google.gson.annotations.SerializedName

data class EdificioResponse(
    val data: List<EdificioData>? = emptyList()
)

data class EdificioData(
    val id: Int,
    val nome: String?,       // "Edifício A"
    val codigo: String?,     // "A", "B", "C1", "C2", "D", "E"
    val descricao: String?,
    @SerializedName("fotos")
    val fotos: List<StrapiMediaData>? = emptyList()
)