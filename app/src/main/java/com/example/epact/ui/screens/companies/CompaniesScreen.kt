package com.example.epact.ui.screens.companies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.epact.model.Company
import com.example.epact.model.EmpresaData
import com.example.epact.ui.components.CategoryFilters
import com.example.epact.ui.components.CompanyCard
import com.example.epact.ui.components.SectionTitle
import com.example.epact.ui.theme.PactBlack
import com.example.epact.ui.theme.PactMuted

@Composable
fun CompaniesScreen(
    viewModel: CompanyViewModel = viewModel(),
    categories: List<String>,
    onCompanyClick: (Int) -> Unit
) {
    val companies by viewModel.companies
    val isLoading by viewModel.isLoading
    var search by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("Todos") }

    val filteredCompanies = companies.filter { empresaData ->
        val attr = empresaData.attributes
        val matchesSearch = attr.nome.contains(search, ignoreCase = true) ||
                attr.descricao.contains(search, ignoreCase = true)

        val matchesCategory = selectedCategory == "Todos" ||
                attr.category?.equals(selectedCategory, ignoreCase = true) == true
        matchesSearch && matchesCategory
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(PactBlack)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionTitle("Empresas do ecossistema")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Lista com pesquisa e filtro.",
                    color = PactMuted
                )
            }

            item {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Pesquisar empresa, categoria ou cidade") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Pesquisar")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }

            item {
                CategoryFilters(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onSelect = { selectedCategory = it }
                )
            }

            items(filteredCompanies) { empresaData ->
                CompanyCardFromApi(
                    empresaData = empresaData,
                    onClick = { onCompanyClick(empresaData.id) }
                )
            }
        }
    }
}

@Composable
fun CompanyCardFromApi(
    empresaData: EmpresaData,
    onClick: () -> Unit
) {
    val attr = empresaData.attributes

    val company = Company(
        id = empresaData.id,
        name = attr.nome,
        category = attr.category ?: "Sem categoria",
        city = attr.city ?: "",
        shortDescription = attr.descricao,
        fullDescription = attr.descricao,
        website = attr.url ?: "",
        tags = attr.tags ?: emptyList(),
        logoRes = null,
        gallery = emptyList()
    )

    CompanyCard(company = company, onClick = onClick)
}
