package com.cjfc.recipesmanager.domain

data class Recipe(
    val id: String?,
    val name: String?,
    val description: String?,
    val favourite: Boolean?,
    val ingredients: String?,
    val origin: String?,
    val temperature: String?,
    val labels: List<String>?,
    val course: String?
)