package com.example.epact.data

import com.example.epact.model.EdificioResponse
import com.example.epact.model.EmpresaResponse
import com.example.epact.model.MediaPactResponse
import retrofit2.http.GET

interface StrapiApiService {

    @GET("api/empresas?populate=*")
    suspend fun getEmpresas(): EmpresaResponse

    @GET("api/media-pacts?populate=*&sort=ordem:asc")
    suspend fun getMediaPact(): MediaPactResponse

    @GET("api/edificios?populate=fotos")
    suspend fun getEdificios(): EdificioResponse
}