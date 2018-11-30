package br.com.mobapps.dynamicfeature

data class Bill(
        val description: String,
        val value: Double,
        val category: Category)

sealed class Category {
    object Food: Category()
    object Clothing: Category()
    object Recreation: Category()
    object Health: Category()
}