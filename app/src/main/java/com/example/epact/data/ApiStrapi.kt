package com.example.epact.data

import com.example.epact.model.EmpresaResponse
import retrofit2.http.GET
import retrofit2.Response

interface StrapiApiService {
    @GET("api/empresas")
    suspend fun getEmpresas(): EmpresaResponse
}
