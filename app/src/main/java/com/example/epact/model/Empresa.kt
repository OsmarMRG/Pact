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
    // Devem ter EXATAMENTE o mesmo nome que está no Strapi!

    val nome: String,

    // Na imagem está 'descricao' (sem til)
    val descricao: String,

    // 'url' é um campo de texto
    val url: String,

    // 'ativo' é Boolean (verdadeiro/falso)
    val ativo: Boolean,

    // --- Tratamento de Media (Campos Especiais) ---

    // 'logo' é um campo de Media única. Precisamos de uma classe para ele.
    // Usamos o GSON para ignorar se vier nulo do Strapi (se a empresa não tiver logo)
    val logo: StrapiMediaSingleResponse?,

    // 'Galeria' (com G maiúsculo) é Media múltipla.
    val Galeria: StrapiMediaMultipleResponse?
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