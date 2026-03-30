package com.example.epact.model

// 1. A resposta principal da API (o JSON exterior que contém 'data')
data class EmpresaResponse(
    val data: List<EmpresaData>
)

// 2. O objeto que contém o ID e os atributos
data class EmpresaData(
    val id: Int,
    val attributes: EmpresaAttributes
)

// 3. Os atributos reais da tua Empresa (mapeados a partir da tua imagem do Strapi)
data class EmpresaAttributes(
    val nome: String,
    val descricao: String,
    val url: String? = null,
    val ativo: Boolean = true,
    val category: String? = null,
    val city: String? = null,
    val tags: List<String>? = null,
    val logo: StrapiMediaSingleResponse? = null,
    val Galeria: StrapiMediaMultipleResponse? = null
)

// --- Estrutura de Media do Strapi (para v4/v5) ---
// Precisamos disto para conseguir ler o 'logo' e a 'Galeria'

data class StrapiMediaSingleResponse(
    val data: StrapiMediaData?
)

data class StrapiMediaMultipleResponse(
    val data: List<StrapiMediaData>?
)

data class StrapiMediaData(
    val id: Int,
    val attributes: StrapiMediaAttributes
)

data class StrapiMediaAttributes(
    val name: String,
    val url: String, // O URL da imagem (relativo, ex: "/uploads/imagem.png")
    val mime: String // Ex: "image/png"
    // Adiciona outros campos se precisares (como 'size', 'width', 'height')
)