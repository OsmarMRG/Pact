package com.example.epact.data

import com.example.epact.model.EmpresaResponse
import retrofit2.http.GET

interface StrapiApiService {
    // O ?populate=* é o que faz as imagens (logoRes e Galeria) aparecerem!
    @GET("api/empresas?populate=*")
    suspend fun getEmpresas(): EmpresaResponse
}