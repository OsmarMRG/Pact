package com.example.epact.model

// 1. O envelope principal
data class EmpresaResponse(
    val data: List<EmpresaData>? = emptyList()
)

// 2. A Empresa (No Strapi 5 os campos vêm aqui DIRETO)
data class EmpresaData(
    val id: Int,
    val documentId: String?,
    val nome: String?,
    val descricao: String?,
    val url: String?,
    val ativo: Boolean?,

    // Para as imagens no Strapi 5, elas também perderam o 'attributes'
    val logo: StrapiMediaData? = null,
    val Galeria: List<StrapiMediaData>? = emptyList()
)

// 3. Estrutura de Media simplificada para Strapi 5
data class StrapiMediaData(
    val id: Int,
    val documentId: String?,
    val url: String?, // O link da imagem (Ex: https://res.cloudinary.com/...)
    val name: String?,
    val mime: String?
)