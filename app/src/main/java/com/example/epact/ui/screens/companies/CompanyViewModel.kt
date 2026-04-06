package com.example.epact.ui.screens.companies

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epact.data.RetrofitClient
import com.example.epact.model.EmpresaData
import kotlinx.coroutines.launch

class CompanyViewModel : ViewModel() {
    var companies = mutableStateOf<List<EmpresaData>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    init {
        loadCompanies()
    }

    fun loadCompanies() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val response = RetrofitClient.instance.getEmpresas()
                companies.value = response.data ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = "Erro ao carregar empresas"
            } finally {
                isLoading.value = false
            }
        }
    }
}