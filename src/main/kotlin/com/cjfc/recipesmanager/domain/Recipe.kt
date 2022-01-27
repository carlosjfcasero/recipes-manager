package com.cjfc.recipesmanager.domain

import java.time.LocalTime

data class Recipe(
    val id: String?,
    val name: String?,
    val description: String?,
    val favourite: Boolean?,
    val ingredients: String?,
    val origin: String?,
    val temperature: String?,
    val tags: List<String>?,
    val course: String?,
    val url: String?,
    val time: LocalTime?
)