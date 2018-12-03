package br.com.mobapps.dynamicfeature.date

sealed class Category {
    object Food: Category()
    object Clothing: Category()
    object Recreation: Category()
    object Health: Category()
}