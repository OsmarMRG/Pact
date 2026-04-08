package com.example.epact.model

import com.google.gson.annotations.SerializedName

// 1. O envelope principal
data class EmpresaResponse(
    val data: List<EmpresaData>? = emptyList()
)

// 2. A Empresa corrigida para Strapi 5
data class EmpresaData(
    val id: Int,
    val documentId: String?,
    val nome: String?,
    val descricao: String?,
    val url: String?,
    val ativo: Boolean?,
    val city: String?,
    val category: CategoryData? = null,
    @SerializedName("logoRes")
    val logoRes: StrapiMediaData? = null,

    @SerializedName("Galeria")
    val Galeria: List<StrapiMediaData>? = emptyList()
)

// NOVO: Classe para a Categoria (para o nome aparecer na lista)
data class CategoryData(
    val id: Int,
    val name: String?
)

// 3. Estrutura de Media corrigida
data class StrapiMediaData(
    val id: Int,
    val documentId: String?,
    val url: String?, // O link real da Cloudinary/AWS
    val name: String?,
    val mime: String?
)