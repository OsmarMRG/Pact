package com.example.epact.navigation

object AppDestinations {
    const val Welcome       = "welcome"
    const val Companies     = "companies"
    const val Map           = "map"
    const val Media         = "media"
    const val CompanyDetail = "company/{companyId}"
    const val EdificioDetail = "edificio/{codigoEdificio}"

    fun companyDetail(companyId: Int): String = "company/$companyId"
    fun edificioDetail(codigo: String): String = "edificio/$codigo"
}