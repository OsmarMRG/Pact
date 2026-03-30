package com.example.epact.ui.screens.companies

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epact.data.RetrofitClient
import com.example.epact.model.EmpresaData
import kotlinx.coroutines.launch

class CompanyViewModel : ViewModel() {
    // Esta será a nossa nova lista que vem do Strapi
    var companies = mutableStateOf<List<EmpresaData>>(emptyList())
    var isLoading = mutableStateOf(false)

    init {
        loadCompanies()
    }

    fun loadCompanies() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitClient.instance.getEmpresas()
                companies.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    // Aqui no futuro adicionaremos as funções para:
    // fun deleteCompany(id: Int) { ... }
    // fun updateCompany(id: Int, attributes: EmpresaAttributes) { ... }
}