package com.cjfc.recipesmanager.domain

data class Recipe(
    val id: String,
    val name: String? = null,
    val description: String? = null,
    val favourite: Boolean? = null,
    val ingredients: String? = null,
    val origin: String? = null,
    val temperature: String? = null,
    val labels: List<String>? = null,
    val course: String? = null
)