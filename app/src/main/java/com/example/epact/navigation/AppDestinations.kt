package com.example.epact.navigation

object AppDestinations {
    const val Welcome  = "welcome"
    const val Companies = "companies"
    const val Map      = "map"
    const val Media    = "media"
    const val CompanyDetail = "company/{companyId}"

    fun companyDetail(companyId: Int): String = "company/$companyId"
}