package com.cjfc.recipesmanager.presentation.payload

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalTime

data class RecipePayload(
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
    @get:[JsonFormat(pattern = "HH:mm")]
    val time: LocalTime?
)