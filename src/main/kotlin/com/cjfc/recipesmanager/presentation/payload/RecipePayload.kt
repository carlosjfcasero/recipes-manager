package com.cjfc.recipesmanager.presentation.payload

data class RecipePayload(
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