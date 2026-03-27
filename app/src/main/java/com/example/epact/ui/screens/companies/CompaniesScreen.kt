package com.example.epact.ui.screens.companies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.epact.model.Company
import com.example.epact.ui.components.CategoryFilters
import com.example.epact.ui.components.CompanyCard
import com.example.epact.ui.components.SectionTitle
import com.example.epact.ui.theme.PactBlack
import com.example.epact.ui.theme.PactMuted

@Composable
fun CompaniesScreen(
    companies: List<Company>,
    categories: List<String>,
    onCompanyClick: (Int) -> Unit
) {
    var search by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("Todos") }

    val filteredCompanies = companies.filter { company ->
        val matchesSearch = company.name.contains(search, ignoreCase = true) ||
            company.category.contains(search, ignoreCase = true) ||
            company.city.contains(search, ignoreCase = true)

        val matchesCategory = selectedCategory == "Todos" || company.category == selectedCategory
        matchesSearch && matchesCategory
    }

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

        items(filteredCompanies) { company ->
            CompanyCard(company = company) {
                onCompanyClick(company.id)
            }
        }
    }
}
