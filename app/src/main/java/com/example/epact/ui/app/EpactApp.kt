package com.example.epact.ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.epact.data.AppData
import com.example.epact.model.BottomItem
import com.example.epact.navigation.AppDestinations
import com.example.epact.ui.components.AppBottomBar
import com.example.epact.ui.components.AppTopBar
import com.example.epact.ui.screens.companies.CompaniesScreen
import com.example.epact.ui.screens.companies.CompanyDetailScreen
import com.example.epact.ui.screens.companies.EdificioDetailScreen
import com.example.epact.ui.screens.map.MapScreen
import com.example.epact.ui.screens.media.MediaScreen
import com.example.epact.ui.screens.welcome.WelcomeScreen
import com.example.epact.ui.theme.PactBlack

@Composable
fun EpactApp() {
    val navController = rememberNavController()
    val currentRoute  = navController.currentBackStackEntryAsState().value?.destination?.route

    val bottomItems = listOf(
        BottomItem("Início",   AppDestinations.Welcome),
        BottomItem("Empresas", AppDestinations.Companies),
        BottomItem("Mapa",     AppDestinations.Map),
        BottomItem("Media",    AppDestinations.Media)
    )

    // Esconde a bottom bar nos ecrãs de detalhe
    val showBottomBar = currentRoute?.startsWith("company/") != true &&
            currentRoute?.startsWith("edificio/") != true

    Scaffold(
        modifier = Modifier.background(PactBlack),
        topBar = {
            AppTopBar(
                currentRoute = currentRoute,
                onBack = { navController.popBackStack() }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(
                    items = bottomItems,
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    iconForRoute = { route, label ->
                        when (route) {
                            AppDestinations.Welcome   -> Icon(Icons.Default.Home,         contentDescription = label)
                            AppDestinations.Companies -> Icon(Icons.Default.Business,     contentDescription = label)
                            AppDestinations.Map       -> Icon(Icons.Default.LocationOn,   contentDescription = label)
                            else                      -> Icon(Icons.Default.PhotoLibrary, contentDescription = label)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestinations.Welcome,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppDestinations.Welcome) {
                WelcomeScreen(
                    onCompaniesClick = { navController.navigate(AppDestinations.Companies) },
                    onMapClick       = { navController.navigate(AppDestinations.Map) }
                )
            }

            composable(AppDestinations.Companies) {
                CompaniesScreen(
                    categories = AppData.categories,
                    onCompanyClick = { companyId ->
                        navController.navigate(AppDestinations.companyDetail(companyId))
                    },
                    onEdificioClick = { codigo ->
                        navController.navigate(AppDestinations.edificioDetail(codigo))
                    }
                )
            }

            composable(AppDestinations.CompanyDetail) { backStackEntry ->
                val companyId = backStackEntry.arguments?.getString("companyId")?.toIntOrNull()
                if (companyId != null) {
                    CompanyDetailScreen(companyId = companyId)
                }
            }

            // Ecrã do edifício — hero + lista de empresas
            composable(AppDestinations.EdificioDetail) { backStackEntry ->
                val codigo = backStackEntry.arguments?.getString("codigoEdificio") ?: ""
                EdificioDetailScreen(
                    codigoEdificio = codigo,
                    onCompanyClick = { companyId ->
                        navController.navigate(AppDestinations.companyDetail(companyId))
                    }
                )
            }

            composable(AppDestinations.Map) {
                MapScreen()
            }

            composable(AppDestinations.Media) {
                MediaScreen()
            }
        }
    }
}