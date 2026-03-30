package com.example.epact.data

import com.example.epact.model.EmpresaResponse
import retrofit2.http.GET

interface StrapiApiService {
    @GET("api/empresa")
    suspend fun getEmpresas(): EmpresaResponse
}
