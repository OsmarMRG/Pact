package com.example.epact.data


import com.example.epact.model.Company
import com.example.epact.model.EmpresaResponse
import retrofit2.http.GET

interface StrapiApiService {
    // Supondo que você tenha um Content Type chamado "companies" no Strapi
    @GET("api/empresa")
    suspend fun getEmpresa(): EmpresaResponse
}
